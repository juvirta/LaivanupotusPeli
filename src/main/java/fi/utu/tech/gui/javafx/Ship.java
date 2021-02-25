package fi.utu.tech.gui.javafx;

import java.util.ArrayList;


// class for ship-objects
// class provides methods to set and get ship coordinates
// each coordinate has boolean value hit to tell whether opponent has shot at this coordinate
public class Ship {
	
	// enum for ship type
	private Type type;
	// list of all ship location coordinates with boolean value to show if hit or not
	private ArrayList<Coordinates> coordinateList;
	// once all coordinates "are hit", sunken is true
	private boolean sunken = false;
	
	Ship(Type type) {
		this.type = type;
		coordinateList = new ArrayList<Coordinates>(shipSize(type));
	}
	
	// Setter for coordinates. x and y indicate ship bow coordinates. 
	// isTurned = false means ship is horizontal
	public void setCoordinates(int x, int y, boolean isTurned) {
		this.coordinateList = new ArrayList<Coordinates>(shipSize(type));
		if(!isTurned) {
			for(int i=0; i<shipSize(type);i++) {
				coordinateList.add(i,new Coordinates(x,y+i));
			}
		} else {
			for(int i=0; i<shipSize(type);i++) {
				coordinateList.add(i,new Coordinates(x+i,y));
			}
		}		
	}
	
	ArrayList<Coordinates> getCoordinateList(){
		return this.coordinateList;
	}
	
	boolean getSunken() {
		return this.sunken;
	}
	
	Type getType() {
		return this.type;
	}
	
	// Alternative setter. Not used currently.
	public boolean setCoordinates(ArrayList<Integer> xList, ArrayList<Integer> yList) {
		if (xList.size() == yList.size() && xList.size() == this.coordinateList.size()) {
			for (int i = 0; i < xList.size(); i++) {
				this.coordinateList.set(i, new Coordinates(xList.get(i), yList.get(i)));
			}
			return true;
		}
		return false;
	}
	
	// x,y are coordinates of shot
	// if hit, ship is updated (both coordinate and ship.sunken) and return value is true
	public boolean isShot(int x, int y) {
		for (Coordinates c : coordinateList) {
			if (c.x == x && c.y == y && !c.hit) {
				c.hit = true;
				checkStatus();
				return true;
			}
		}
		return false;
	}
	
	// checks if all coordinates of ship are hit
	public void checkStatus() {
		for (Coordinates c : coordinateList) {
			if (!c.hit) {
				return;
			}
		}
		sunken = true;
	}
	
	enum Type{
		LENTOTUKIALUS, TAISTELULAIVA, RISTEILIJA, SUKELLUSVENE, HAVITTAJA
	}
	
	// gives type by int
	static Type typeMap(int i) {
		switch(i) {
		case 0:
			return Type.LENTOTUKIALUS;
		case 1:
			return Type.TAISTELULAIVA;
		case 2:
			return Type.RISTEILIJA;
		case 3:
			return Type.SUKELLUSVENE;
		case 4:
			return Type.HAVITTAJA;
		default:
			return null;
		}
	}
	
	//gives int by type
	static int getIndexByType(Type t) {
		switch(t) {
		case LENTOTUKIALUS:
			return 0;
		case TAISTELULAIVA:
			return 1;
		case RISTEILIJA:
			return 2;
		case SUKELLUSVENE:
			return 3;
		case HAVITTAJA:
			return 4;
		default:
			return 5;
		}
	}
	
	//gives size by type
	static int shipSize(Type type) {
		switch(type) {
		case LENTOTUKIALUS:
			return 5;
		case TAISTELULAIVA:
			return 4;
		case HAVITTAJA:
			return 2;
		default:
			return 3;
		}
	}
	
	// class for ship coordinates. Shows if coordinate is hit.
	class Coordinates {
		protected int x;
		protected int y;
		protected boolean hit;
		public Coordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

}
