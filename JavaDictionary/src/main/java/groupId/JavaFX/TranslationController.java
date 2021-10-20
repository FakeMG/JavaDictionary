package groupId.JavaFX;

import groupId.JavaDictionary.TextToSpeech;
import groupId.JavaDictionary.Translator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TranslationController {
    @FXML
    public TextField targetWord;
    @FXML
    public TextArea translatedWord;
    @FXML
    public ImageView speakerImg;
    @FXML
    public Button translateButton;
    @FXML
    public ImageView arrowImg;
    @FXML
    public Text languageIn;
    @FXML
    public Text languageOut;

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
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

    //clickable
    public void swapLanguage() {
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
    }

    public void translate() {
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

    public void speak() {
        String text = targetWord.getText();
        TextToSpeech.speak(text);
    }
}
