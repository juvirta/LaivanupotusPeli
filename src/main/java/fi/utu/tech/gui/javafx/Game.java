package fi.utu.tech.gui.javafx;


import java.io.IOException;
import java.util.Optional;

import fi.utu.tech.gui.javafx.Ship.Coordinates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

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
	//Pelilaudat GridPaneina
	private GridPane[] lauta1;
	private GridPane[] lauta2;
	
	public Game(String p1, String p2, Board p1Board, Board p2Board, Stage stage) {
		this.p1=p1;
		this.p2=p2;
		this.p1Board=p1Board;
		this.p2Board=p2Board;
		this.stage=stage;
		this.p1Turn=true;
		this.lauta1=luoPelilauta1();
		this.lauta2=luoPelilauta2();
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
			middleLabel1.setText("Pelaajan " + p1 +": vuoro alkaa.");
			middleLabel2.setText(p2 +": Siirry pois koneelta.");
		} else {
			middleLabel1.setText("Pelaajan " + p2 +": vuoro alkaa.");
			middleLabel2.setText(p1 +": Siirry pois koneelta.");
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
			peliLogiikka();
		});
		pane.setBottom(okButton);
		BorderPane.setAlignment(okButton, Pos.CENTER);
		BorderPane.setMargin(okButton, new Insets(30));
		stage.setScene(new Scene(pane, 450, 450));
		stage.show();
	}
	
	private GridPane[] luoPelilauta1() {
		//Luodaan pelaajan 1 pääpelilauta
		GridPane gridPane1 = new GridPane();
		GridPane gridPane2 = new GridPane();
		for(int i=0; i<p1Board.gameBoard[0].length; i++){
			for(int j=0; j<p1Board.gameBoard[0].length; j++){
				Rectangle r = new Rectangle();
				r.setWidth(300 / p1Board.gameBoard[0].length);
				r.setHeight(300 / p1Board.gameBoard[0].length);
				r.setFill(Color.BLUE);
				int valueX=i;
				int valueY=j;
				GridPane.setRowIndex(r, i);
		        GridPane.setColumnIndex(r, j);
		        //Hiirellä klikataan neliötä
				r.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//Tarkistetaan osuuko
						if (p2Board.gameBoard[valueX][valueY]==1) {
							//Löydetään osumakohdan laiva
							for (Ship s : p2Board.shipList) {
								if (s.isShot(valueX, valueY)) {
									r.setFill(Color.RED);
									//Värjätään myös toisen pelaajan näkymästä ruutu
									for (Node child : gridPane2.getChildren()) {
										if (GridPane.getRowIndex(child) == valueX && GridPane.getColumnIndex(child)==valueY) {
											((Rectangle) child).setFill(Color.RED);
											break;
										}
									}
									//jos laiva uppoaa, molemmilta pelaajilta värjätään laivan ruudut mustaksi
									if (s.getSunken()) {
										for (Coordinates c : s.getCoordinateList()) {
											for (Node child : gridPane1.getChildren()) {
												if (c.x == GridPane.getRowIndex(child) && c.y == GridPane.getColumnIndex(child)) {
													((Rectangle) child).setFill(Color.BLACK);
													break;
												}
											}
											for (Node child : gridPane2.getChildren()) {
												if (c.x == GridPane.getRowIndex(child) && c.y == GridPane.getColumnIndex(child)) {
													((Rectangle) child).setFill(Color.BLACK);
													break;
												}
											}
										}
									}
								}
							}
							//Tarkistetaan voittaako pelaaja
							if (p2Board.shipList.stream().allMatch(s -> s.getSunken()==true)) {
								voitaPeli(p1);
							}
						}
						else {
							p1Turn=false;
							r.setFill(Color.YELLOW);
							showChangeScreenAndChangeTurn();
						}
					}
				});
				gridPane1.getChildren().addAll(r);
				
				//Tässä luodaan toisen pelaajan näkymä, jossa on omat laivat ja ei interaktiota hiiren kanssa
				Rectangle r2 = new Rectangle();
				r2.setWidth(300 / p1Board.gameBoard[0].length);
				r2.setHeight(300 / p1Board.gameBoard[0].length);
				if (p2Board.gameBoard[i][j]==1) {
					r2.setFill(Color.GREEN);
				}
				else {
					r2.setFill(Color.BLUE);
				}
				GridPane.setRowIndex(r2, i);
		        GridPane.setColumnIndex(r2, j);
		        
		        gridPane2.getChildren().addAll(r2);
			}
		}
		gridPane1.setGridLinesVisible(true);
		gridPane2.setGridLinesVisible(true);
		
		GridPane[] result = new GridPane[2];
		result[0]=gridPane1;
		result[1]=gridPane2;
		return result;
	}
	
	private GridPane[] luoPelilauta2() {
		//Luodaan pelaajan 2 pääpelilauta
		//Tämä metodi on käytännössä identtinen edellisen luoPelilauta1() metodin kanssa, mutta luodaan toisen pelaajan pelilaudat.
		GridPane gridPane1 = new GridPane();
		GridPane gridPane2 = new GridPane();
		for(int i=0; i<p2Board.gameBoard[0].length; i++){
			for(int j=0; j<p2Board.gameBoard[0].length; j++){
				Rectangle r = new Rectangle();
				r.setWidth(300 / p2Board.gameBoard[0].length);
				r.setHeight(300 / p2Board.gameBoard[0].length);
				r.setFill(Color.BLUE);
				int valueX=i;
				int valueY=j;
				GridPane.setRowIndex(r, i);
		        GridPane.setColumnIndex(r, j);
				r.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (p1Board.gameBoard[valueX][valueY]==1) {
							for (Ship s : p1Board.shipList) {
								if (s.isShot(valueX, valueY)) {
									r.setFill(Color.RED);
									for (Node child : gridPane2.getChildren()) {
										if (GridPane.getRowIndex(child) == valueX && GridPane.getColumnIndex(child)==valueY) {
											((Rectangle) child).setFill(Color.RED);
											break;
										}
									}
									if (s.getSunken()) {
										for (Coordinates c : s.getCoordinateList()) {
											for (Node child : gridPane1.getChildren()) {
												if (c.x == GridPane.getRowIndex(child) && c.y == GridPane.getColumnIndex(child)) {
													((Rectangle) child).setFill(Color.BLACK);
													break;
												}
											}
											for (Node child : gridPane2.getChildren()) {
												if (c.x == GridPane.getRowIndex(child) && c.y == GridPane.getColumnIndex(child)) {
													((Rectangle) child).setFill(Color.BLACK);
													break;
												}
											}
										}
									}
								}
							}
								if (p1Board.shipList.stream().allMatch(s -> s.getSunken()==true)) {
									voitaPeli(p2);
								}
						}
						else {
							p1Turn=true;
							r.setFill(Color.YELLOW);
							showChangeScreenAndChangeTurn();
						}
					}
				});
				gridPane1.getChildren().addAll(r);
				
				Rectangle r2 = new Rectangle();
				r2.setWidth(300 / p2Board.gameBoard[0].length);
				r2.setHeight(300 / p2Board.gameBoard[0].length);
				if (p1Board.gameBoard[i][j]==1) {
					r2.setFill(Color.GREEN);
				}
				else {
					r2.setFill(Color.BLUE);
				}
				GridPane.setRowIndex(r2, i);
		        GridPane.setColumnIndex(r2, j);
		        
		        gridPane2.getChildren().addAll(r2);
			}
		}
		gridPane1.setGridLinesVisible(true);
		gridPane2.setGridLinesVisible(true);
		
		GridPane[] result = new GridPane[2];
		result[0]=gridPane1;
		result[1]=gridPane2;
		return result;
	}

	private void peliLogiikka() {
		//Näkymä itse pelin pelaamisen aikana
		BorderPane borderPane = new BorderPane();
		if (p1Turn) {
			borderPane.setTop(lauta1[0]);
			borderPane.setBottom(lauta2[1]);
			lauta1[0].setAlignment(Pos.CENTER);
			lauta2[1].setAlignment(Pos.CENTER);
			BorderPane.setMargin(lauta1[0], new Insets(20,0,50,0));
			BorderPane.setMargin(lauta2[1], new Insets(50,0,20,0));
		}
		else {
			borderPane.setTop(lauta2[0]);
			borderPane.setBottom(lauta1[1]);
			lauta2[0].setAlignment(Pos.CENTER);
			lauta1[1].setAlignment(Pos.CENTER);
			BorderPane.setMargin(lauta2[0], new Insets(20,0,50,0));
			BorderPane.setMargin(lauta1[1], new Insets(50,0,20,0));
		}
		Label ohjeTeksti = new Label();
		Label ohjeTeksti2 = new Label();
		ohjeTeksti.setText("Klikkaa ylläolevan pelilaudan ruutua ampuaksesi. Vuoro vaihtuu, jos et osu vastapelaajan alukseen.");
		ohjeTeksti2.setText("Alla on oma pelilautasi. Punainen väri tarkoittaa osumaa, musta uponnutta, keltainen ohimennyt laukaus ja vihreä on oma laiva");
		ohjeTeksti.setWrapText(true);
		ohjeTeksti2.setWrapText(true);
		Button exitButton = new Button("  Lopeta peli  ");
		exitButton.setOnAction((e)->{
			lopetaPeli(e);
		});
		VBox vBox = new VBox();
		vBox.getChildren().add(ohjeTeksti);
		vBox.getChildren().add(ohjeTeksti2);
		borderPane.setCenter(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(borderPane);
		mainPane.setBottom(exitButton);
		BorderPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(exitButton, new Insets(0,5,5,0));
		stage.setScene(new Scene(mainPane, 700, 820));
		stage.show();
	}
	
	private void voitaPeli(String pelaaja) {
		//Ilmoitetaan voittaja ja luodaan loppuruutu
		BorderPane pane = new BorderPane();
		Label title = new Label("Peli on päättynyt");
		title.setFont(new Font("System",18));
		BorderPane.setMargin(title, new Insets(10));
		BorderPane.setAlignment(title, Pos.CENTER);
		pane.setTop(title);
		Label middleLabel1 = new Label();
		Label middleLabel2 = new Label();
		middleLabel1.setText("Pelaaja " + pelaaja +" voitti pelin");
		middleLabel2.setText("Alla oleva nappi aloittaa uuden pelin");
		middleLabel1.setWrapText(true);
		middleLabel2.setWrapText(true);
		VBox vBox = new VBox();
		vBox.getChildren().add(middleLabel1);
		vBox.getChildren().add(middleLabel2);
		pane.setCenter(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		Button okButton = new Button("  Aloita uusi peli  ");
		Button exitButton = new Button("  Lopeta peli  ");
		okButton.setOnAction((e)->{
			exitEvent(e);
		});
		exitButton.setOnAction((e)->{
			System.exit(0);
		});
		HBox hBox = new HBox(5);
		hBox.getChildren().add(okButton);
		hBox.getChildren().add(exitButton);
		hBox.setAlignment(Pos.CENTER);
		BorderPane.setMargin(hBox, new Insets(30));
		pane.setBottom(hBox);
		stage.setScene(new Scene(pane, 450, 450));
		stage.show();
	}
	
	void exitEvent(ActionEvent event) {
		//Pelin päätyttyä luodaan uusi peli, ei varmistusta
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
	
	void lopetaPeli(ActionEvent event) {
		//Aloitetaan peli alusta ja annetaan varoitus
		Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Pelin tila nollataan");
		exitAlert.setTitle("Varoitus");
		exitAlert.setHeaderText("Haluatko aloittaa pelin kokonaan alusta?");
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
