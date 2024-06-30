import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        URL fxmlUrl = new URL("file:C:\\Users\\Amalitech\\Documents\\TextProcessingTool\\Resources/Main.fxml"); // Replace with your actual path
        Parent root = FXMLLoader.load(fxmlUrl);

        // Set the scene
        Scene scene = new Scene(root);

        // Set the stage
        primaryStage.setTitle("Text Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
