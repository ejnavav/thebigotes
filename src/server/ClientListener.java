package server;

import java.net.*;
import java.io.*;
/**
 * The Thread that listen to clients requests
 */
public class ClientListener extends Thread {
Socket socket;
BufferedReader in;
private Client client;
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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
			if (input==null)return;
			if (input.length()>0) BattleShipsServer.gameController.processClientCommand(client, input);
			
		}
		}catch(Exception e){
			System.out.println(e);
			BattleShipsServer.clientDisconnected(client);
		}


	}
	/**
	 * @param client The client to be connected to
	 * @param socket Created by the connection listener
	 */
	public ClientListener(Client client, Socket socket){
		this.socket = socket;
		this.client = client;
	}
	/**
	 * To Stop the process
	 */
	public void kill(){
		try{
			socket.close();
		}catch (Exception e){
			
		}
		
	}
	

}
