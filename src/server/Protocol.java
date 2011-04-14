package server;

import util.Command;

/**
* Server protocol for validating all commands received from the client
* 
* @author John Kolovos
* 
*/
public class Protocol {

    Command lastCommand ;

    public Protocol() {
    }

    /**
     * Sets the last command to this command
     * 
     * @param command
     */
    public void setLastCommand(String commandStr) {
    	this.lastCommand = new Command(commandStr);
    }
    
    /**
     * Check if a command received by client is valid
     * 
     * @param command
     * @return
     */
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
    
    /**
     * Check if the command to test is the same as the last command sent 
     * 
     * @param command
     * @return true if is the same fale otherwise
     */
    private boolean isSameType(Command command) {
        return lastCommand.type().equals(command.type());
    }
    
    /**
     * Checks if the join command is valid
     * 
     * @param command
     * @return true if valid false otherwise
     */
    private boolean checkJoinCommand (Command command){
        return command.hasKey("as");
    }

    /**
     * Checks if the position commmand is valid
     * 
     * @param command
     * @return true if valid false otherwise
     */
    private boolean checkPositionCommand (Command command){
        return (command.hasKey("ship") && command.hasKey("location") && command.hasKey("orientation"));
    }

    /**
     * Check if the fire command is valid
     * 
     * @param command
     * @return true
     */
    private boolean checkFireCommand (Command command){
        return command.hasKey("location");
    }
}
