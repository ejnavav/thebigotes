package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	public Client(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {

		}
		ClientListener clientListener = new ClientListener(socket);
		clientListener.start();
	}

	public String sendCommand(String command) throws Exception {
		out.println(command);
		String input = null;
		try {
			while (true) {
				input = in.readLine();
				//System.out.println("Received " + input);
				if (input.length() > 0)
					return input;
			}
		} catch (Exception e) {
			throw e ;//System.out.println(e);
		}

	}
}
