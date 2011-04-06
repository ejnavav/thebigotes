package util;

import java.util.*;

public class Command {
	public static HashMap<String, String> parse(String commandStr){
		HashMap<String, String> command = new HashMap<String, String>();
		String[] sections = commandStr.split("&");

		for(String keyVal : sections){
			String[] pair = keyVal.split(":");
			command.put(pair[0], pair[1]);
		}
		return command;
	}

	public static String toString(HashMap<String, String> command){
		String commandStr = "";
		ArrayList<String> sections = new ArrayList<String>();

		for (String key : command.keySet()) {
			sections.add(key+":"+command.get(key));
		}
		return join(sections, "&");
	}

	private static String join(Collection<?> s, String delimiter) {
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