package client;
import java.util.*;
import util.*;

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

		//FIXME should stop at some point

		while(true){
			System.out.println("Waiting for command...");
			String commandStr = server.waitForMessage();
			System.out.println("Command received: "+commandStr);
			handleCommand(commandStr);
		}
	}

	public void handleCommand(String commandStr){
		System.out.println("handling command");
		Command command = new Command(commandStr);

		// TODO command.is("join")
		if (command.type().equals("join")) {
			join(command);
		}
		// else if (commandType.equals("position")){ position(); }
		// else { dontKnow(); }
		System.out.println("done handling command");
	}

	public HashMap<String, String> parseCommand(String commandStr){
		HashMap<String, String> command = new HashMap<String, String>();
		String[] sections = commandStr.split("&");

		for(String keyVal : sections){
			String[] pair = keyVal.split(":");
			command.put(pair[0], pair[1]);
		}
		return command;
	}

	public void join(Command command){
		String msg = command.get("message");
		String[] options = command.get("options").split(",");
		System.out.println(msg);
		
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
				System.out.println("Please select a valid option.");
			}
		}
		Command reply = new Command();
		reply.put("command", "join");
		reply.put("as", option);
		server.sendMessage(reply.toString());
	}

	public void position(){
		// TODO implement
		System.out.println("position()");
	}

	public void dontKnow(){
		// TODO implement
		System.out.println("dontKnow()");
	}

	public static void drawGrid(String player1,String player2){
		player1 = player1.replace('_',' ');
		player2 = player2.replace('_',' ');
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
//		String host = "localhost";
		String host = "10.1.1.6";
		int port = 54321;
		BattleShipsClient client = new BattleShipsClient(host, port);
		client.run();
	}
}