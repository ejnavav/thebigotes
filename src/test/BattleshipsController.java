package test;

import java.util.*;

import server.*;
public class BattleshipsController {
	ArrayList<Client> clients = new ArrayList<Client>();
	gameState state;
	//jhdsjd

	public BattleshipsController() {
		state = gameState.WAITING;
	}

	public enum gameState {
		WAITING, POSITIONING, PROGRESS
	}

	public void joinPlayer(Client client, String clientType) {
		client.setType(clientType);
		clients.add(client);
		if (state.equals(gameState.WAITING) && clients.size() == 2) {
				state = gameState.POSITIONING;
		}
		System.out.println("a New Player has joined ");
		System.out.println("Game State: " + this.state);
		
		if (clientType.equalsIgnoreCase("p")){
			String positionCommand = fakeSumbarinePositionCommand();
			sendCommand(client,positionCommand);
		}
		
	}
	
	private void sendCommand(Client client, String command){
		try {
			System.out.println("Sent From Server: " + command);
			client.sendCommand(command);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}

	public void newClientArrived(Client client) {
		switch (state) {
		case WAITING:
//			joinPlayer(client);
			sendCommand(client,generateFakePlayerJoinCommand());
			break;
		}
	}
	
	public void processClientCommand(Client client, String message){
		HashMap<String, String> command = null;
		//TODO Delete these lines (Just tests)
		if (message.equalsIgnoreCase("j")){
			command = parseFakeJoinCommand();
		}
		if (message.equalsIgnoreCase("p1")){
			command = parseSubFakePositionCommand();
		}
		if (message.equalsIgnoreCase("p2")){
			command = parseCruFakePositionCommand();
		}
		if (message.equalsIgnoreCase("p3")){
			command = parseDesFakePositionCommand();
		}
		if (message.equalsIgnoreCase("p4")){
			command = parseBatFakePositionCommand();
		}

		
		if (command.get("command").equals("join")){
			String clientType = command.get("as");
			joinPlayer(client,clientType);
		}
		
		if (command.get("command").equals("position")){
			placeShip(client, command);
		}
	}
	
	private void placeShip(Client client,HashMap<String,String> command){
		String shipType = command.get("ship");
		int position = Integer.parseInt(command.get("position"));
		String orientation = command.get("orientation");
		Ship ship = new Ship(shipType,orientation,position);
		client.addShip(ship);
		
		if (shipType.equalsIgnoreCase("submarine")){
			sendCommand(client,fakeCruiserPositionCommand());
		}
		if (shipType.equalsIgnoreCase("cruiser")){
			sendCommand(client,fakeDestroyerPositionCommand());
		}
		if (shipType.equalsIgnoreCase("destroyer")){
			sendCommand(client,fakeBattleshipPositionCommand());
		}
		if (shipType.equalsIgnoreCase("battleship")){
			
		}

	}
	
	private String generateFakePlayerJoinCommand(){
		return "command:join&message:please join the game&options:p,v";
	}
	
	private String fakeSumbarinePositionCommand(){
		return "command:position&Message:Please place your Submarine&options:a1,a2,a4&board:##########";
	}
	private String fakeCruiserPositionCommand(){
		return "command:position&Message:Please place your Cruiser&options:a1,a2,a4&board:###00#s##";
	}
	private String fakeDestroyerPositionCommand(){
		return "command:position&Message:Please place your Destroyer&options:a1,a2,a4&board:#cc###s###";
	}
	private String fakeBattleshipPositionCommand(){
		return "command:position&Message:Please place your Battleship&options:a1,a2,a4&board:d##cc###ss#";
	}
	
	private static HashMap<String,String> parseFakeJoinCommand(){
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "join");
		command.put("as", "p");
		return command;
	}
	private static HashMap<String,String> parseSubFakePositionCommand(){
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "position");
		command.put("ship", "submarine");
		command.put("position", "1");
		command.put("orientation", "v");
		return command;
	}
	private static HashMap<String,String> parseCruFakePositionCommand(){
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "position");
		command.put("ship", "cruiser");
		command.put("position", "10");
		command.put("orientation", "h");
		return command;
	}
	private static HashMap<String,String> parseDesFakePositionCommand(){
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "position");
		command.put("ship", "destroyer");
		command.put("position", "12");
		command.put("orientation", "h");
		return command;
	}
	private static HashMap<String,String> parseBatFakePositionCommand(){
		HashMap<String, String> command = new HashMap<String, String>();
		command.put("command", "position");
		command.put("ship", "battleship");
		command.put("position", "14");
		command.put("orientation", "v");
		return command;
	}
	
	
}
