package server;

import java.util.*;

import util.*;

public class BattleshipsController {
	ArrayList<Client> clients = new ArrayList<Client>();
	gameState state;
	Client player1;
	Client player2;

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
	
	//TODO move to command class
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
		submarineCommand.put("board1", client.board.ownView());
		submarineCommand.put("board2", new Board().oponentView()); //TODO Generate Board2 String
		return submarineCommand;
	}
	
	//TODO move to command class
	private Command generateDrawCommand(String board1, String board2, String message){
		Command drawCommand = new Command();
		drawCommand.put("command", "draw");
		drawCommand.put("board1", board1);
		drawCommand.put("board2", board2);
		drawCommand.put("message", message);
		return drawCommand;
	}
	
	//TODO move to command class
	private Command generateWaitCommand(String message){
        Command command = new Command();
        command.put("command", "wait");
        command.put("message", message);
        return command;
	}
	
	//TODO move to command class
	private Command generateFireCommand(String message, String board1, String board2){
        Command command = new Command();
        command.put("command", "fire");
        command.put("message", message);
        command.put("board1", board1);
        command.put("board2", board2);
        return command;
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
    private void fire(Client client, Command command){
        String location = command.get("location");
        Client enemy = getEnemy(client);
        boolean hit = false;
        try {
            hit = client.board.fire(location);
        } catch (Exception e) {
            //TODO do something about this
            System.out.println("can't fire there");
            return;
        }
        
        if(hit){
            Ship ship = client.board.getShip(location);
            if (ship.isDestroyed()){
                
            	String board1 = enemy.board.ownView();
                String board2 = client.board.oponentView();
                String msg = "Your "+ship.getType()+" is FUBAR";
                sendCommand(enemy, generateDrawCommand(board1, board2, msg).toString());
                
                board1 = client.board.ownView();
                board2 = enemy.board.oponentView();
                sendCommand(client, generateFireCommand("You destroyed the "+ship.getType(), board1, board2).toString());
            }
        }
        
        
        System.out.println("somebody fired");
    }
	 
	private void placeShip(Client client,Command command){
		String shipType = command.get("ship");
		String position = command.get("location");
		String orientation = command.get("orientation");
//		Ship ship = new Ship(shipType,orientation,position);
		try{
		    client.addShip(shipType, orientation, position);
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
                //TODO put this in a function
            	System.out.println("shipType.equalsIgnoreCase");
                
                // String board2 = (getEnemy(client) == null) ? (new Board()).oponentView() : getEnemy(client).board.oponentView();
                // String board2 = getEnemy(client).board.oponentView();
                
                System.out.println("String board2 = getEnemy(client).board.oponentView();");
                
                sendCommand(client, generateDrawCommand(client.board.ownView(), (new Board()).oponentView(), null).toString());
                // sendCommand(client, generateDrawCommand(client.board.ownView(), board2, null).toString());
                
                System.out.println("sendCommand(client, generateDrawCommand(client.board.ownView(), board2, null).toString());");
                
                client.isReady = true;
                         
                 if (player1.isReady) { //player1 is ready
                     if (player2 == null) {
                         sendCommand(player1, generateWaitCommand("wait other player to connect").toString());
                         System.out.println("sendCommand(player1, generateWaitCommand).toString());");
                     } else if (player2.isReady){
                         System.out.println("player2.isReady");
                         startGame();
                     } else { //player2 is not ready
                         sendCommand(player1, generateWaitCommand("wait other player to position").toString());
                         System.out.println("sendCommand(player1, generateWaitCommandwait other player to position).toString());");
                     }
                 } else if (player2.isReady){
                     sendCommand(player2, generateWaitCommand("wait other player to position").toString());
                     System.out.println("sendCommand(player2, generateWaitCommand(wait other player to position).toString());");
                 }
            }
            
		} catch(Exception e){
			//TODO Send Placement Error Message
            System.out.println(e.getStackTrace());
			Command errorCommand = generatePositionCommand(client, shipType);
			errorCommand.put("message", "Error placing your "+shipType);
			this.sendCommand(client, errorCommand.toString());
		}
	}
	
	private void setPlayerTurn(Client player){
	    if (player == player1){
	        player1.hasTurn = true;
	        player2.hasTurn = false;
	    } else{
	        player1.hasTurn = false;
	        player2.hasTurn = true;
	    }	    
	}
	
	private Client getEnemy(Client client){
	    if (client == player1){
	        return player2;
	    } else{
	        return player1;
	    }
	}
	
	private void startGame(){
        System.out.println("game started");
        String fireMsg = "Your turn, select location to fire"; 
        String waitMsg = "Other player has the turn..."; 
        if (Math.random() > 0.5){ setPlayerTurn(player1); }
        else{ setPlayerTurn(player2); }
	    
	    if (player1.hasTurn){
	        sendCommand(player2, generateWaitCommand(waitMsg).toString());
	        Command command = generateFireCommand(fireMsg, player1.board.ownView(), player2.board.oponentView());
	        sendCommand(player1, command.toString());
	    } else{
	        sendCommand(player1, generateWaitCommand(waitMsg).toString());
	        Command command = generateFireCommand(fireMsg, player1.board.ownView(), player2.board.oponentView());
	        sendCommand(player2, command.toString());
	    }
	}
}
