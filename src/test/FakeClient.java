package test;

import java.io.*;
import java.net.*;
import java.util.*;

public class FakeClient {
	String host;
	int port;
	Socket socket = null;
	PrintWriter out = null;
	// Scanner in = null;
	BufferedReader in = null;

	
	public FakeClient(String host, int port) {
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
	
	public static void main(String[] args) {
		FakeClient client = new FakeClient("localhost", 54321);
		
		client.connect();
		
		while(true){
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			client.getMessage();
		}
	}
}