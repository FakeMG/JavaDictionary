package groupId.JavaFX;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class Scene2Controller {
    @FXML
    Text name;

    public void setName(String p_name) {
        name.setText("Welcome: " + p_name);
    }
}
