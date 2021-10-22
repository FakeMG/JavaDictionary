package groupId.JavaFX;

import groupId.JavaDictionary.DataBase;
import groupId.JavaDictionary.TextToSpeech;
import groupId.JavaDictionary.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class VocabularyController implements Initializable {
    ObservableList<String> fullWordArray = FXCollections.observableArrayList();
    FilteredList<String> filteredList;

    @FXML
    public Button backButton;
    @FXML
    public ImageView speakerImg;
    @FXML
    public TextField engWord;
    @FXML
    public TextField vietWord;
    @FXML
    public TextField searchBar;
    @FXML
    public ListView<String> mainListView;

    public String selectedWord = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initListView();
        updateSelectedWord();
    }

    public void initListView() {
        DataBase.readFromDatabase(fullWordArray);
        initSearch();
    }

    public void updateSelectedWord() {
        //update the word user clicked on
        mainListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedWord = mainListView.getSelectionModel().getSelectedItem();

                // cần điều kiện này để tránh lỗi
                if (selectedWord != null) {
                    engWord.setText(selectedWord);
                    vietWord.setText(DataBase.lookupSingleWord(selectedWord));
                }
            }
        });
    }

    public void initSearch() {
        filteredList = new FilteredList<>(fullWordArray, word -> true);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // lọc listView với những từ bắt đầu bằng chữ được điền trong searchBar
            // word là các giá trị trong filteredList
            // nếu s thỏa mãn điều kiện thì return true (hiện ra) else return false (ko hiện)
            filteredList.setPredicate(word -> newValue == null || newValue.isEmpty() || word.toLowerCase().startsWith(newValue.toLowerCase()));
        });

        SortedList<String> sortedList = new SortedList<>(filteredList);

        // sort listView
        sortedList.setComparator(new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                return arg0.compareToIgnoreCase(arg1);
            }
        });

        mainListView.setItems(sortedList);
    }

    private void showAlert(String txt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(txt);
        alert.showAndWait();
    }

    private Word editingDialog(String word) {
        Dialog<ButtonType> dialog = new Dialog<>();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //init button
        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, cancelBtn);

        TextField engWord = new TextField();
        TextField meaning = new TextField();

        if (word.isEmpty()) {
            engWord.setPromptText("English Word");
            meaning.setPromptText("Meaning");
        } else {
            engWord.setText(word);
            meaning.setText(DataBase.lookupSingleWord(word));
        }

        //add button, textField to layout
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
        Word newWord = editingDialog("");
        if (newWord != null) {
            if (DataBase.checkRepeatedWord(newWord.getWordTarget())) {
                showAlert("This word has already existed!");
                return;
            }
            DataBase.insertToDatabase(newWord.getWordTarget(), newWord.getWordExplain());
            fullWordArray.add(newWord.getWordTarget());
            showAlert("Word is added!");
        }
    }

    public void editWord() {
        if (!selectedWord.isEmpty()) {
            Word targetWord = editingDialog(selectedWord);
            if (targetWord != null) {
                //check repeated
                if (DataBase.lookupSingleWord(selectedWord).equals(targetWord.getWordExplain()) && selectedWord.equals(targetWord.getWordTarget())) {
                    showAlert("This word has already existed!");
                    return;
                }

                //check empty
                if (targetWord.getWordTarget().isEmpty() || targetWord.getWordExplain().isEmpty()) {
                    showAlert("Word or meaning is empty!");
                    return;
                }

                //update word
                DataBase.updateDatabase(targetWord.getWordTarget(), targetWord.getWordExplain(), selectedWord);
                int index = fullWordArray.indexOf(selectedWord);
                fullWordArray.set(index, targetWord.getWordTarget());
                showAlert("Word is updated!");
            }
        }
    }

    public void deleteWord() {
        if (!selectedWord.isEmpty()) {
            DataBase.deleteFromDatabase(selectedWord);
            fullWordArray.remove(selectedWord);
            showAlert("Xóa từ thành công!");
        }
    }

    public void speak() {
        String text = engWord.getText();
        TextToSpeech.speak(text);
    }

    public void back() throws IOException {
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
    }
}
