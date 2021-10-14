package groupId.JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    public Button exitButton;

    public void vocabScene(ActionEvent event) throws IOException {
        switchScene(event, "vocabulary_stage.fxml", "Vocabulary");
    }

    public void translateScene(ActionEvent event) throws IOException {
        switchScene(event, "translate_stage.fxml", "Translate");
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
        stage.close();
    }
}
