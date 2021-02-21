package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
    public void start(Stage stage) throws Exception {
        // Getting the reference to "class object of this class"
        var resourceRoot = getClass();
        // The fxml filename that is in resources folder
        var form = "Laiva.fxml";

        // Give the FXML resource to the FXMLLoader
        var loader = new FXMLLoader(resourceRoot.getResource(form));

        // Load and parse the FXML into an Java object (Parent)
        Parent root = loader.load();

        // This is just the usual: Setting scene, showing stage
        stage.setTitle("Laivanupotus");
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
