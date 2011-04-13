package util;

import java.util.*;

public class Command {
	private HashMap<String, String> hm = null;

	public Command(){
		hm = new HashMap<String, String>();
	}

	public Command(String commandStr){
		this();
		String[] sections = commandStr.split("&");
		for(String keyVal : sections){
			String[] pair = keyVal.split(":");
			hm.put(pair[0], pair[1]);
		}
	}

	public String put(String key, String val){ return hm.put(key, val); }
	
    public String put(String key, ArrayList<String> val){
    	return hm.put(key, join(val, ","));
    }

	public String get(String key){ return hm.get(key); }

	public String type(){ return hm.get("command"); }
	
	public boolean hasKey(String key){ return hm.containsKey(key); }

	public String toString(){
		String commandStr = "";
		ArrayList<String> sections = new ArrayList<String>();
		for (String key : hm.keySet()) {
			sections.add(key+":"+hm.get(key));
		}
		return join(sections, "&");
	}

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