package fi.utu.tech.gui.javafx;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// This is the class for game play

public class Game {
	
	//player names
	protected String p1, p2;
	// player boards
	protected Board p1Board, p2Board;
	//size (width and height of the board)
	protected int boardSize;
	//stage for passing to new screens as needed
	private Stage stage;
	//indicates whose turn is active
	protected boolean p1Turn;
	
	
	public Game(String p1, String p2, Board p1Board, Board p2Board, Stage stage) {
		this.p1=p1;
		this.p2=p2;
		this.p1Board=p1Board;
		this.p2Board=p2Board;
		this.stage=stage;
		this.p1Turn=true;
	}
	
	//PlacementController calls this when game starts
	public void startGame() {
		showChangeScreenAndChangeTurn();
	}
	
	//Shows change screen and changes p1Turn variable
	public void showChangeScreenAndChangeTurn() {
		BorderPane pane = new BorderPane();
		Label title = new Label("Vuoronvaihto");
		title.setFont(new Font("System",18));
		BorderPane.setMargin(title, new Insets(10));
		pane.setTop(title);
		Label middleLabel1 = new Label();
		Label middleLabel2 = new Label();
		if(p1Turn) {
			middleLabel1.setText(p1 +": vuoro alkaa.");
			middleLabel2.setText(p2+": Siirry pois koneelta.");
		} else {
			middleLabel1.setText(p1 +": vuoro alkaa.");
			middleLabel2.setText(p2+": Siirry pois koneelta.");
		}
		middleLabel1.setWrapText(true);
		middleLabel2.setWrapText(true);
		VBox vBox = new VBox();
		vBox.getChildren().add(middleLabel1);
		vBox.getChildren().add(middleLabel2);
		pane.setCenter(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		Button okButton = new Button("  OK  ");
		okButton.setOnAction((e)->{
			p1Turn=!p1Turn;
			middleLabel1.setText("Juhan osuus loppuu tähän ;)");
			middleLabel2.setText("");
		});
		pane.setBottom(okButton);
		BorderPane.setAlignment(okButton, Pos.CENTER);
		BorderPane.setMargin(okButton, new Insets(30));
		stage.setScene(new Scene(pane, 450, 450));
		stage.show();
	}
	
	// for debugging only
	public void printBoard() {
		for(int i=0;i<p1Board.gameBoard[0].length;i++) {
			for(int j=0;j<p1Board.gameBoard[0].length;j++) {
				System.out.print(p1Board.gameBoard[i][j]);
			}
			System.out.println();
		}
		for(int i=0;i<p1Board.gameBoard[0].length;i++) {
			for(int j=0;j<p1Board.gameBoard[0].length;j++) {
				System.out.print(p2Board.gameBoard[i][j]);
			}
			System.out.println();
		}
	}


}
