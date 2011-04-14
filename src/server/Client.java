package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Victor Nava
 * Holds Informaton about a Client and performs operations on them
 * used to send/receive messages from a client
 *
 */
public class Client {
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	private String type;
	public Board board;
	public boolean isReady= false;
	public boolean hasTurn = false;
	ClientListener clientListener;
	String name;
	Protocol protocol;
	
	//Receives the Socket created by the connection listener
	public Client(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {

		}
		clientListener = new ClientListener(this,socket);
		clientListener.start();
		System.out.println("After Thread Start");
		
		this.board = new Board();
		this.protocol = new Protocol();
	}
	
	/**
	 * @return The Player's name
	 */
	public String getName(){return name;}
	
	/**
	 * Sets the name of the player to be displayed to viewers
	 * @param name
	 */
	public void setName(String name){this.name=name;}
	
	public void receiveCommand(String command){
		if (protocol.isValidCommand(command)){
			BattleShipsServer.gameController.processClientCommand(this, command);
		}
	}
	/**
	 * Sends commands to the client
	 * @param command
	 * @throws Exception
	 */
	public void sendCommand(String command) throws Exception {
		protocol.setLastCommand(command);
		out.println(command);
	}
	/**
	 * Tries to place a Ship in the board
	 * @param shipType
	 * @param orientation
	 * @param position
	 * @throws Exception if the ship cant be placed in the given position
	 */
	public void addShip(String shipType,String orientation, String position )throws Exception{
		try{
			board.placeShip(shipType,orientation,position);
		}catch(Exception e){
			//TODO Create Proper exception
			e.printStackTrace();
			throw e;
		}
		
	}
	/**
	 * @return The Type of Client "p" "v"
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of client "p" for Player "v" for viewer
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	
	/**
	 * To Stop the separate listener thread
	 */
	public void kill(){
		clientListener.kill();
		clientListener.interrupt();
		try {
			this.finalize();
		}catch(Throwable e){
			
		}
		
	}
}
