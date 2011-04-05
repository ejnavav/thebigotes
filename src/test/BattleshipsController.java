package test;

import java.util.*;

public class BattleshipsController {
	ArrayList<server.Client> clients = new ArrayList<server.Client>();
	gameState state;

	public BattleshipsController() {
		state = gameState.WAITING;
	}

	public enum gameState {
		WAITING, POSITIONING, PROGRESS
	}

	public void joinPlayer(server.Client client) {
		try {
			String command = "command:join";
			System.out.println("Sent From Server: " + command);
			System.out.println("Received from Client: "
					+ client.sendCommand(command));

			clients.add(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (state.equals(gameState.WAITING)) {
			if (clients.size() == 2) {
				state = gameState.POSITIONING;
			}
		}
	}

	public void newClientArrived(server.Client client) {
		switch (state) {
		case WAITING:
			joinPlayer(client);
			break;
		}
	}

}
