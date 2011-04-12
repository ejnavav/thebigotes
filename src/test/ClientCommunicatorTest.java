package test;

import org.junit.*;
import static org.junit.Assert.*;
import client.*;

public class ClientCommunicatorTest {
	FakeServer server;
	Communicator communicator;
	String host = "localhost";
	
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
//	
//	@Test
//	public void should_send_message() {
//		int port = 54323;
//		server = new FakeServer(port);
//		server.start();
//		server.waitUntilReady();
//		communicator = new Communicator(host, port);
//		communicator.connect();
//		communicator.sendMessage("The Bigotes");
//		assertTrue(server.waitForMessage().equals("The Bigotes"));
//		communicator.disconnect();
//		server.quit();
//	}
}