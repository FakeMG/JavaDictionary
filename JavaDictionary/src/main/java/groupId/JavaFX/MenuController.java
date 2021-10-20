package groupId.JavaFX;

import groupId.JavaDictionary.TextToSpeech;
import groupId.JavaDictionary.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public Button exitButton;

    public void vocabScene(ActionEvent event) throws IOException {
        switchScene(event, "vocabulary.fxml", "Vocabulary");
    }

    public void translateScene(ActionEvent event) throws IOException {
        switchScene(event, "translation.fxml", "Translation");
    }

    public void switchScene(ActionEvent event, String sceneName, String title) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneName));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
    }

    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        TextToSpeech.close();
        DataBase.close();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataBase.connect();
        TextToSpeech.init();
    }
}
