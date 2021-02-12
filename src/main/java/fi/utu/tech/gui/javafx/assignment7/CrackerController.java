package fi.utu.tech.gui.javafx.assignment7;

import fi.utu.tech.gui.javafx.WordIterator;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class CrackerController {
    HashCrackTask crackerTask;

    @FXML
    private ListView<String> reversedList;

    @FXML
    private TextField hashInputField;

    @FXML
    private Button crackBtn;

    @FXML
    private ProgressBar crackingProgressBar;

    @FXML
    private Label statusLabel;

    @FXML
    void crackBtnAction(ActionEvent event) {
    	
    	if(crackBtn.getText().equals("Crack")) {
    		crackerTask = new HashCrackTask(hashInputField.getText(), 4, WordIterator.DEFAULT_DICT, "md5", "utf-8");
    		Thread th = new Thread(crackerTask);
    		th.setDaemon(true);
    		th.start();
    	} else {
    		crackerTask.cancel();
    	}
        
        // päivittää napin tekstin
        crackBtn.textProperty().bind(
        		Bindings.when(
        				crackerTask.runningProperty()
        				).then(Bindings.createStringBinding(
        						() -> {
        							return new String("Cancel");
        						},
        						crackerTask.runningProperty()
        						)).otherwise(Bindings.createStringBinding(
        						() -> {
        							return new String("Crack");
        						},
        						crackerTask.runningProperty()
        						)));
        		
        // sidos - paikka??!
    	statusLabel.textProperty().bind(crackerTask.messageProperty());
    	crackingProgressBar.progressProperty().bind(crackerTask.progressProperty());
        
    	/**
        crackerTask.setOnRunning(e1 ->
    		crackBtn.setDisable(true)
    	);
    	**/
        crackerTask.setOnSucceeded(e2 -> {
        	reversedList.getItems().add(crackerTask.getValue());
        	//crackBtn.setDisable(false);
        }
        	);        
        	
}

}

