package groupId.JavaFX;

import groupId.JavaDictionary.TextToSpeech;
import groupId.JavaDictionary.Translator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TranslateController implements Initializable {
    @FXML
    public TextField targetWord;
    @FXML
    public TextArea translatedWord;
    @FXML
    public ImageView speakerImg;
    @FXML
    public ProgressIndicator loading;
    @FXML
    public Button translateButton;
    @FXML
    public ImageView arrowImg;
    @FXML
    public Text languageIn;
    @FXML
    public Text languageOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loading.setVisible(false);
        speak();
        swapLanguage();
        translate();
    }

    public void swapLanguage() {
        arrowImg.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (languageIn.getText().equals("English")) {
                languageIn.setText("Vietnamese");
                languageOut.setText("English");
            } else {
                languageIn.setText("English");
                languageOut.setText("Vietnamese");
            }

            //Swap nội dung
            String text = targetWord.getText();
            targetWord.setText(translatedWord.getText());
            translatedWord.setText(text);
        });
    }

    public void translate() {
        translateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String result;

                    if (targetWord.getText().equals("")) {
                        showAlert();
                        return;
                    }

                    if (languageIn.getText().equals("English")) {
                        result = Translator.translate("en", "vi", targetWord.getText());
                    } else {
                        result = Translator.translate("vi", "en", targetWord.getText());
                    }
                    translatedWord.setText(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void speak() {
        speakerImg.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            String text = targetWord.getText();
            TextToSpeech.speak(text);
        });
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Please enter something!");
        alert.showAndWait();
    }


    public void back() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Scene scene = new Scene(root, 600, 400);
        Stage stage = (Stage) targetWord.getScene().getWindow();
        stage.setTitle("Dictionary");
        stage.setScene(scene);

        stage.show();
    }

}
