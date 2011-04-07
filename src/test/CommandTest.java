package test;

import org.junit.* ;
import static org.junit.Assert.* ;
import util.*;
import java.util.*;

public class CommandTest {	
	@Test
	public void should_parse_command(){
		String commandStr = "command:join&options:p,v";
		HashMap<String, String> command = Command.parse(commandStr);
		assertTrue(command.get("command").equals("join"));
		assertTrue(command.get("options").equals("p,v"));
	}
	
	@Test
	public void should_convert_to_string(){
		String commandStr = "command:join&options:p,v";
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "join");
		command.put("options", "p,v");
		assertTrue(Command.toString(command).equals(commandStr));
	}
}
	
