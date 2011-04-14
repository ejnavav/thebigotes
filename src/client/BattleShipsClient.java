package client;
import java.util.*;
import util.*;
import server.*;

/**
 * The battleships client class which connects
 * to the battleship server to play the game.
 * 
 * This class is a very thin client that simply executes
 * commands send from the server. It doesn't have any
 * game logic and it doesn't keep any state of the game
 * either.
 * 
 */
public class BattleShipsClient {
    
	private Communicator server = null;
    private Scanner user = new Scanner(System.in);
    private boolean imViewer = true;
    
    
    /**
     * Starts a new communicator
     * @param host
     * @param port
     */
    public BattleShipsClient(String host, int port){
        server = new Communicator(host, port);
    }
    
    
    /**
     * The method to start the client.
     * Tries to connect to server and just waits for commands
     * Sent from server
     */
    public void run(){
        System.out.println("Welcome to Battleships");
        System.out.println("Connecting to server...");
        if (server.connect() == false){
            System.err.println("Couldn't connect, please try again");
            System.exit(0);
        }
        System.out.println("Connected");
        
        while(true){
            // System.out.println("Waiting for command...");
            Command command = waitForCommand();
            if (command == null){
                break;
            }
            // System.out.println("Command received: "+command.toString());
            handleCommand(command);
        }
    }
    
    /**
     * Connect to the server
     * 
     * @return true if connection ok, false otherwise
     */
    public boolean connect(){
        return server.connect();
    }
    
    /**
     * Sends a message/command to the server
     * 
     * @param message
     */
	public void sendMessage(String message){
	    server.sendMessage(message);
	}
	
	/**
	 * Keeps waiting until a command from server is received
	 * 
	 * @return Command from server
	 */
    public Command waitForCommand(){
        String commandStr = server.waitForMessage();
        Command command = null;
        try {
            command = new Command(commandStr);
        } catch (Exception e) {
            System.err.println("Invalid command: >"+commandStr);
        }
        return command;
    }

    /**
     * When a command from server this method is called
     * to handle the command, it is responsible to decide 
     * what to do with it.
     * 
     * @param command 
     */
    public void handleCommand(Command command){
        // System.out.println("handling command");

        if (command.type().equals("join")) {
            join(command);
        }
        else if  (command.type().equals("position")) {
            imViewer = false;
            position(command);
        }
        else if  (command.type().equals("draw")) {
            draw(command);
        }
        else if  (command.type().equals("wait")) {
            wait(command);
        }
        else if  (command.type().equals("fire")) {
            fire(command);
        }
        else if  (command.type().equals("gameover")) {
            gameover(command);
        }
        else {
            System.out.println("Don't know about command" +command.toString());
        }
    }

    /**
     * Asks the user to enter how they want to join
     * then sends the join reply to the server
     * 
     * @param command
     */
    public void join(Command command){
        System.out.println(command.get("message"));
        String[] options = command.get("options").split(",");
        String option = readValidOption(options);
        Command reply = new Command();
        reply.put("command", "join");
        reply.put("as", option);
        server.sendMessage(reply.toString());
    }
    
    /**
     * Asks the user to enter the location and orientation
     * to position one ship in the board, then sends a position
     * reply to the server with the position info.
     * 
     * @param command
     */
    public void position(Command command){
        drawGrid(command.get("board1"), command.get("board2"));
        System.out.println(command.get("message"));
        
        Board board = new Board(command.get("board1"));
        String location = null;
        String orientation = null;

        while(true) {
            System.out.println("Enter location:");
            location = user.nextLine();
            System.out.println("Enter orientation h or v:");
            orientation = user.nextLine();
            try {
                board.placeShip(command.get("ship"), orientation, location);
                break;
            } catch (Exception e) {
                System.out.println("Invalid location or orientation, try again.");
            }
        }

        Command reply = new Command();
        reply.put("command", command.type() );
        reply.put("ship", command.get("ship"));
        reply.put("location", location);
        reply.put("orientation", orientation);
        server.sendMessage(reply.toString());
    }
    
    
    /**
     * Displays the two boards and an alternative message 
     * 
     * @param command
     */
    public void draw(Command command){
        String board1 = command.get("board1");
        String board2 = command.get("board2");
        drawGrid(board1, board2);
        
        String msg = command.get("message");
        
        if(msg != null && !msg.equals("null")){
            System.out.println(command.get("message"));
        }
    }
    
    
    /**
     * Asks the user for a location to fire
     * 
     * @param command
     */
    public void fire(Command command){
        drawGrid(command.get("board1"), command.get("board2"));
        System.out.println(command.get("message"));
        Board board = new Board(command.get("board2"));

        String location = null;

        while(true) {
            System.out.println("Enter location to fire:");
            location = user.nextLine();
            try {
                board.fire(location);
                break;
            } catch (Exception e) {
                System.out.println("Invalid location, try again.");
            }
        }
        
        Command reply = new Command();
        reply.put("command", command.type());
        reply.put("location", location);
        server.sendMessage(reply.toString());
    }
    
    /**
     * Just prints a message and do nothing
     * 
     * @param command
     */
    public void wait(Command command){
        System.out.println(command.get("message"));
    }
    
    
    /**
     * Displays a the gameover message, sends a gameover
     * reply to the server then closes the app.
     * 
     * @param command
     */
    public void gameover(Command command){
        System.out.println(command.get("message"));
        sendMessage("command:gameover");
        server.disconnect();
        System.exit(0);
    }
    
    
    /**
     * Displays the boards
     *
     * It accepts two board-strings as argument.
     * Each boards is a 32 char long where a char
     * represents either a ship, a hit or a miss
     * in the board.
     *
     * @param player1 board String
     * @param player2 board String
     */
    public void drawGrid(String player1,String player2){
        player1 = player1.replace('#',' ');
        player2 = player2.replace('#',' ');
        char letter = 'A';
        
        String printString = "\n           Your Board                        Your Oponent's board\n\n";
        
        if(imViewer){
            printString = "\n           Player1 Board                        Player2 board\n\n";
        }
        
        printString += "   ";
        for (int j =0;j<2;j++){
            for (int i = 1; i<7;i++){
                printString+=("  "+i+" ");
            }
            printString+=("\t\t   ");
        }
        printString+=("\n   ");
        for (int i =0; i<50;i++){
            printString+=("-");
            if (i==24){
                printString+=("\t\t   ");
            }
        }
        
        printString+=("\n" + letter + "  ");
        for (int i=0;i<player1.length();i++){
            printString+="| "+ player1.substring(i,i+1) + " ";
            if (i>0&&(i+1)%6==0){
                printString+="|\t\t"+letter + "  ";
                for (int j=i-5;j<=i;j++){
                    printString+="| "+ player2.substring(j,j+1) + " ";
                }
                letter++;
                printString+="|\n"+(i<player1.length()-1 ? letter:" ") + "  ";
            }
        }
        
        for (int i =0; i<50;i++){
            printString+=("-");
            if (i==24){
                printString+=("\t\t   ");
            }
        }
        printString += "\n";
        System.out.println(printString);
    }

    /**
     * Prompts the user to enter an option until
     * the option is a valid.
     * 
     * @param options an Array of all posible options
     * @return a valid option String
     */
    public String readValidOption(String[] options){
        boolean optionIsInvalid = true;
        String option = null;

        while(optionIsInvalid){
            option = user.nextLine();
            for (String opt : options){
                if (option.equals(opt)){
                    optionIsInvalid = false;
                    break;
                }
            }
            if(optionIsInvalid){
                System.out.println("Please select a valid option:");
            }
        }
        return option;
    }

    /**
     * The main methods that runs the app
     * It accepts the host as first argument
     * and port number as second argument
     * 
     * @param args An array of options
     */
    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 54321;
        
        BattleShipsClient client = new BattleShipsClient(host, port);
        client.run();
    }
}