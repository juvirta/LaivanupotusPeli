package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	/*
	 * This is the class that starts the Application and loads the
	 * settings screen. 
	 */

	@Override
    public void start(Stage stage) throws Exception {
       
		// Setting up Settings screen 
		var resourceRoot = getClass();
        var form = "Settings.fxml";
        var loader = new FXMLLoader(resourceRoot.getResource(form));
        Parent root = loader.load();
        
        //Starting the settings screen
        stage.setTitle("Laivanupotus");
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
