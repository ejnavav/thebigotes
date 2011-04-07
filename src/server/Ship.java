package server;
import java.util.* ;

public class Ship {
	// private static String types[] = { "battleship", "cruiser", "destroyer", "submarine" }
	private HashMap<String, Integer> types = new HashMap<String, Integer>();
	private int size;
	private String type;
	private String orientation;
	private int position;
	public Ship(String type, String orientation, int position) {
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
	
	public int getSize() { return size; }
	
	public String getOrientation() { return orientation; }
	
	public String getType() { return type; }
	
	public int getPosition() {return position;}
	
	public String getLetter(){
		return this.type.substring(0,1);
	}
	
}