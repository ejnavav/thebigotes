package server;

import java.util.*;

public class Board {
	private class Cell {
		//Contains All the information about a cell in the matrix
		//If its hit, if is there a ship, etc.
		String cellString;
		Ship ship;
		boolean isFired;
		
		public Cell(int row, int col) {
			int charInt = 97 + row;
			char rowChar = (char) charInt;
			this.cellString = String.valueOf(rowChar) + (col + 1);
		}

		public Ship getShip() {
			return ship;
		}

		public void setShip(Ship ship) {
			this.ship = ship;
		}

		//Returns the cells characters to be presented to the client
		//if oponent view is set to true returns the way the oponent would view it
		public String toString(boolean oponentView) {
			if (ship == null) {
				if (!isFired)			//if hasn't been fired yet
					return SPACE_CHAR;
				else
					return MISSED_CHAR;	//if is fired but no ship on it (miss)
			} else if (!isFired)		//if there's a Ship
				return (oponentView == true ? SPACE_CHAR : ship.getLetter()); //if hasnt been fired
			else if (!ship.isDestroyed())//if is hit
				return HIT_CHAR;
			else
				return ship.getLetter().toUpperCase();//if the ship's destroyed
		}

		//Returns the cell in a string. eg: a3
		public String getCellString() {
			return cellString;
		}

		public boolean isFired() {
			return isFired;
		}

		//Method to call when firing a cell
		public void fire() {
			if (ship != null)
				ship.hit();
			isFired = true;
		}
	}
	
	private ArrayList<Ship> ships = new ArrayList<Ship>(); //Contains all the ships the board has
														   //to make it easy to know when game is over
	private int size = 6; //Size of the matrix (6x6)
	Cell[][] cells = new Cell[size][size];

	protected final String SPACE_CHAR = "#";
	protected final String MISSED_CHAR = "o";
	protected final String HIT_CHAR = "*";

	//Constructor to be called when a new board is created
	public Board() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	//Constructor to be called by the client to generate a board from 
	//a given String (Board String)
	public Board(String boardString) {
		this();
		HashMap<String, Ship> shipTypes = new HashMap<String, Ship>();
		Ship submarine = new Ship("submarine", "h", "a1");
		Ship destroyer = new Ship("destroyer", "h", "a1");
		Ship cruiser = new Ship("cruiser", "h", "a1");
		Ship battleship = new Ship("battleship", "h", "a1");
		shipTypes.put(submarine.getLetter(), submarine);
		shipTypes.put(destroyer.getLetter(), destroyer);
		shipTypes.put(cruiser.getLetter(), cruiser);
		shipTypes.put(battleship.getLetter(), battleship);

		int row = 0;
		int col = 0;

		for (int i = 0; i < boardString.length(); i++) {
			if (i > 0 && i % size == 0) {
				row++;
				col = 0;
			}
			String boardChar = boardString.substring(i, i++);
			if (!boardChar.equals(SPACE_CHAR)) {
				if (boardChar.equals(MISSED_CHAR) || boardChar.equals(HIT_CHAR)) {
					cells[row][col].fire();
				} else {
					cells[row][col].setShip(shipTypes.get(boardChar));
				}
			}
			col++;
		}
	}

	//To Fire a Cell,
	//Receives the cell as a string eg:a3
	public boolean fire(String cell) throws Exception {
		cell = cell.toLowerCase();
		int row = getRowCol(cell)[0];
		int col = getRowCol(cell)[1];
		if (cells[row][col].isFired()) {
			throw new Exception("Cell has been fired already");
		} else {
			cells[row][col].fire();
		}
		if (cells[row][col].ship != null) {
			return true;
		} else
			return false;
	}

	//Returns an array with the row in the index 0 and the col in index 1
	//from a given cell String. eg: b4
	public int[] getRowCol(String position) {
		int[] rowCol = new int[2];
		char c = position.charAt(0);
		int charInt = c;
		int col = Integer.parseInt(position.substring(1, 2).toLowerCase()) - 1;
		int row = (charInt - 97);
		rowCol[0] = row;
		rowCol[1] = col;
		return rowCol;
	}

	public Ship getShip(String position) {
		int row = getRowCol(position)[0];
		int col = getRowCol(position)[1];
		return cells[row][col].getShip();
	}
	
	//Generates the board to be sent to the client
	private String getBoardString(boolean oponentView){
		String board = "";
		for (Cell[] cellRow : cells) {
			for (Cell cell : cellRow) {
				board += cell.toString(oponentView);
			}
		}
		return board;
	}
	
	public String ownView() {		
		return getBoardString(false);
	}
	
	public String oponentView() {
		return getBoardString(true);
	}

	//To Place the ships in the board
	public void placeShip(String shipType, String orientation, String pos)
			throws Exception {

		// To Convert the row Letter to the integer Positions in the array
		int row = getRowCol(pos)[0];
		int col = getRowCol(pos)[1];
		Ship ship = new Ship(shipType, orientation, pos);
		int shipSize = ship.getSize();

		try {
			if (!verifyClash(shipSize, row, col, orientation))
				throw new Exception("Verifying Clash");
			for (int i = 0; i < ship.getSize(); i++) {
				cells[row][col].setShip(ship);
				if (orientation.equalsIgnoreCase("h")) {
					col++;
				} else
					row++;
			}
			ships.add(ship);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	public boolean hasShipsAlive(){
		for (int i =0;i<ships.size();i++){
			if (!ships.get(i).isDestroyed()) return true;
		}
		return false;
	}

	private boolean verifyClash(int shipSize, int row, int col,
			String orientation) {
		try {
			if (!verifyBounds(shipSize, row, col, orientation))
				return false;
			for (int i = 0; i < shipSize; i++) {

				if (cells[row][col].getShip() != null)
					return false;

				if (orientation.equals("h"))
					col++;
				else
					row++;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean verifyBounds(int shipSize, int row, int col,
			String orientation) {
		try {
			for (int i = 0; i < shipSize; i++) {
				cells[row][col].toString();
				if (orientation.equalsIgnoreCase("h")) {
					col++;
				} else
					row++;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	//Returns the possible positions to place a given ship
	public ArrayList<String> getPositionOptions(Ship ship) {
		ArrayList<String> positions = new ArrayList<String>();
		int row = 0;
		int col = 0;
		for (int i = 0; i < (size * size); i++) {
			if (i > 0 && i % size == 0) {
				row++;
				col = 0;
			}
			if (verifyClash(ship.getSize(), row, col, "h")) {
				positions.add("h@" + cells[row][col].getCellString());
			}
			if (verifyClash(ship.getSize(), row, col, "v")) {
				positions.add("v@" + cells[row][col].getCellString());
			}
			col++;
		}
		return positions;
	}
}