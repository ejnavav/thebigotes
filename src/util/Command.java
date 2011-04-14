package util;

import java.util.*;


/**
 * Class for handling the parsing and generation
 * of the command strings sent by the server and
 * the client
 * 
 * A command string consist of key/value pairs strings
 * the key/value separator is a colon ":"
 * and a keyset separator is a "&" eg:
 * 
 * "command:fire&location:a4"
 * 
 * Is basically a wrapper of a HashMap with only some
 * methods exposed and some extra functionality added
 * 
 * @author John Kolovos
 * 
 */
public class Command {
	private HashMap<String, String> hm = null;

	/**
	 * Empty constructor
	 */
	public Command(){
		hm = new HashMap<String, String>();
	}

	
	/**
	 * Return a command object given a command string
	 * 
	 * @param commandStr
	 */
	public Command(String commandStr){
		this();
		String[] sections = commandStr.split("&");
		for(String keyVal : sections){
			String[] pair = keyVal.split(":");
			hm.put(pair[0], pair[1]);
		}
	}
	
	/**
	 * Adds a key/val
	 * 
	 * @param key String
	 * @param val String
	 * @return String
	 */
	public String put(String key, String val){ return hm.put(key, val); }
	
	/**
	 * Adds a key/val
	 * 
	 * @param key String
	 * @param val an ArrayList of strings
	 * @return String
	 */
    public String put(String key, ArrayList<String> val){
    	return hm.put(key, join(val, ","));
    }

    /**
     * Returns the value of the given key
     * 
     * @param key
     * @return String, value of key
     */
	public String get(String key){ return hm.get(key); }

	
	/**
	 * Returns the type of command
	 * @return String command type
	 */
	public String type(){ return hm.get("command"); }
	
	/**
	 * Check if the command has a given key
	 * @param key
	 * @return true if it contains the key, false otherwise
	 */
	public boolean hasKey(String key){ return hm.containsKey(key); }

	/**
	 * Converts the command object to it's string representation
	 */
	public String toString(){
		ArrayList<String> sections = new ArrayList<String>();
		for (String key : hm.keySet()) {
			sections.add(key+":"+hm.get(key));
		}
		return join(sections, "&");
	}

	/**
	 * Joins a collection as a string separated by a delimiter char
	 * @param s
	 * @param delimiter
	 * @return the joined collection as string
	 */
	private String join(Collection<?> s, String delimiter) {
		StringBuilder builder = new StringBuilder();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
			if (!iter.hasNext()) {
				break;                  
			}
			builder.append(delimiter);
		}
		return builder.toString();
	}
}