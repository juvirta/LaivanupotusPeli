package fi.utu.tech.gui.javafx.assignment3;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import fi.utu.tech.gui.javafx.WordIterator;
import fi.utu.tech.gui.javafx.assignment2.HashCrack;
import javafx.application.Platform;
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
        final var inputHash = hashInputField.getText();
        // Tehtävän 1 toteutus 
        Runnable block = () -> {
        try {
        	// Tehtävän 3 toteutus
        	Platform.runLater(() -> crackBtn.setDisable(true));
            String result = new HashCrack(inputHash, 4, WordIterator.DEFAULT_DICT, "md5", "utf-8").bruteForce();
         // Tehtävän 3 toteutus
            Platform.runLater(() -> reversedList.getItems().add(result));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
     // Tehtävän 3 toteutus
        Platform.runLater(() -> crackBtn.setDisable(false));
        };
        new Thread(block).start();
        }

}
