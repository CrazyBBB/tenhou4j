package tenhodownloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Downloader.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tenhou Downloader");
        stage.show();
    }
}