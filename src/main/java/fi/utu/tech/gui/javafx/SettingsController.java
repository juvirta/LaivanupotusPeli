package fi.utu.tech.gui.javafx;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This class controls the initial settings of the game
 */

public class SettingsController {
	
	private String player1;
	private String player2;
	// stores count of different ships selected
	private int[] shipTypeCount = new int[5];
	private int boardSize;
	// Observables used in indicating correctness of selections
	private SimpleBooleanProperty fit = new SimpleBooleanProperty();
	private SimpleBooleanProperty names = new SimpleBooleanProperty();
	private SimpleBooleanProperty noShips = new SimpleBooleanProperty();
	
	public void initialize() {
		
		// Setting up options to comboBoxes
		for (int i=0; i<10; i++) {
			ship0.getItems().add(i);
			ship1.getItems().add(i);
			ship2.getItems().add(i);
			ship3.getItems().add(i);
			ship4.getItems().add(i);
		}
		for (int i=5; i<11; i++) {
			boardSetting.getItems().add(i);
		}
		
		// Doing the preselection as per specs
		ship0.getSelectionModel().select(1);
		ship1.getSelectionModel().select(2);
		ship2.getSelectionModel().select(3);
		ship3.getSelectionModel().select(4);
		ship4.getSelectionModel().select(5);
		boardSetting.getSelectionModel().select(5);
		
		// saving preselections and updating labels and button
		saveSelections();
		checkSelections();
		checkNames();
		
		// adding listeners to all fields
		// checks correctness of input and updates labels & button
		ship0.setOnAction((e)->{saveSelections(); checkSelections();});
		ship1.setOnAction((e)->{saveSelections(); checkSelections();});
		ship2.setOnAction((e)->{saveSelections(); checkSelections();});
		ship3.setOnAction((e)->{saveSelections(); checkSelections();});
		ship4.setOnAction((e)->{saveSelections(); checkSelections();});
		boardSetting.setOnAction((e)->{saveSelections(); checkSelections();});
		name1.setOnKeyTyped((e)->{saveSelections(); checkNames();});
		name2.setOnKeyTyped((e)->{saveSelections(); checkNames();});
		
		// Binding label to fit and noShips -obsevables. Indicate if ships fit or not
		fitLabel.textProperty().bind(Bindings.createStringBinding(
				() -> {
					if(noShips.get()) { return "Lisää ainakin yksi laiva";}
					else if(fit.get()) { return "";}
					else { return "Vähennä laivoja tai suurenna kenttää";}
				},fit, noShips));
		
		// Binding label to names-obsevable. Indicate if player names are given
		nameLabel.textProperty().bind(Bindings.createStringBinding(
				() -> {
					if(names.get()) { return "";
					} else { return "Syötä pelaajien nimet";}
				},names));

	}
	
	@FXML
	private Label fitLabel, nameLabel;
	
	@FXML
	private TextField name1, name2;
	
	@FXML
	private ComboBox<Integer> boardSetting, ship0, ship1, ship2, ship3, ship4;
	
	@FXML
	private Button confirm;
	
	
	// ActionHandler tied to button
	// as all settings are already saved this only changes screen to next
	@FXML 
	void confirmSettings(ActionEvent event){	
		changeScreen();
		}
	
	//Method saves current selections from all input fields to class variables
	private void saveSelections() {
		shipTypeCount[0] = ship0.getValue();
		shipTypeCount[1] = ship1.getValue();
		shipTypeCount[2] = ship2.getValue();
		shipTypeCount[3] = ship3.getValue();
		shipTypeCount[4] = ship4.getValue();
		boardSize = boardSetting.getValue();
		player1 = new String(name1.getText());
		player2 = new String(name2.getText());
	}
	
	//Method checks if ships fit to the selected board size
	//Method also calls updateButton() to see if button should be disabled
	private void checkSelections() {
		int shipArea = shipTypeCount[0]*5+shipTypeCount[1]*4+shipTypeCount[2]*3+
				shipTypeCount[3]*3+shipTypeCount[4]*2;
		int boardArea = boardSize*boardSize;
		if(shipArea*2>boardArea) {
			fit.set(false);
			noShips.set(false);
			updateButton();
		} else if (shipArea==0) {
			fit.set(true);
			noShips.set(true);
			updateButton();
		} else {
			fit.set(true);
			noShips.set(false);
			updateButton();
		}
	}
	
	//Method checks if names are given (any String with length>0 is ok)
	//Method also calls updateButton() to see if button should be disabled
	private void checkNames() {
		if(player1.equals("")||player2.equals("")) {
			names.set(false);
			updateButton();
		} else {
			names.set(true);
			updateButton();
		}
	}
	
	// Activates / Disables button based on selections
	private void updateButton() {
		if(names.get() && fit.get() && !noShips.get()) {confirm.setDisable(false);
		} else {confirm.setDisable(true);}
	}
	
	// Changes screen to ship placing
	// Passes on selections made
	private void changeScreen() {
		Scene scene = confirm.getScene();
		Window window = scene.getWindow();
		Stage stage = (Stage) window;
		if(true) {
	        var resourceRoot = getClass();
	        var form = "Placement.fxml";
	        var loader = new FXMLLoader(resourceRoot.getResource(form));
	        Parent root;
			try {	
				root = loader.load();
				PlacementController controller = loader.getController();
		        controller.setup(player1, player2, shipTypeCount, boardSize, stage);
		        stage.setTitle("Laivanupotus");
		        scene.setRoot(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


