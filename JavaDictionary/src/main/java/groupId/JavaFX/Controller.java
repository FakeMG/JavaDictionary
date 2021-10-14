package groupId.JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Text wrongPassword;

    public void switchScenes2(ActionEvent event) throws IOException {
        if (password.getText().equals("123")) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Scene2.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            String name = username.getText();
            Scene2Controller scene2Controller = fxmlLoader.getController();
            scene2Controller.setName(name);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Java Translation App");
            stage.setScene(scene);
            stage.show();
        } else {
            wrongPassword.setVisible(true);
        }
    }
}
