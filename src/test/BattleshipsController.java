package test;

import java.util.*;

import server.*;
import util.*;
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
		//TODO Verify Client Type
		if (state.equals(gameState.WAITING) && clients.size() == 2) {
				state = gameState.POSITIONING;
		}
		System.out.println("a New Player has joined ");
		System.out.println("Game State: " + this.state);
		
		if (clientType.equalsIgnoreCase("p")){ //Is a Player
			Command submarineCommand = generatePositionCommand(client, "submarine");
			sendCommand(client,submarineCommand.toString());
		}
		
	}
	
	private Command generateDrawCommand(){
		Command drawCommand = new Command();
		drawCommand.put("command", "draw");
		
		return drawCommand;
	}
	private Command generatePositionCommand(Client client, String shipType){
		Ship ship = new Ship(shipType,"v","a1");
		Command submarineCommand = new Command();
		submarineCommand.put("command", "position");
		submarineCommand.put("message", "Please place your "+shipType);
		submarineCommand.put("ship", shipType);
		submarineCommand.put("options", client.board.getPositionOptions(ship));
		submarineCommand.put("board1", client.board.getBoardString());
		submarineCommand.put("board2", new Board().getBoardString());
		return submarineCommand;
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
//			joinPlayer(client);""
//			sendCommand(client,generateFakePlayerJoinCommand());
			Command joinCommand = new Command("command:join&message:please join the game&options:p,v");
			sendCommand(client,joinCommand.toString());
			break;
		}
	}
	
	public void processClientCommand(Client client, String message){
//		HashMap<String, String> command = null;
		Command command = new Command(message);
		//TODO Delete these lines (Just tests)

		//TODO Verify Game States to process commands
		if (command.get("command").equals("join")){
			String clientType = command.get("as");
			joinPlayer(client,clientType);
		}
		
		if (command.get("command").equals("position")){
			placeShip(client, command);
		}
		
		if (command.get("command").equals("fire")){
			fire(client, command);
		}
		
	}
	
	 private void fire(Client client,Command command){
		 
	 }
	 
	private void placeShip(Client client,Command command){
		String shipType = command.get("ship");
		String position = command.get("location");
		String orientation = command.get("orientation");
//		Ship ship = new Ship(shipType,orientation,position);
		try{
			client.addShip(shipType,orientation,position);
		}catch(Exception e){
			//TODO Send Placement Error Message
			System.out.println(e);
		}

		if (shipType.equalsIgnoreCase("cruiser")){
			Command destroyerCommand = generatePositionCommand(client, "destroyer");
			sendCommand(client,destroyerCommand.toString());
//			sendCommand(client,fakeDestroyerPositionCommand());
		}
		
		if (shipType.equalsIgnoreCase("submarine")){
			Command cruiserCommand = generatePositionCommand(client, "cruiser");
			sendCommand(client,cruiserCommand.toString());
//			sendCommand(client,fakeCruiserPositionCommand());
		}
		
		if (shipType.equalsIgnoreCase("destroyer")){
			Command battleshipCommand = generatePositionCommand(client, "battleship");
			sendCommand(client,battleshipCommand.toString());
//			sendCommand(client,fakeBattleshipPositionCommand());
		}
		if (shipType.equalsIgnoreCase("battleship")){
			sendCommand(client,"wait");
		}

	}
	
	private String generateFakePlayerJoinCommand(){
		return "command:join&message:please join the game&options:p,v";
	}
	
	private String fakeCruiserPositionCommand(){
		return "command:position&ship:submarine&&Message:Please place your Cruiser&options:a1,a2,a4&board:###00#s##";
	}
	private String fakeDestroyerPositionCommand(){
		return "command:position&ship:submarine&&Message:Please place your Destroyer&options:a1,a2,a4&board:#cc###s###";
	}
	private String fakeBattleshipPositionCommand(){
		return "command:position&ship:submarine&&Message:Please place your Battleship&options:a1,a2,a4&board:d##cc###ss#";
	}

	
}
