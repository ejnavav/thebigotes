package client;

import java.io.*;
import java.net.*;


/**
 * Class in charge of all the communication with the client
 * It sends a receives messages using a socket connection.
 * 
 * @author John Kolovos
 * 
 */
public class Communicator {
	String host;
	int port;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	
	
	/**
	 * Constructor 
	 * @param host to connect
	 * @param port to connect
	 */
	public Communicator(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Make a socket connection with the server
	 * 
	 * @return true if it can connect false otherwise
	 */
	public boolean connect() {
		try {
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			return false;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + host);
			return false;
		}
		return true;
	}

	/**
	 * Closes the socket connection
	 */
	public void disconnect() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	/**
	 * Waits until there is a string (message) in the socket
	 * 
	 * @return the message received
	 */
	public String waitForMessage(){
		String msg = null;
		try{
			msg = in.readLine();
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
		return msg;
	}

	/**
	 * Sends a message
	 * @param message
	 */
	public void sendMessage(String message){
		try {
			out.println(message);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Test harness 
	 * @param args
	 */
	public static void main(String[] args) {
		Communicator client = new Communicator("localhost", 54321);
		client.connect();
	}
}