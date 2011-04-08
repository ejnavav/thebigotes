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
        else if  (command.type().equals("position")) {
            position(command);
        }
        else if  (command.type().equals("draw")) {
            draw(command);
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
        draw(command);
        System.out.println(command.get("message"));
        String[] options = command.get("options").split(",");
        
        // TODO maybe move to command class
        //convert command:position&message:position your submarine&options:v@b2,h@b2,h@b3
        //to somehting like this:
        //(json notation) { b2 : [h, v], b3: [h] }
        HashMap <String, ArrayList<String>> positions = new HashMap <String, ArrayList<String>>();
                
        for(String item : options){
            String orientation = item.split("@")[0];
            String location = item.split("@")[1];
            ArrayList<String> orientations = new ArrayList<String>();
            
            if(positions.containsKey(location)){
                orientations = positions.get(location);
            }
            orientations.add(orientation);
            positions.put(location, orientations);
        }
        
        System.out.println("Enter location:");
        // System.out.println("Valid Options: "+positions.keySet().toArray().toString());
        
        String[] locs = positions.keySet().toArray(new String[positions.size()]);

        String location = readValidOption(locs);
        
        System.out.println("Enter orientation: ");
        // System.out.println("Valid Options: " + positions.get(location).toArray().toString());
        String[] os = positions.get(location).toArray(new String[positions.size()]);
        String orientation = readValidOption(os);
        
        Command reply = new Command();
        reply.put("command", command.type());
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
    }

    public void dontKnow(Command command){
        
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
        // String host = "localhost";
        String host = "10.1.1.6";
        int port = 54321;
        BattleShipsClient client = new BattleShipsClient(host, port);
        client.run();
    }
}