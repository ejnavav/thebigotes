package client;

import java.io.*;
import java.net.*;

public class Communicator {
	String host;
	int port;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

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
	
	public String waitForMessage(){
		String msg = null;
		try{
			msg = in.readLine();
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
		return msg;
	}

	public void sendMessage(String message){
		try {
			out.println(message);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		Communicator client = new Communicator("localhost", 54321);
		client.connect();
	}
}