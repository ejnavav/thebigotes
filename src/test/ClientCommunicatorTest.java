package test;

import org.junit.*;
import static org.junit.Assert.*;
import client.*;


/**
 * Tests for the Communicator class
 * 
 * @author Victor Nava
 *
 */
public class ClientCommunicatorTest {
	FakeServer server;
	Communicator communicator;
	String host = "localhost";
	
	/**
	 * Tests if it can connect to the server
	 */
	@Test
	public void should_connect() {
		int port = 54321;
		server = new FakeServer(port);
		server.start();
		server.waitUntilReady();
		communicator = new Communicator(host, port);
		assertTrue(communicator.connect());
		server.quit();
	}

	/**
	 * Tests if it can receive messages from the server
	 */
	@Test
	public void should_receive_message() {
		int port = 54322;
		server = new FakeServer(port);
		server.start();
		server.waitUntilReady();
		communicator = new Communicator(host, port);
		communicator.connect();
		server.sendMessage("Hello World");
		assertTrue(communicator.waitForMessage().equals("Hello World"));
		server.quit();
	}
	
	/**
	 * Tests if it can send messages to the server
	 */
	@Test
	public void should_send_message() {
		int port = 54323;
		server = new FakeServer(port);
		server.start();
		server.waitUntilReady();
		communicator = new Communicator(host, port);
		communicator.connect();
		communicator.sendMessage("The Bigotes");
		assertTrue(server.waitForMessage().equals("The Bigotes"));
		communicator.disconnect();
		server.quit();
	}
}