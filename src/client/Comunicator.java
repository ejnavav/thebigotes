package client;

import java.io.*;
import java.net.*;

public class Comunicator {
	//String serverHost = "10.1.1.7";
	int serverPort = 54321;
	String serverHost = "localhost";
//	int serverPort = 8000;
	PrintWriter outputToServer;
	BufferedReader inputFromServer;
	Socket socket;
	public void startClient() {

		try {
			socket = new Socket(serverHost, serverPort);
			inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputToServer = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			System.out.println("Could not Connect to " + serverHost
					+ "on port " + serverPort);
		}
		
		
	}

	public void sendMessage(String msg) {
		try {
			outputToServer.println(msg);
			
		} catch (Exception e) {
			System.out.println("There was an error trying to send the message");
		}
	}

	public String receiveMesssage() {
		String MsgFromServer="";
		try {
			while (true){
			 MsgFromServer =  inputFromServer.readLine();
			 if (MsgFromServer.length()>0) break;
			}
		} catch (IOException e) {
			System.out.println("There was an error  receiving the message");
		} catch (Exception e){
			System.out.println("There was an error trying to receive the messages");
		}
		return MsgFromServer;
	}
}
