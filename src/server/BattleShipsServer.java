package server;

public class BattleShipsServer {

	
	private Communicator communicator;
	
	private int port;
	private static test.BattleshipsController gameController;

	public BattleShipsServer(int port) {
		this.port = port;
	}

	public void start() {
		// communicator = new Thread(new Communicator(port));
//		communicator = new Communicator(port);
//		communicator.start();
		
		ConnectionListener listener = new ConnectionListener();
		listener.start();

	}

	public void stop() {
		communicator.quit();
		while (communicator.isAlive()) {
		}
		System.out.println("stoped");
	}

	public static void main(String[] args) {
		BattleShipsServer server = new BattleShipsServer(54321);
	    server.start();
		gameController = new test.BattleshipsController();

//		while (true) {
//			System.out.println("Client ID");
//			Scanner input = new Scanner(System.in);
//			int clientId = Integer.parseInt(input.nextLine());
//			System.out.println("Command");
//			String command = input.nextLine();
//			try {
//				System.out.println("Received from Client: "
//						+ clientList.get(clientId).sendCommand(command));
//			} catch (Exception e) {
//				System.out.println("Error :" + e);
//			}
//
//		}

	}

	public static void newClientConnected(Client client) {
		gameController.newClientArrived(client);
	}
	
	
}