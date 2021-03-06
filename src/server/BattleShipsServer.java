package server;

/**
 * @author Eduardo Nava
 * Game Server Class
 *
 */
public class BattleShipsServer {
	private int port;
	public static BattleshipsController gameController;

	/**
	 * Main Constructor
	 * @param port The port to listen to
	 */
	public BattleShipsServer(int port) {
		this.port = port;
	}

	/**
	 *  Starts the Server
	 *  Creates a Connection Listener (Separate Thread)
	 */
	public void start() {
		gameController = new BattleshipsController();
		ConnectionListener listener = new ConnectionListener(this.port);
		listener.start();
	}
		
	/**
	 * Class Driver
	 */
	public static void main(String[] args) {
		BattleShipsServer server = new BattleShipsServer(54321);
	    server.start();
		
	}

	/**
	 * To be called by the connection listener when a New client Arrives
	 * @param client
	 */
	public static void newClientConnected(Client client) {
		gameController.newClientArrived(client);
	}
	
	/**
	 * When a Client has disconnected/Lost connection
	 * @param client
	 */
	public static void clientDisconnected(Client client){
		gameController.clientDisconnected(client);
	}
}