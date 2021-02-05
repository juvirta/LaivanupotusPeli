package fi.utu.tech.gui.javafx.assignment2;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
        final var inputHash = hashInputField.getText();
        try {
            String result = new HashCrack(inputHash, 4, WordIterator.DEFAULT_DICT, "md5", "utf-8").bruteForce();
            reversedList.getItems().add(result);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
