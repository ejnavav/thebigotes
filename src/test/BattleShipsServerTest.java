package test;

import org.junit.*;
import static org.junit.Assert.*;
import server.*;
import client.*;
import util.*;

public class BattleShipsServerTest {

    BattleShipsServer server = null;
    // String host = "localhost";
    String host = "10.1.1.6";
    int port = 54321;

    @Test
    public void accepts_multiple_client_connection() {
        System.out.println("accepts_multiple_client_connection");            
        BattleShipsClient client1 = new BattleShipsClient(host, port);
        BattleShipsClient client2 = new BattleShipsClient(host, port);
        assertTrue(client1.connect());
        assertTrue(client2.connect());
    } 

    @Test
    public void two_player_can_join() {
        System.out.println("two_player_can_join()");
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

    @Test
    public void should_position_ships() {
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