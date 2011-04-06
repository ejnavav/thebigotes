package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ClientListener extends Thread {
Socket socket;
BufferedReader in;
Client client;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
		in = new BufferedReader(
			new InputStreamReader(
			socket.getInputStream()));
		while (true){
			String input = in.readLine();
//			System.out.println("Received hh "+ input);
			if (input.length()>0) BattleShipsServer.processClientCommand(this.client,input);
		}
		}catch(Exception e){
			System.out.println(e);
		}


	}
	public ClientListener(Client client,Socket socket){
		this.socket = socket;
		this.client = client;
	}
	

}
