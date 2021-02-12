package fi.utu.tech.gui.javafx.assignment8;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class CrackerController {
	
	HashCrackService palvelu;
    
    public void initialize() {
    	palvelu = new HashCrackService();
    }

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
    		palvelu.setHash(hashInputField.getText());
    		palvelu.createTask();
    		palvelu.reset();
    		palvelu.start();

    	} else {
    		palvelu.cancel();
    	}
        
        // päivittää napin tekstin
        crackBtn.textProperty().bind(
        		Bindings.when(
        				palvelu.runningProperty()
        				).then(Bindings.createStringBinding(
        						() -> {
        							return new String("Cancel");
        						},
        						palvelu.runningProperty()
        						)).otherwise(Bindings.createStringBinding(
        						() -> {
        							return new String("Crack");
        						},
        						palvelu.runningProperty()
        						)));
        		
        // sidos - paikka??!
    	statusLabel.textProperty().bind(palvelu.messageProperty());
    	crackingProgressBar.progressProperty().bind(palvelu.progressProperty());
        
    	/**
        crackerTask.setOnRunning(e1 ->
    		crackBtn.setDisable(true)
    	);
    	**/
        palvelu.setOnSucceeded(e2 -> {
        	reversedList.getItems().add(palvelu.getValue());
        	//crackBtn.setDisable(false);
        }
        	);        
        	
}
}
