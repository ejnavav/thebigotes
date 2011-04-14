package test;
import org.junit.* ;
import static org.junit.Assert.* ;
import util.*;

/**
 * Tests for the Command class
 * 
 * @author vic
 */
public class CommandTest {	
	
	/**
	 * Tests it can convert a command string to a command object
	 */
	@Test
	public void should_parse_command(){
		String commandStr = "command:join&options:p,v";
		Command command = new Command(commandStr);
		assertTrue(command.get("command").equals("join"));
		assertTrue(command.get("options").equals("p,v"));
	}
	
	/**
	 * Test it can covert the object to string its string representation 
	 */
	@Test
	public void should_convert_to_string(){
		String commandStr = "command:join&options:p,v";
		Command command = new Command();
		command.put("command", "join");
		command.put("options", "p,v");
		assertTrue(command.toString().equals(commandStr));
	}
}