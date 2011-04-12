package client;
import java.util.*;
import util.*;
import server.*;
import java.io.*;

public class BattleShipsClient {
    private Communicator server = null;
    
    private Scanner user = new Scanner(System.in);

    public BattleShipsClient(String host, int port){
        server = new Communicator(host, port);
    }
    
    public void run(){
        System.out.println("Welcome to Battleships");
        System.out.println("Connecting to server...");
        if (server.connect() == false){
            System.err.println("Couldn't connect, please try again");
            System.exit(0);
        }
        System.out.println("Connected");
        
        while(true){
            System.out.println("Waiting for command...");
            Command command = waitForCommand();
            if (command == null){
                break;
            }
            System.out.println("Command received: "+command.toString());
            handleCommand(command);
        }
    }
    
    // public InputStream setTestImputStream(){
    //     
    // }
    
    public boolean connect(){
        return server.connect();
    }
    
	public void sendMessage(String message){
	    server.sendMessage(message);
	}
    
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

    public void handleCommand(Command command){
        System.out.println("handling command");

        if (command.type().equals("join")) {
            join(command);
        }
        else if  (command.type().equals("position")) {
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
        else {
            System.out.println("Don't know about command" +command.toString());
        }
        System.out.println("done handling command");
    }

    public void join(Command command){
        System.out.println(command.get("message"));
        String[] options = command.get("options").split(",");
        String option = readValidOption(options);
        Command reply = new Command();
        reply.put("command", "join");
        reply.put("as", option);
        server.sendMessage(reply.toString());
    }
    
    public void position(Command command){
        drawGrid(command.get("board1"), command.get("board2"));
        System.out.println(command.get("message"));
        // Board board = new Board(command.get("board1"));

        boolean optionIsInvalid = true;

        String location = null;
        String orientation = null;

        //TODO validations
        // while(true) {
            System.out.println("Enter location:");
            location = user.nextLine();
            System.out.println("Enter orientation h or v:");
            orientation = user.nextLine();

            // try {
            //                 board.placeShip(command.get("ship"), location, orientation);
            //                 break;
            //             } catch (Exception e) {
            //                 System.out.println("Invalid location or orientation.");
            //             }
        // }

        Command reply = new Command();
        reply.put("command", command.type() );
        reply.put("ship", command.get("ship"));
        reply.put("location", location);
        reply.put("orientation", orientation);
        System.out.println("sending to server: "+ reply.toString());
        server.sendMessage(reply.toString());
    }
        
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
    
    public void draw(Command command){
        String board1 = command.get("board1");
        String board2 = command.get("board2");
        drawGrid(board1, board2);
        if(command.get("message") != null){
            System.out.println(command.get("message"));
        }
    }
    
    public void fire(Command command){
        drawGrid(command.get("board1"), command.get("board2"));
        System.out.println(command.get("message"));
        // Board board = new Board(command.get("board2"));

        String location = null;

        // while(true) {
        System.out.println("Enter location:");
        location = user.nextLine();
        // try {
        //            board.fire(location);
        //            break;
        //        } catch (Exception e) {
        //            System.out.println("Invalid location, try again.");
        //        }
        //    }

        Command reply = new Command();
        reply.put("command", command.type() );
        reply.put("location", location);
        System.out.println("sending to server: "+ reply.toString());
        server.sendMessage(reply.toString());
    }
    
    public void wait(Command command){
        System.out.println(command.get("message"));
    }
    
    
    public static void drawGrid(String player1,String player2){
        player1 = player1.replace('#',' ');
        player2 = player2.replace('#',' ');
        char letter = 'A';
        String printString="   ";
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
        System.out.println(printString);
    }

    public static void main(String[] args) {
         String host = "localhost";
        // String host = "10.1.1.3";
        int port = 54321;
        BattleShipsClient client = new BattleShipsClient(host, port);
        client.run();
    }
}