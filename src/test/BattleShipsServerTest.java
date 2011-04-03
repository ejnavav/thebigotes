package test;

import org.junit.*;
import static org.junit.Assert.*;
import server.*;

public class BattleShipsServerTest {

	BattleShipsServer server = null;
	FakeClient client1;
	FakeClient client2;

	@Before
	public void init() {
		System.out.println("before");
		server = new BattleShipsServer(54321);
		server.start();
	}

	@After
	public void teardown() {
		System.out.println("After");
		server.stop();
	}

	@Test
	public void accepts_one_client_connection() {
		FakeClient client1 = new FakeClient("localhost", 54321);
		assertTrue(client1.connect());
		client1.disconnect();
	}

	@Test
	public void accepts_multiple_client_connection() {
		client1 = new FakeClient("localhost", 54321);
		client2 = new FakeClient("localhost", 54321);
		assertTrue(client1.connect());
		assertTrue(client2.connect());
		client1.disconnect();
		client2.disconnect();
	}

	// @Test(timeout=1000)
	// public void replies_to_ping(){
	// client1 = new FakeClient("localhost", 54321);
	// client2 = new FakeClient("localhost", 54321);
	// client1.connect();
	// client2.connect();
	// assertTrue(client1.ping());
	// assertTrue(client2.ping());
	// client1.disconnect();
	// client2.connect();
	// }

	@Test
	public void one_player_can_join() {
		client1 = new FakeClient("localhost", 54321);
		client1.connect();
		try {
			Thread.sleep(200);
		} catch (Exception e) {
		}
		String command = client1.getLastMessage();
		assertNotNull("Expecting to receive a command", command);
		assertTrue("Expecting: 'command:join' but received: " + command,
				command.matches("command:join"));
	}

	@Test
	public void two_player_can_join() {
		client1 = new FakeClient("localhost", 54321);
		client2 = new FakeClient("localhost", 54321);
		client1.connect();
		client2.connect();
		try {
			Thread.sleep(200);
		} catch (Exception e) {
		}
		String command = client1.getLastMessage();
		assertNotNull("Expecting to receive a command", command);
		assertTrue("Expecting: 'command:join' but received: " + command,
				command.matches("command:join"));
		command = client2.getLastMessage();
		assertNotNull("Expecting to receive a command", command);
		assertTrue("Expecting: 'command:join' but received: " + command,
				command.matches("command:join"));
	}
}