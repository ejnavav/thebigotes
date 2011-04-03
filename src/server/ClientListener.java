package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ClientListener implements Runnable {
Socket socket;
PrintWriter out;
BufferedReader in;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(
			new InputStreamReader(
			socket.getInputStream()));
		}catch(Exception e){
		
		}

	}
	public ClientListener(Socket socket){
		this.socket = socket;
	}
	
	public void sendCommand(String command){
		out.println(command);
	}

}
