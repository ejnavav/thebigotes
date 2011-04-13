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
		WAITING, POSITIONING, PROGRESS, FINISHED
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
        System.out.println("somebody fired");
        try {
            hit = enemy.board.fire(location);
        } catch (Exception e) {
            //TODO do something about this
            System.out.println("can't fire there");
            String clientMsg = "Can't fire in that location, try again";
            sendCommand(client, generateFireCommand(clientMsg, client.board.ownView(), enemy.board.oponentView()).toString());
            return;
        }
        
        String enemyMsg = ""; String clientMsg = "";
        
        if(hit){
            System.out.println("hit");
            Ship ship = enemy.board.getShip(location);
            if (ship.isDestroyed()){
                System.out.println("ship destroyed");
                enemyMsg = "Your "+ship.getType()+" is FUBAR";
                clientMsg = "You destroyed the "+ship.getType();
                
                if (!enemy.board.hasShipsAlive()){ //GAME OVER
                	state = gameState.FINISHED;
                	clientMsg = "Game Over, You nailed your oponent";
                	enemyMsg = "Game Over, Your oponent nailed you";
                	sendCommand(enemy, generateDrawCommand(enemy.board.ownView(), client.board.oponentView(), enemyMsg).toString());
                	sendCommand(client, generateDrawCommand(client.board.ownView(), enemy.board.oponentView(), clientMsg).toString());
                	//handle gameOver
                	return;
                }
                
            } else { //hit something
                System.out.println("hit something");
                enemyMsg = "Your "+ship.getType()+" got hit";
                clientMsg = "You hit something";
            }
            //TODO refactor
            sendCommand(enemy, generateDrawCommand(enemy.board.ownView(), client.board.oponentView(), enemyMsg).toString());
            sendCommand(client, generateFireCommand(clientMsg, client.board.ownView(), enemy.board.oponentView()).toString());
        } else{
             System.out.println("no hit");
            enemyMsg = "You are safe. Its your turn to fire";
            clientMsg = "You wasted a bullet. Other player has the turn";
            //TODO refactor
            sendCommand(client, generateDrawCommand(client.board.ownView(), enemy.board.oponentView(), clientMsg).toString());
            sendCommand(enemy, generateFireCommand(enemyMsg, enemy.board.ownView(), client.board.oponentView()).toString());
            setPlayerTurn(enemy);
        }    
    }
	 
	private void placeShip(Client client,Command command){
		String shipType = command.get("ship");
		String position = command.get("location");
		String orientation = command.get("orientation");
		
		try{
		    client.addShip(shipType, orientation, position);
			if (shipType.equalsIgnoreCase("cruiser")){
				Command destroyerCommand = generatePositionCommand(client, "battleship");
				sendCommand(client,destroyerCommand.toString());
			}
			
			if (shipType.equalsIgnoreCase("submarine")){
				Command cruiserCommand = generatePositionCommand(client, "destroyer");
				sendCommand(client,cruiserCommand.toString());
			}
			
			if (shipType.equalsIgnoreCase("destroyer")){
				Command battleshipCommand = generatePositionCommand(client, "cruiser");
				sendCommand(client,battleshipCommand.toString());
			}
            if (shipType.equalsIgnoreCase("battleship")){
                sendCommand(client, generateDrawCommand(client.board.ownView(), (new Board()).oponentView(), null).toString());
                client.isReady = true;
                
                //TODO put this in a function                 
                 if (player1.isReady) {
                     if (player2 == null) { //player2 hasnt join
                         sendCommand(player1, generateWaitCommand("wait other player to connect").toString());
                     } else if (player2.isReady){
                         startGame();
                     } else { //player2 is not ready
                         sendCommand(player1, generateWaitCommand("wait other player to position").toString());
                     }
                 } else if (player2.isReady){
                     sendCommand(player2, generateWaitCommand("wait other player to position").toString());
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
	    return (client == player1) ? player2 : player1;
	}
	
	private void startGame(){
        System.out.println("game started");
        String fireMsg = "Your turn, select location to fire"; 
        String waitMsg = "Other player has the turn"; 
        
        //flip a coin
        if (Math.random() > 0.5){ setPlayerTurn(player1); }
        
        else{ setPlayerTurn(player2); }
	    
	    if (player1.hasTurn){
	        sendCommand(player2, generateWaitCommand(waitMsg).toString());
	        Command command = generateFireCommand(fireMsg, player1.board.ownView(), player2.board.oponentView());
	        sendCommand(player1, command.toString());
	    } else{
	        sendCommand(player1, generateWaitCommand(waitMsg).toString());
	        Command command = generateFireCommand(fireMsg, player2.board.ownView(), player1.board.oponentView());
	        sendCommand(player2, command.toString());
	    }
	}
}
