package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Separate Thread that listen to new Connection
 * Creates a new Client for every Connection Attempt
 */
public class ConnectionListener extends Thread {
	public void run() {
		int port = 54321;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started\n");
			System.out.println("Listening on port " + port);
		} catch (IOException e) {
			System.err.println("Could not start listening on port: " + port);
			System.exit(1);
		}

		// Socket clientSocket = null;
		try {
			while (true) {
				Socket clientSocket = new Socket();
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
