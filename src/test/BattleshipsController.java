package test;

import java.util.*;

import server.*;
import util.*;
public class BattleshipsController {
	ArrayList<Client> clients = new ArrayList<Client>();
	gameState state;
	Client clientWithTurn;
	Client player1;
	Client player2;
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
			if (player1==null) player1 = client;
			else player2 = client;
			Command submarineCommand = generatePositionCommand(client, "submarine");
			sendCommand(client,submarineCommand.toString());
		}
		
	}
	
	
	private Command generatePositionCommand(Client client, String shipType){
		Ship ship = new Ship(shipType,"v","a1");
		Command submarineCommand = new Command();
		String shipString =" \\";
		for (int i =0;i<ship.getSize();i++) shipString+="_";
		shipString+="/";
		submarineCommand.put("command", "position");
		submarineCommand.put("message", "Please place your "+shipType+shipString);
		submarineCommand.put("ship", shipType);
		submarineCommand.put("options", client.board.getPositionOptions(ship));
		submarineCommand.put("board1", client.board.getBoardString());
		submarineCommand.put("board2", new Board().getBoardString()); //TODO Generate Board2 String
		return submarineCommand;
	}
	
	private Command generateDrawCommand(Client client){
		//TODO Generate Command according to the client type/game status
		Command drawCommand = new Command();
		drawCommand.put("command", "draw");
		drawCommand.put("message","test");
		drawCommand.put("board1", client.board.getBoardString());
		drawCommand.put("board2", new Board().getBoardString());//TODO Generate Board2 String
		return drawCommand;
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
			//TODO Check Game Status and generate proper Status Command (Options)
			Command joinCommand = new Command("command:join&message:please join the game&options:p,v");
			sendCommand(client,joinCommand.toString());
			break;
		}
	}
	
	public void processClientCommand(Client client, String message){
//		HashMap<String, String> command = null;
		Command command = new Command(message);

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
			if (shipType.equalsIgnoreCase("cruiser")){
				Command destroyerCommand = generatePositionCommand(client, "battleship");
				sendCommand(client,destroyerCommand.toString());
//				sendCommand(client,fakeDestroyerPositionCommand());
			}
			
			if (shipType.equalsIgnoreCase("submarine")){
				Command cruiserCommand = generatePositionCommand(client, "destroyer");
				sendCommand(client,cruiserCommand.toString());
//				sendCommand(client,fakeCruiserPositionCommand());
			}
			
			if (shipType.equalsIgnoreCase("destroyer")){
				Command battleshipCommand = generatePositionCommand(client, "cruiser");
				sendCommand(client,battleshipCommand.toString());
//				sendCommand(client,fakeBattleshipPositionCommand());
			}
			if (shipType.equalsIgnoreCase("battleship")){
				client.isReady = true;
//				if(player1!=null && player2!=null &&player1.isReady&&player2.isReady)
				sendCommand(client,generateDrawCommand(client).toString());
			}
		}catch(Exception e){
			//TODO Send Placement Error Message
			//System.out.println(e);
			Command errorCommand = generatePositionCommand(client, shipType);
			errorCommand.put("message", "Error placing your "+shipType);
			this.sendCommand(client, errorCommand.toString());
		}

	}
	
	private Client flipCoin(){
		
		return (Math.random()>0.5)? player1:player2;
		
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
