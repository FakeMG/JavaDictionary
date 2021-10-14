package groupId.JavaFX;

import groupId.JavaDictionary.TextToSpeech;
import groupId.JavaDictionary.Word;
import groupId.JavaDictionary.dataBase;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class VocabularyStageController implements Initializable {
    ObservableList<String> fullWordArray = FXCollections.observableArrayList();

    @FXML
    public Button backButton;
    @FXML
    public ImageView speaker;
    @FXML
    public Button addWordButton;
    @FXML
    public TextArea engWord;
    @FXML
    public TextArea vietWord;
    @FXML
    public TextField searchBar;
    @FXML
    public ListView<String> mainListView;

    public void initListView() {
        dataBase.readFromDatabase(fullWordArray);
        initSearch();
    }

    public void showWord() {
        mainListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String currentWord = mainListView.getSelectionModel().getSelectedItem();
                engWord.setText(currentWord);
                vietWord.setText(dataBase.lookupSingleWord(currentWord));
            }
        });
    }

    public void edit() {
        //init context menu to implement edit and delete vocabulary in listview
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem();
        editItem.textProperty().bind(Bindings.format("Edit"));
        MenuItem deleteItem = new MenuItem();
        deleteItem.textProperty().bind(Bindings.format("Delete"));
        contextMenu.getItems().addAll(editItem, deleteItem);

        //set listview handler when click item
        mainListView.setOnMouseClicked(mouseEvent -> {
            String selectedWord = mainListView.getSelectionModel().getSelectedItem();

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                //set action when edit item
                editItem.setOnAction(actionEvent -> {
                    Word newWord = editingDialog(selectedWord);
                    if (newWord != null) {
                        dataBase.updateDatabase(newWord.getWordTarget(), newWord.getWordExplain(), selectedWord);
                        int index = fullWordArray.indexOf(newWord.getWordTarget());
                        fullWordArray.set(index, newWord.getWordTarget());
                        showAlert("Chỉnh sửa từ thành công!");
                    }
                });

                //set action when delete item
                deleteItem.setOnAction(actionEvent -> {
                    dataBase.deleteFromDatabase(selectedWord);
                    fullWordArray.remove(selectedWord);
                    showAlert("Xóa từ thành công!");
                });
                contextMenu.show(mainListView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    public void initSearch() {
        //filter data when user find vocabulary
        FilteredList<String> filteredData = new FilteredList<>(fullWordArray, s -> true);
        mainListView.setItems(filteredData);
        searchBar.textProperty().addListener((observableValue, s, t1) -> {
            String filter = searchBar.getText();
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s1 -> true);
            } else {
                filteredData.setPredicate(s1 -> s1.toLowerCase().startsWith(filter.toLowerCase()));
            }
        });
    }

    private Word editingDialog(String word) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Dialog");

        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, cancelBtn);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField engWord = new TextField();
        TextField meaning = new TextField();

        if (word.isEmpty()) {
            engWord.setPromptText("English Word");
            meaning.setPromptText("Meaning");
        } else {
            engWord.setText(word);
            meaning.setText(dataBase.lookupSingleWord(word));
        }

        grid.add(new Label("English Word:"), 0, 0);
        grid.add(engWord, 1, 0);
        grid.add(new Label("Meaning:"), 0, 1);
        grid.add(meaning, 1, 1);
        dialog.getDialogPane().setContent(grid);

        AtomicBoolean check = new AtomicBoolean(false);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == okBtn) {
                check.set(true);
            }
        });
        if (check.get()) return new Word(engWord.getText(), meaning.getText());
        else return null;
    }

    //clickable
    public void addWord() {
        addWordButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Word add_word = editingDialog("");
            if (add_word != null) {
                dataBase.insertToDatabase(add_word.getWordTarget(), add_word.getWordExplain());
                fullWordArray.add(add_word.getWordTarget());
                showAlert("Thêm từ thành công!");
            }
        });
    }

    public void speak() {
        speaker.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            String text = engWord.getText();
            TextToSpeech.speak(text);
        });
    }

    private void showAlert(String txt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert!");
        alert.setContentText(txt);
        alert.showAndWait();
    }

    public void back() throws IOException {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
                Scene scene = new Scene(root, 600, 400);
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setTitle("Dictionary");
                stage.setScene(scene);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initListView();
        showWord();
        edit();
    }
}
