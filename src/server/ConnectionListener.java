package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread {
	public void run(){
		int port = 54321;	

		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on port " +port);
		} catch (IOException e) {
			System.err.println("Could not start listening on port: " + port);
			System.exit(1);
		}

		Socket clientSocket = null;
		try {
			while (true){
				clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket);
				System.out.println("New Client Connected ");
				BattleShipsServer.newClientConnected(client);
			}
			

		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
	}
}
