package server;

import util.Command;

/**
* Server protocol for validating all recived and sent commants to the client
* 
* @author ioannis
* 
*/
public class Protocol {

    Command lastCommand ;

    public Protocol() {
    }

    public void setLastCommand(Command command) { this.lastCommand = command; }

    public boolean isValidCommand(String command){

        Command c = null;
        
        try {
            c = new Command(command);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (c != null && isSameType(c)){

            //If its join command
            if (c.type().equals("join") && checkJoinCommand(c)){
                return true;
            }
            //If its possition command
            else if (c.type().equals("position") && checkPositionCommand(c)) {
                return true;
            }
            //If its fire  command
            else if (c.type().equals("fire") && checkFireCommand(c)){
                return true; 
            }
            else if (c.type().equals("gameover")){
                return true; 
            }            
            return false;
        }
        else{
        	return false;
        }
    }
    
    private boolean isSameType(Command command) {
        return lastCommand.type().equals(command.type());
    }
    
    private boolean checkJoinCommand (Command command){
        return command.hasKey("as");
    }

    private boolean checkPositionCommand (Command command){
        return (command.hasKey("ship") && command.hasKey("location") && command.hasKey("orientation"));
    }

    private boolean checkFireCommand (Command command){
        return command.hasKey("location");
    }
}
