package server;

import java.net.*;
import java.io.*;
public class ClientListener extends Thread {
Socket socket;
BufferedReader in;
private Client client;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
		in = new BufferedReader(
			new InputStreamReader(
			socket.getInputStream()));
		while (true){
			String input = in.readLine();
			System.out.println("Received from Client: "+ input);
			if (input.length()>0) BattleShipsServer.gameController.processClientCommand(client, input);
		}
		}catch(Exception e){
			System.out.println(e);
		}


	}
	public ClientListener(Client client, Socket socket){
		this.socket = socket;
		this.client = client;
	}
	

}
