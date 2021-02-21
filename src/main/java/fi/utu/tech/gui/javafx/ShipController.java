package fi.utu.tech.gui.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class ShipController {
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
    }


}
