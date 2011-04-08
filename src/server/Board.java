package server;

import java.util.*;

public class Board {
	private class Cell{
		String cellString;
		Ship ship;
		boolean isFired;
		
		public Cell(int row, int col){
			int charInt = 97+row;
			char rowChar = (char)charInt;
			this.cellString  = String.valueOf(rowChar)+col;
		}
		
		public Ship getShip(){
			return ship;
		}
		
		public void setShip(Ship ship){
			this.ship = ship;
			this.ship.addCell(cellString);
		}
		
		public String toString(){
			if (ship==null) return SPACE_CHAR;
			return ship.getLetter();
		}
		
		public String getCellString(){
			return cellString;
		}	
			
		
		public boolean isFired(){
			return isFired;
		}
	}
	private int size = 6;
	Cell[][] cells = new Cell[size * size][size * size];
	ArrayList<Ship> ships = new ArrayList<Ship>();
	protected final String SPACE_CHAR = "SPACE_CHAR";
	
	public Board() {
		for (int i = 0; i < cells.length; i++) {
			for (int j =0 ;j<cells.length;j++){
				cells[i][j] = new Cell(i,j);
			}
		}
	}

	public void placeShip(String shipType, String orientation, String pos)
			throws Exception {
		
		//To Convert the row Letter to a integer Position in the array
		char c = pos.charAt(0);
		int charInt = c;
		int col = Integer.parseInt(pos.substring(1,1).toLowerCase());
		int row = (charInt-97)*size;
				
		Ship ship = new Ship(shipType, orientation, pos);
		int shipSize = ship.getSize();

//		if (!verifyBounds(shipSize, position, orientation)
//				|| !verifyClash(shipSize, position, orientation)) {
//			throw new Exception("Ship Can't be placed there");
//		}
		
		
		ships.add(ship);
//		for (int i = position)
//		int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
//		for (int i = position; i < position + ((shipSize - 1) * size); i = i
//				+ increment) {
//			cells[i] = ship.getLetter();
//		}
		
		try{
			for (int i=0;i<ship.getSize();i++){
				if(!verifyClash(shipSize,row,col,orientation)) throw new Exception();
				cells[row][col].setShip(ship);
				if (orientation.equalsIgnoreCase("h")){
					col++;
				} else row++;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private boolean verifyClash(int shipSize, int row, int col, String orientation) {
//		int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
//		for (int i = position; i < position + ((shipSize - 1) * size); i = i
//				+ increment) {
//			if (!cells[i].equals("SPACE_CHAR")) {
//				return false;
//			}
//		}
//		return true;
		if (cells[row][col].getShip()==null) return true;
		return false;
	}

	private boolean verifyBounds(int shipSize, int row, int col, String orientation) {
		// Position out of bound
//		if (position < 0 || position >= size * size) {
//			return false;
//		}
//
//		// Ship Wont fit in the board
//		if (orientation.equalsIgnoreCase("h")) {
//			if (position + shipSize > (Math.ceil((double)(position+1) / this.size) * this.size)) {
//				return false;
//			} else
//				return true;
//		} else if (orientation.equalsIgnoreCase("v")) {
//			if (position + (this.size * (shipSize - 1)) >= size * size) {
//				return false;
//			} else
//				return true;
//		}
		
		try {
			for (int i=0;i<shipSize;i++){
				cells[row][col].toString();
				if (orientation.equalsIgnoreCase("h")){
					col++;
				} else row++;
			}	
		}catch(Exception e){
			return false;
		}
		return false;
	}

	public void fire(int position) throws Exception {
		
	}

	public String getBoardString() {
		String board = "";
		for (Cell[] cellRow : cells) {
			for (Cell cell:cellRow){
				board += cell.toString();
			}
		}
		return board;
	}
//private boolean verifyClash(int shipSize, int row, int col, String orientation) {
	public ArrayList<String> getPositionOptions(Ship ship) {
		ArrayList<String> positions= new ArrayList<String>();
		int row = 1; 
		int col = 1;
		for (int i =0; i<(size*size);i++){
			if (i>0 && i%size==0){
				row+= 1;
				col=1;
			}
			if (verifyClash(ship.getSize(), row,col, "h") && verifyBounds(ship.getSize(),row,col, "h")){
//				positions.add("h@"+(char)(96+row)+col);
				positions.add("h@"+cells[row][col].getCellString());
			}
			if (verifyClash(ship.getSize(), row,col, "v") && verifyBounds(ship.getSize(), row,col, "v")){
//				positions.add("v@"+(char)(96+row)+col);
				positions.add("v@"+cells[row][col].getCellString());
			}
			col++;
		}
		return positions;
	}
}