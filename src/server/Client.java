package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Client {
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	private String type;
	ArrayList<Ship> ships;
	Board board;

	public Client(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
//			in = new BufferedReader(new InputStreamReader(
//					socket.getInputStream()));
		} catch (Exception e) {

		}
		ClientListener clientListener = new ClientListener(this,socket);
		clientListener.start();
		System.out.println("After Thread Start");
	}

	public void sendCommand(String command) throws Exception {
		out.println(command);
//		String input = null;
//		try {
//			while (true) {
//				input = in.readLine();
//				//System.out.println("Received " + input);
//				if (input.length() > 0)
//					return input;
//			}
//		} catch (Exception e) {
//			throw e ;//System.out.println(e);
//		}

	}
	public void addShip(Ship ship){
		this.ships.add(ship);
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
