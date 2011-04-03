package server;
import java.io.*;
import java.net.*;

public class Comunicator {
	int socketPort = 8000;

	public void startServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(socketPort);
			System.out.println("Listening on port: "+socketPort);
			while (true){
				Socket socket = serverSocket.accept();
				DataInputStream inputFromClient = new DataInputStream(
						socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(
						socket.getOutputStream());
				String msgReceived = inputFromClient.readUTF();
				outputToClient.writeUTF("received: " +msgReceived);
			}

		} catch (IOException e) {
			System.err.println("Could not Open port on Server" + socketPort);
			System.exit(1);
		}
	}
}
