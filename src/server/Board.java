package server;

import java.util.*;

public class Board {
	private int size = 6;
	String[] cells = new String[size * size];
	ArrayList<Ship> ships = new ArrayList<Ship>();

	public Board() {
		for (int i = 0; i < cells.length; i++) {
			cells[i] = "#";
		}
	}

	public void placeShip(String shipType, String orientation, int position)
			throws Exception {
		Ship ship = new Ship(shipType, orientation, position);
		int shipSize = ship.getSize();

		if (!verifyBounds(shipSize, position, orientation)
				|| !verifyClash(shipSize, position, orientation)) {
			throw new Exception("Ship Can't be placed there");
		}
		ships.add(ship);
		int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
		for (int i = position; i < position + ((shipSize - 1) * size); i = i
				+ increment) {
			cells[i] = ship.getLetter();
		}
	}

	private boolean verifyClash(int shipSize, int position, String orientation) {
		int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
		for (int i = position; i < position + ((shipSize - 1) * size); i = i
				+ increment) {
			if (!cells[i].equals("#")) {
				return false;
			}
		}
		return true;
	}

	private boolean verifyBounds(int shipSize, int position, String orientation) {
		// Position out of bound
		if (position < 0 || position >= shipSize * shipSize) {
			return false;
		}

		// Ship Wont fit in the board
		if (orientation.equalsIgnoreCase("h")) {
			if (position + shipSize > (Math.ceil(position / this.size) * this.size)) {
				return false;
			} else
				return true;
		} else if (orientation.equalsIgnoreCase("v")) {
			if (position + (this.size * (shipSize - 1)) >= size * size) {
				return false;
			} else
				return true;
		}
		return false;
	}

	public void fire(int position) throws Exception {
		
	}

	public String getBoardString() {
		String board = "";
		for (String cell : cells) {
			board = cell;
		}
		return board;
	}

	public ArrayList<String> getPositionOptions(Ship ship) {
		ArrayList<String> positions= new ArrayList<String>();
		int column = 1; 
		for (int i =0; i<size*size;i++){
			if (i%size==0) column = 1;
			if (verifyClash(ship.getSize(), i, "h") && verifyBounds(ship.getSize(), i, "h")){
				positions.add("h@"+(char)(96+i));
			}
			if (verifyClash(ship.getSize(), i, "v") && verifyBounds(ship.getSize(), i, "v")){
				positions.add("v@"+(char)(96+i));
			}
			column++;
		}
		return positions;
	}
}