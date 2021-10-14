package groupId.JavaFX;

import groupId.JavaDictionary.dataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            dataBase.connect();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
