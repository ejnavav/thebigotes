package server;

import java.util.HashMap;

public class BattleShipsServer {
	private int port;
	public static BattleshipsController gameController;

	public BattleShipsServer(int port) {
		this.port = port;
	}

	public void start() {		
		ConnectionListener listener = new ConnectionListener();
		listener.start();

	}

	public static void main(String[] args) {
		BattleShipsServer server = new BattleShipsServer(54321);
	    server.start();
		gameController = new BattleshipsController();

	}

	public static void newClientConnected(Client client) {
		gameController.newClientArrived(client);
	}	
}