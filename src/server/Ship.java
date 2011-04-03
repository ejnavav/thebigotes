package server;
import java.util.* ;

public abstract class Ship {
	// private static String types[] = { "battleship", "cruizer", "destroyer", "submarine" }
	private HashMap<String, Integer> types = new HashMap<String, Integer>();
	private int size;
	private String type;
	private String orientation;
	
	public Ship(String type) {
		types.put("battleship", new Integer(4));
		types.put("cruizer", new Integer(3));
		types.put("destroyer", new Integer(2));
		types.put("submarine", new Integer(1));
		
		if(types.containsKey(type)){
			this.type = type;
			this.size = types.get(type);
		}
		else { throw new RuntimeException("Invalid ship type"); }
	}
	
	public int getSize() { return size; }
	
	public String getOrientation() { return orientation; }
	
	public String getType() { return type; }
}