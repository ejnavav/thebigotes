package server;

public class BattleShipsServer {

	
	private Communicator communicator;
	
	private int port;

	public BattleShipsServer(int port) {
		this.port = port;
	}
	
	public void start() {
		// communicator = new Thread(new Communicator(port));
		communicator = new Communicator(port);
		communicator.start();
	}
	
	public void stop() {
		communicator.quit();
		while(communicator.isAlive()){}
		System.out.println("stoped");
	}

	public static void main(String[] args) {
		BattleShipsServer server = new BattleShipsServer(54321);
		server.start();
	}
}