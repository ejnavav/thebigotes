package test;

import org.junit.*;
import static org.junit.Assert.*;
import server.*;
import client.*;
import util.*;

/**
 * 
 * Tests for the BattleShipServer class
 * All the tests use real socket communication
 * between the server and the client
 * 
 * @author Victor Nava
 *
 */
public class BattleShipsServerTest {

    BattleShipsServer server = null;
    String host = "localhost";

    /**
     * Test if two clients can connect
     */
    @Test
    public void accepts_multiple_client_connection() {
    	System.out.println("accepts_multiple_client_connection");
    	int port = 54321;
    	BattleShipsServer server = new BattleShipsServer(port);
    	server.start();
    	try{Thread.sleep(100);}catch (Exception e) {}; //wait a bit
        BattleShipsClient client1 = new BattleShipsClient(host, port);
        BattleShipsClient client2 = new BattleShipsClient(host, port);
        assertTrue(client1.connect());
        assertTrue(client2.connect());
    }
    
    /**
     * Test if two players can join
     */
    @Test
    public void two_player_can_join() {
        System.out.println("two_player_can_join()");
        int port = 54322;
    	BattleShipsServer server = new BattleShipsServer(port);
    	server.start();
    	try{Thread.sleep(100);}catch (Exception e) {}; //wait a bit
        BattleShipsClient client1 = new BattleShipsClient(host, port);
        BattleShipsClient client2 = new BattleShipsClient(host, port);
        try{
            client1.connect();
            client2.connect();
            
            Command command = client1.waitForCommand();
            assertTrue(command.type().equals("join"));
            client1.sendMessage("command:join&as:p");
            command = client1.waitForCommand();
            assertTrue(command.type().equals("position"));
            
            command = client2.waitForCommand();
            assertTrue(command.type().equals("join"));
            client2.sendMessage("command:join&as:p");
            command = client2.waitForCommand();
            assertTrue(command.type().equals("position"));

        } catch (Exception e) {
            fail("should not throw any exception but threw: "+e.getMessage());
        }
    }

    /**
     * Test if a player can position all the ships
     */
    @Test
    public void should_position_ships() {
    	int port = 54323;
    	BattleShipsServer server = new BattleShipsServer(port);
    	server.start();
    	try{Thread.sleep(100);}catch (Exception e) {}; //wait a bit
    	
        System.out.println("should_position_ships");
        BattleShipsClient client =  new BattleShipsClient(host, port);
        assertTrue(client.connect());

        Command command = client.waitForCommand();
        assertTrue(command.type().equals("join"));
        client.sendMessage("command:join&as:p");

        command = client.waitForCommand();
        assertTrue(command.type().equals("position"));
        client.sendMessage("command:position&ship:"+command.get("ship")+"&location:a1&orientation:v");

        command = client.waitForCommand();
        assertTrue(command.type().equals("position"));
        client.sendMessage("command:position&ship:"+command.get("ship")+"&location:a2&orientation:v");

        command = client.waitForCommand();
        assertTrue(command.type().equals("position"));
        client.sendMessage("command:position&ship:"+command.get("ship")+"&location:a3&orientation:v");

        command = client.waitForCommand();
        assertTrue(command.type().equals("position"));
        client.sendMessage("command:position&ship:"+command.get("ship")+"&location:a4&orientation:v");

        command = client.waitForCommand();
        assertTrue("expecting commant type to be draw but received: " +command.type(), command.type().equals("draw"));

        String board1 = "sdcb###dcb####cb#####b##############";
        assertTrue("excpeting board1 to be: "+board1+" but received: "+command.get("board1"),
            command.get("board1").matches(board1));
    }
}