package client;


import java.io.*;
import java.net.*;
//import java.util.*;

public class Communicator {
	String host;
	int port;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	private ClientProtocol protocol = new ClientProtocol();
 

	public Communicator(String host, int port) {
		this.host = host;
		this.port = port;
	}

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

	public void disconnect() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public String getLastMessage(){
		System.out.println("getLastMessage()");
		String msg = getMessage();
		String lastMsg = null;
		while(msg != null){
			lastMsg = new String(msg);
			msg = getMessage();
		}
		System.out.println("getLastMessage() > "+lastMsg);
		return lastMsg;
	}

	/**
	 * Get message from server
	 * @return
	 */
	
	public String getMessage(){
		System.out.println("getMessage()");
		String msg = null;
		try{
			if(in.ready()){
				msg = in.readLine();
			}
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
		System.out.println("getMessage() > "+msg);
		return msg;
	}

/**
 * sent message to server
 * @param message
 */

	public void sendMessage(String message){
		try {
			
			out.println(message);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	
	public String waitForMessage(){
		String msg = null;
		try{
			msg = in.readLine();
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
		return msg;
	}
	
	
	public static void main(String[] args) {
		Communicator client = new Communicator("localhost", 54321);
		client.connect();
	}
}