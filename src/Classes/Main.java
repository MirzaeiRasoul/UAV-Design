package Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("../Views/FirstView1.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Resources/css/First.css").toExternalForm());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}