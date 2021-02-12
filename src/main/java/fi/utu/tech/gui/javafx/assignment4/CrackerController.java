package fi.utu.tech.gui.javafx.assignment4;


import fi.utu.tech.gui.javafx.WordIterator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CrackerController {

    @FXML
    private ListView<String> reversedList;

    @FXML
    private TextField hashInputField;

    @FXML
    private Button crackBtn;

    @FXML
    void crackBtnAction(ActionEvent event) {
        HashCrackTask crackerTask = new HashCrackTask(hashInputField.getText(), 4, WordIterator.DEFAULT_DICT, "md5", "utf-8");
        Thread th = new Thread(crackerTask);
        th.setDaemon(true);
        th.start();
        crackerTask.setOnRunning(e1 ->
    		crackBtn.setDisable(true)
    	);
        crackerTask.setOnSucceeded(e2 -> {
        	reversedList.getItems().add(crackerTask.getValue());
        	crackBtn.setDisable(false);
        }
        	);        
}
}

