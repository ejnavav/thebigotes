package server;
import java.util.* ;

/**
 * Hold information about a Ship
 *
 */
public class Ship {
	private HashMap<String, Integer> types = new HashMap<String, Integer>();
	private int size;
	private String type;
	private String orientation;
	private String position;
	private boolean isDestroyed;
	private boolean isHit;
	private int hitParts=0;
	
	/**
	 * Ship Constructor
	 * @param type
	 * @param orientation
	 * @param position
	 */
	public Ship(String type, String orientation, String position) {
		types.put("battleship", new Integer(4));
		types.put("cruiser", new Integer(3));
		types.put("destroyer", new Integer(2));
		types.put("submarine", new Integer(1));
		
		if(types.containsKey(type)){
			this.type = type;
			this.size = types.get(type);
		}
		else { throw new RuntimeException("Invalid ship type"); }
		this.orientation = orientation;
		this.position = position;
	}
	
	/**
	 * @return The size of the Ship
	 */
	public int getSize() { return size; }
	
	/**
	 * @return Its Orientation
	 */
	public String getOrientation() { return orientation; }
	
	/**
	 * @return The type of Ship eg: "submarine"
	 */
	public String getType() { return type; }
	
	/**
	 * @return it's position
	 */
	public String getPosition() {return position;}
	
	/**
	 * @return The letter to be presented in the board
	 */
	public String getLetter(){
		return this.type.substring(0,1);
	}
	
	/**
	 * to be called When the ship is hit (Determined by the board) 
	 */
	public void hit(){
		isHit = true;
		if (!isDestroyed) hitParts++;
		if (hitParts==size) isDestroyed= true;
	}
	
	/**
	 * @return If the ship has been hit
	 */
	public boolean isHit(){
		return isHit;
	}
	
	/**
	 * @return True if ship is destroyed
	 */
	public boolean isDestroyed(){
		return isDestroyed;
	}
}