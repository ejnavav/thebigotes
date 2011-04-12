package server;

import java.util.*;

public class Board {
	private class Cell {
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
			this.ship.addCell(cellString);
		}

		public String toString(boolean oponentView) {
			if (ship == null) {
				if (!isFired)
					return SPACE_CHAR;
				else
					return MISSED_CHAR;
			} else if (!isFired)
				return (oponentView == true ? SPACE_CHAR : ship.getLetter());
			else if (!ship.isDestroyed())
				return HIT_CHAR;
			else
				return ship.getLetter().toUpperCase();
		}

		public String getCellString() {
			return cellString;
		}

		public boolean isFired() {
			return isFired;
		}

		public void fire() {
			if (ship != null)
				ship.hit();
			isFired = true;
		}
	}

	private int size = 6;
	Cell[][] cells = new Cell[size][size];

	protected final String SPACE_CHAR = "#";
	protected final String MISSED_CHAR = "o";
	protected final String HIT_CHAR = "*";

	public Board() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}

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

	public boolean fire(String cell) throws Exception {

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

	public int[] getRowCol(String position) {
		int[] rowCol = new int[1];
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

	public void placeShip(String shipType, String orientation, String pos)
			throws Exception {

		// To Convert the row Letter to a integer Position in the array
		char c = pos.charAt(0);
		int charInt = c;
		int col = Integer.parseInt(pos.substring(1, 2).toLowerCase()) - 1;
		int row = (charInt - 97);

		Ship ship = new Ship(shipType, orientation, pos);
		int shipSize = ship.getSize();

		// if (!verifyBounds(shipSize, position, orientation)
		// || !verifyClash(shipSize, position, orientation)) {
		// throw new Exception("Ship Can't be placed there");
		// }

		// for (int i = position)
		// int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
		// for (int i = position; i < position + ((shipSize - 1) * size); i = i
		// + increment) {
		// cells[i] = ship.getLetter();
		// }

		try {
			// for (int i = 0; i < ship.getSize(); i++) {
			if (!verifyClash(shipSize, row, col, orientation))
				throw new Exception("Verifying Clash");
			for (int i = 0; i < ship.getSize(); i++) {
				cells[row][col].setShip(ship);
				if (orientation.equalsIgnoreCase("h")) {
					col++;
				} else
					row++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	private boolean verifyClash(int shipSize, int row, int col,
			String orientation) {
		// int increment = orientation.equalsIgnoreCase("h") ? 1 : this.size;
		// for (int i = position; i < position + ((shipSize - 1) * size); i = i
		// + increment) {
		// if (!cells[i].equals("SPACE_CHAR")) {
		// return false;
		// }
		// }
		// return true;
		try {
			if (!verifyBounds(shipSize, row, col, orientation))
				return false;
			for (int i = 0; i < shipSize; i++) {

				// if (verifyBounds(shipSize, row, col, orientation)) {
				if (cells[row][col].getShip() != null)
					return false;
				// }else return false;

				if (orientation.equals("h"))
					col++;
				else
					row++;
			}
			return true;
		} catch (Exception e) {
			return false;
		}

		// if (cells[row][col].getShip() == null)
		// return true;
		// return false;
	}

	private boolean verifyBounds(int shipSize, int row, int col,
			String orientation) {
		// Position out of bound
		// if (position < 0 || position >= size * size) {
		// return false;
		// }
		//
		// // Ship Wont fit in the board
		// if (orientation.equalsIgnoreCase("h")) {
		// if (position + shipSize > (Math.ceil((double)(position+1) /
		// this.size) * this.size)) {
		// return false;
		// } else
		// return true;
		// } else if (orientation.equalsIgnoreCase("v")) {
		// if (position + (this.size * (shipSize - 1)) >= size * size) {
		// return false;
		// } else
		// return true;
		// }

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

	// private boolean verifyClash(int shipSize, int row, int col, String
	// orientation) {
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
				// positions.add("h@"+(char)(96+row)+col);
				positions.add("h@" + cells[row][col].getCellString());
			}
			if (verifyClash(ship.getSize(), row, col, "v")) {
				// positions.add("v@"+(char)(96+row)+col);
				positions.add("v@" + cells[row][col].getCellString());
			}
			col++;
		}
		return positions;
	}
}