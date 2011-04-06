package test;

import org.junit.* ;
import static org.junit.Assert.* ;
import client.*;
import java.util.*;

public class BattleShipsClientTest {
	
	@Test
	public void should_parse_command(){
		BattleShipsClient client = new BattleShipsClient("", 0);
		String commandStr = "command:join&options:p,v";
		HashMap<String, String> command = client.parseCommand(commandStr);
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

	
