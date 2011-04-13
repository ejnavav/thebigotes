package test;

import java.net.*;
import java.io.*;
import java.util.*;

public class FakeServer extends Thread {
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private int port;
	private PrintWriter out;
	private Scanner in;
	boolean ready = false;
	boolean quit = false;

	public FakeServer(int port) {
		this.port = port;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listenin to port:" + port);
		} catch (IOException e) {
			System.out.println("Couldn't start listening on port: " + port);
			System.exit(0);
		}
        
        ready = true;

		try {
			System.out.println("Waiting for client 1...");
			clientSocket = serverSocket.accept();
			System.out.println("Client 1 connected");
		} catch (IOException e) {
			System.out.println("Accept failed.");
			System.exit(0);
		}

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new Scanner(clientSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("Couldn't get in or out stream" + e.getMessage());
		}
				
		while(!quit){
		    try{Thread.sleep(10);}catch (Exception e) {};
		}
	}
	
	public void waitUntilReady(){
	    while(!ready){ System.out.println("wait for ready");};
	}
	
	public void quit() {
	    disconnect();
	    quit = true;
	}
	
	public void disconnect() {
		try {
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Connection closed");
	}
	
	public void sendMessage(String msg){
		System.out.println("Sending: "+msg);
		out.println(msg);
	}
	
	public String waitForMessage(){
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