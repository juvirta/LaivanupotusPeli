package fi.utu.tech.gui.javafx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import fi.utu.tech.gui.javafx.Ship.Coordinates;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class PlacementController {
	private String player1, player2;
	private int[] shipCounts;
	private int boardSize;
	private GridPane grid = new GridPane();
	// Observable for showing count of ships left to be placed
	private ArrayList<IntegerProperty> shipCountPropList = new ArrayList<IntegerProperty>(5);
	// rectangle drawn during drag and drop
	private Rectangle x;
	
	//Attributes regarding ship that is being placed (drag and drop)
	private Ship.Type shipType;
	private boolean isTurned = false;
	
	// reference to current active node considered as drop target
	private Rectangle currentTarget;
	// shows whether ship can be placed on active node
	private boolean dragReleaseOk;
	// shows if p1 or p2 is placing ships at the moment
	private boolean player1Turn = true;
	// These store ship placements
	// 1st is WIP board, 2nd final board for p1 and 3rd final board for p2
	private Board board, p1Board, p2Board;
	//scalar used in drawing
	private int cellSize;
	//preset grid (board size) in pixels
	private int gridSize = 300;
	//stores references to nodes in grid
	private Shape[][] gridNodes;
	private Stage stage;
	
	// Initializes controller. Initialize() not used as parameters needed in setup.
	public void setup(String p1,String p2, int[] l,int r,Stage s) {
		
		// Saving the received parameters as class variables
		stage = s;
		player1 = new String(p1);
		player2 = new String(p2);
		shipCounts = l;
		boardSize = r;
		
		// Initializing class attributes based on parameters received 
		headerLabel.setText(player1+", aseta kaikki laivat");
		gridNodes = new Shape[boardSize][boardSize];
		board = new Board(boardSize);
		for (int i = 0; i<shipCounts.length; i++) {
			shipCountPropList.add(new SimpleIntegerProperty(shipCounts[i]));
		}
		cellSize = gridSize/boardSize;
		
		//setting grid to BorderPane
		borderPane.setCenter(grid);
		// should be used in debugging only, but works well here to form board with cells...
		grid.setGridLinesVisible(true);
		// limiting grid max size so it stays in sync with cell size
		grid.setMaxSize(boardSize*cellSize+boardSize, boardSize*cellSize+boardSize);
		
		//Handler added for DragRelease. Needed in case drag ends outside of grid (board)
		borderPane.setOnMouseDragReleased((e)->{
			imageBox.setMouseTransparent(false);
			x.setMouseTransparent(false);
			borderPane.getChildren().remove(x);
		});
		
		//call function to add nodes to grid and listeners to those nodes
		setupGridNodes();
		
		//setup list of ships and their labels to be dragged
		// add listeners as well
		setupShipDragList();

		// Add handler to stage for rotate action
		// Handler fires only when x-rectangle is drawn (meaning that drag is in progress)
		stage.addEventFilter(KeyEvent.KEY_PRESSED, e2 ->{
			if (e2.getCode()==KeyCode.R && x != null) {rotateX();}
		});
	}
	
	// Setting buttons to disable as there are no ships on board in the beginning.
	public void initialize() {
		confirmButton.setDisable(true);
		removeAllButton.setDisable(true);	
	}
	
	@FXML
	BorderPane borderPane;
	
	@FXML
	Label headerLabel;
	
	@FXML
	Button confirmButton, removeAllButton, exitButton;
	
	@FXML
	VBox imageBox, nameBox, countBox;
	
	//Confirm button handler. Saves player board and switches turn.
	@FXML
	void confirmEvent(ActionEvent event) {
		try {
			// this block is run when player1 confirms
			if(player1Turn) {
				// cloning the current board and saving it
				p1Board = (Board) board.clone();
				// resetting (emptying) the grid
				setupGridNodes();
				// creating new Board for p2
				board = new Board(boardSize);
				// resetting the shipCountPropList to match with settings
				for (int i = 0; i<shipCounts.length; i++) {
					shipCountPropList.get(i).set(shipCounts[i]);
				}
				player1Turn = false;
				headerLabel.setText(player2+", aseta kaikki laivat");
				// send message
				Alert a = new Alert(Alert.AlertType.INFORMATION,player2+": vuoro alkaa.");
				a.setTitle("Viesti");
				a.setHeaderText(player1+": laivat on tallennettu.");
				a.showAndWait();
				
				// This block runs when p2 confirms. This starts a new Game
			} else {
				p2Board = (Board) board.clone();
				setupGridNodes();
				Alert a = new Alert(Alert.AlertType.INFORMATION,"Peli alkaa.");
				a.setTitle("Viesti");
				a.setHeaderText(player2+": laivat on tallennettu.");
				a.showAndWait();
				Game game = new Game(player1, player2, p1Board, p2Board, stage);
				game.startGame();
				}	
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	//removeAllButton handler. Empties grid and board. Resets shipCountPropList.
	//user confirmation is asked for
	@FXML
	void removeAllEvent(ActionEvent event) {
		Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION);
		removeAlert.setTitle("Varoitus");
		removeAlert.setHeaderText("Haluatko poistaa kaikki laivat kentältä?");
		Optional<ButtonType> result = removeAlert.showAndWait();
		if(result.get() == ButtonType.OK) {
			setupGridNodes();
			board = new Board(boardSize);
			for (int i = 0; i<shipCounts.length; i++) {
				shipCountPropList.get(i).set(shipCounts[i]);
			}
		}
	}
	
	@FXML
	//exitButton handler. Loads Settings screen and gives control to SettingsControl.
	// user confirmation is asked for
	void exitEvent(ActionEvent event) {
		Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Laivojen asetuksia ei tallenneta");
		exitAlert.setTitle("Varoitus");
		exitAlert.setHeaderText("Haluatko poistua laivojen asettelusta?");
		Optional<ButtonType> result = exitAlert.showAndWait();
		if(result.get() == ButtonType.OK) {
			var resourceRoot = getClass();
			var form = "Settings.fxml";
			var loader = new FXMLLoader(resourceRoot.getResource(form));
			Parent root;
			try {
				root = loader.load();
				//Starting the settings screen
				stage.setTitle("Laivanupotus");
				var scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//method that checks whether ship can be placed in a given place
	// parameter is the node considered as target
	private boolean checkFit(Rectangle a) {
		int row = GridPane.getRowIndex(a);
		int col = GridPane.getColumnIndex(a);
		double shipW = x.getWidth()/gridSize*boardSize;
		double shipH = x.getHeight()/gridSize*boardSize;
		//check for grid borders
		if(col+shipW>boardSize || row + shipH>boardSize) {return false;}
		//check for other ships
		Ship ship = new Ship(shipType);
		ship.setCoordinates(row, col, isTurned);
		return board.checkNewShipFit(ship);
	}
	
	//this method returns a node by index of gridpane
	public Node getNodeByIndex(int row, int col, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> children = gridPane.getChildren();
		for (Node node : children) {
			if(GridPane.getColumnIndex(node)==col && GridPane.getRowIndex(node)==row) {
				result = node;
				break;
			}
		}
		return result;
	}
	
	//This method "rotates" the rectangle x by first removing it
	// and then creating a new one with swapped width and height
	// method also calls checkDragReleaseOk() after rotate
	private void rotateX() {
		try {
			double oX = x.getX();
			double oY = x.getY();
			double oW = x.getWidth();
			double oH = x.getHeight();
			borderPane.getChildren().remove(x);
			x = new Rectangle(oH,oW);
			x.setX(oX);
			x.setY(oY);
			x.setMouseTransparent(true);
			borderPane.getChildren().add(x);
			isTurned = !isTurned;
			checkDragReleaseOk();
		} catch (Exception e) {
			return;
		}
	}
	
	// This method checks if ship can be dropped to current target
	// Method also colors the nodes accordingly
	private void checkDragReleaseOk() {
		if(!Objects.isNull(currentTarget)) {
			if(checkFit(currentTarget)) {
				currentTarget.setFill(Color.GREEN);
				dragReleaseOk=true;
			} else if (currentTarget.getFill()==Color.RED){
				dragReleaseOk=false;
			} else {
				currentTarget.setFill(Color.BLUE);
				dragReleaseOk=false;
			}		
		}
	}
	
	//This method sets up the grid which acts as board for ship placement
	// All listeners to grid cells are added here
	private void setupGridNodes() {
		// for each cell in grid
		for (int i = 0; i<boardSize; i++) {
			for (int j = 0; j<boardSize; j++) {
				// create rectangle, set it blue, add to grid and gridNodes (for referencing)
				Rectangle a = new Rectangle(cellSize, cellSize);
				a.setFill(Color.BLUE);	
				grid.add(a, i, j);
				gridNodes[i][j]=a;
				
				// setup dragEntered handler
				// sets node as target and calls method to see if release is ok
				a.setOnMouseDragEntered((e)->{
					currentTarget = a;
					checkDragReleaseOk();
				});
				
				// setup dragReleased handler
				// removes x rectangle
				// adds ship, if ship fits
				a.setOnMouseDragReleased((e)->{
					int col = GridPane.getColumnIndex(a);
					int row = GridPane.getRowIndex(a);
					imageBox.setMouseTransparent(false);
					x.setMouseTransparent(false);
					borderPane.getChildren().remove(x);
					if(dragReleaseOk) {addShip(row,col);}	
				});			
				
				// setup dragExit handler
				// sets color back to blue and updates dragReleaseOK to false
				a.setOnMouseDragExited((e)->{
					currentTarget = null;
					if(!(a.getFill()==Color.RED)) {
						a.setFill(Color.BLUE);
					}
					dragReleaseOk=false;				
				});		
			}
			
			//creating grid constraints that make cells visible
			grid.getColumnConstraints().add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
			grid.getRowConstraints().add(new RowConstraints(-1, -1, -1, Priority.ALWAYS, VPos.CENTER, false));		
		}
	}
	
	// This method sets up a list of ship pictures for drag and drop, list of ship names and a
	// list of count of ships to be placed. 
	// This method also sets up drag and drop action handlers on ship images
	private void setupShipDragList() {
		// fixed parameter used in image sizing
		int fontHeight = 15;
		// Headers for 3 columns
		Rectangle invisible = new Rectangle(fontHeight,fontHeight);
		invisible.setVisible(false);
		imageBox.getChildren().add(invisible);
		nameBox.getChildren().add(new Label("LAIVA"));
		countBox.getChildren().add(new Label("KPL"));
		// add rows:
		for (int i=0; i<5; i++) {

			// parameter used in action handlers
			int index = i;
			
			// create rectangles and fill with ship images
			Rectangle s = new Rectangle((Ship.shipSize(Ship.typeMap(i)))*fontHeight,fontHeight);
			s.setFill(new ImagePattern(new Image(getClass().getResource("ship.png").toExternalForm())));
			imageBox.getChildren().add(s);
			
			//create ship name labels and count labels
			nameBox.getChildren().add(new Label(Ship.typeMap(i).toString()));
			Label countLabel = new Label();
			countLabel.textProperty().bind(shipCountPropList.get(i).asString());
			countBox.getChildren().add(countLabel);
			
			//start drag and drop only if there are ships left to be placed
			// when drag and drop starts, create new rectangle x to be used in dragging
			s.setOnMousePressed((e)->{
				if(shipCountPropList.get(index).intValue()>0) {
					isTurned = false;
					shipType = Ship.typeMap(index);
					x = new Rectangle(cellSize*(Ship.shipSize(Ship.typeMap(index))),cellSize);
					borderPane.getChildren().add(x);
					x.setX(e.getSceneX());
					x.setY(e.getSceneY());	
					x.setMouseTransparent(true);
					imageBox.setMouseTransparent(true);
					e.setDragDetect(true);
				}
			});
			
			// when dragged, move the rectangle x
			s.setOnMouseDragged((e)->{
				Double sX = e.getSceneX();
				Double sY = e.getSceneY();
				x.setX(sX);
				x.setY(sY);
				e.setDragDetect(false);
			});
			
			// start full drag on drag detect, so actionhandlers in gridpane can be used in drop action
			s.setOnDragDetected((e)->{s.getScene().startFullDrag();});
		}
	}
	
	//method adds a new ship to selected location on player's board
	// activates remove-button, updates shipCountPropList
	private void addShip(int row, int col) {
		Ship ship = new Ship(shipType);
		ship.setCoordinates(row, col, isTurned);
		board.setShip(ship);
		removeAllButton.setDisable(false);
		ArrayList<Coordinates>shipCoordinates = ship.getCoordinateList();
		for(Coordinates coordinates : shipCoordinates) {
			gridNodes[coordinates.y][coordinates.x].setFill(Color.RED);
		}
		int newCount = shipCountPropList.get(Ship.getIndexByType(shipType)).intValue()-1;
		shipCountPropList.get(Ship.getIndexByType(shipType)).set(newCount);
		checkIfAllShipsSet();
		
	}
	
	//method to check if all ships are set
	private void checkIfAllShipsSet(){
		for(IntegerProperty laiva : shipCountPropList) {
			if(laiva.intValue()>0) {return;}
		}
		confirmButton.setDisable(false);
		}

}
