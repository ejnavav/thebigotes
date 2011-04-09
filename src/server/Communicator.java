package server;

import java.net.*;
import java.io.*;
import java.util.*;

 
public class Communicator extends Thread {
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private int port;
	private PrintWriter out;
	private Scanner in;
	private boolean keepRunning = true;
	private ServerProtocol protocol = new ServerProtocol();

	
	//TODO:I think we must have a hasmap with all the clients to strem the player results
	//private Player p1 = new Player();
	//private Player p2 = new Player();
	
	public Communicator(int port) {
		this.port = port;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listenin to port:" + port);
		} catch (IOException e) {
			System.err.println("Couldn't start listening on port: " + port);
			System.exit(0);
		}

		try {
			
			//TODO: this is all the assimgnet 
			//the first  2 clients are player 1+2 and all the others are the public 
			
			System.out.println("Waiting for client ...");
			clientSocket = serverSocket.accept();
			System.out.println("Client connected ip:" + clientSocket.getInetAddress());
			
			
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(0);
		}

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new Scanner(clientSocket.getInputStream());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		sendMessage("command:join");
		
		// sendjoin();
		// loop();
		disconnect();
	}

	public void loop() {
		String inputLine; String outputLine;
		// out.println("Welcome to Battleships");
		while (keepRunning) {				
			if (in.hasNextLine()){
				inputLine = in.nextLine();
				System.out.println("Received: " + inputLine+ "/n");

				if (inputLine.matches("ping")){
					outputLine = "pong";
				}
				else {
					outputLine = "received: "+inputLine + "/n";
				}
				out.println(outputLine);
				System.out.println("sent "+outputLine+ "/n");	
			}
		}
	}
	
	public void disconnect() {
		try {
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
			System.out.println("Server Disconnected");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Connection closed");
	}
	
	public void quit(){
		keepRunning = false;
	}
	
	public void sendMessage(String msg){
		System.out.println("Sending: "+msg);
		out.println(msg);
	}
	
	public String getMessage(){
		String inputLine;
		System.out.println("Reading message");	
		if (in.hasNextLine()){
			inputLine = in.nextLine();
			System.out.println("Received: " + inputLine);	
			return inputLine;
		}
		else {
			System.out.println("in.hasNextLine() is false");	
			return null;
		}
	}
}