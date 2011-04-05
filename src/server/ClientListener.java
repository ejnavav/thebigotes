package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ClientListener extends Thread {
Socket socket;
BufferedReader in;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
		in = new BufferedReader(
			new InputStreamReader(
			socket.getInputStream()));
		while (true){
			String input = in.readLine();
			System.out.println("Received "+ input);
		}
		}catch(Exception e){
			System.out.println(e);
		}


	}
	public ClientListener(Socket socket){
		this.socket = socket;
	}
	

}
