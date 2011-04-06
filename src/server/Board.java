package server;
import java.math.*;
public class Board {
	private int size = 6;
	String[] cells = new String[size*size];
	public Board(){	
		for (int i =0; i<cells.length ; i++){
			cells[i]="#";
		}
	}
	
	public void placeShip(Ship ship) throws Exception{
		verifyBounds(ship);
		verifyClash(ship);
	}
	private boolean verifyClash(Ship ship){
		int shipSize = ship.getSize();
		int position = ship.getPosition();
		String orientation = ship.getOrientation();
		int increment = orientation.equalsIgnoreCase("h")? 1:this.size;
		
		return false;
	}
	private boolean verifyBounds(Ship ship){
		int shipSize = ship.getSize();
		int position = ship.getPosition();
		String orientation = ship.getOrientation();
		
		//Position out of bound
		if (position<0||position>=shipSize*shipSize){
			return false;
		}
		
		//Ship Wont fit in the board
		if (orientation.equalsIgnoreCase("h")){
			if(position+shipSize>(Math.ceil(position/this.size)*this.size)){
				return false;
			}else return true;
		}else if (orientation.equalsIgnoreCase("v")){
			if(position+(this.size*(shipSize-1))>=size*size){
				return false;
			}else return true;
		}
		return false;
	}
	public void fire (int position){
		
	}
}