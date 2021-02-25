package fi.utu.tech.gui.javafx;

import java.util.ArrayList;

import fi.utu.tech.gui.javafx.Ship.Coordinates;

//this class stores a list of player's ships
//it also has int[][] matrix to reflect the game area. 1 marks ship part, 0 marks free space.

public class Board implements Cloneable{
	
	protected ArrayList<Ship> shipList;
	protected int[][] gameBoard;
	
	//constructor takes board size as parameter
	public Board(int ruudukko) {
		shipList = new ArrayList<Ship>();
		this.gameBoard = new int[ruudukko][ruudukko];
			for(int i=0;i<ruudukko;i++) {
				for(int j=0; j<ruudukko;j++) {
					gameBoard[i][j]=0;
				}
			}
	}
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	// method to set Ship on a board and update gameBoard
	public void setShip(Ship ship) {
		shipList.add(ship);
		ArrayList<Coordinates> shipCoordinates = ship.getCoordinateList();
		for (Coordinates coordinates : shipCoordinates) {
			gameBoard[coordinates.x][coordinates.y]=1;
		}
	}
	
	// method to check if ship can be placed on a board
	public boolean checkNewShipFit(Ship ship) {
		ArrayList<Coordinates> shipCoordinates = ship.getCoordinateList();
		for (Coordinates coordinates : shipCoordinates) {
			if(gameBoard[coordinates.x][coordinates.y]==1) {
				return false;
		}
	}
		return true;
	}
	

}
