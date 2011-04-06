package test;

import org.junit.* ;
import static org.junit.Assert.* ;
import util.*;
import java.util.*;

public class CommandTest {
	
	@Test
	public void should_parse_command(){
		String commandStr = "command:join&options:p,v";
		HashMap<String, String> command = parse(commandStr);
		assertTrue(command.get("command").equals("join"));
		assertTrue(command.get("options").equals("p,v"));
	}
	
	@Test
	public void should_convert_to_string(){
		HashMap<String, String> command = new HashMap<String, String>();
		String commandStr = "command:join&options:p,v";
		HashMap<String, String> command = parse(commandStr);
		assertTrue(command.get("command").equals("join"));
		assertTrue(command.get("options").equals("p,v"));
	}
	
	// @Test
	// public void it_should_connect_to_server(){
	// 	
	// }
	
	
	// @Test
	// public void it_should_join_as_player(){
	// }
	// 
	// @Test
	// public void it_should_position_ships(){
	// }
}

	
