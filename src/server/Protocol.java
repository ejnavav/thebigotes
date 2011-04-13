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
 
     /*    
     --------------------------------
    1.COMMAND RULES
    
    JOIN Must have
    command:join
    as:
    --------------------------------
    FIRE Must have
    command:fire
    location:
    --------------------------------    
    POSITION Must have
    command:position
    ship:
    location:
    orientation
    --------------------------------     
     --------------------------------
     2.STATE RULES
    1.join
    2.possition
    3.fire
    5.end
    */
     
     
    public Protocol() {
    }

    public void setLastCommand(Command command) {this.lastCommand=command;}
    
    public boolean isSameType(Command command) {
        if(lastCommand.type().equals(command.type())){
            return true;
        } return false;

      }

    public boolean isValidCommand (String command){
        
        Command c =null;
        try {
            c = new Command(command);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        if (c !=null && isSameType(c)){
            
            //If its join command
            if (c.type().equals("join")){
                if (checkJoinCommand(c))return true;}
                
            //If its possition command
            else if (c.type().equals("position"))     {
                if (checkPositionCommand(c))return true;}
            
            //If its fire  command
            else if (c.type().equals("fire"))     {
                if (checkFireCommand(c))return true;}
            }
                    
        return false;
        }

public boolean checkJoinCommand (Command command){
    	return command.hasKey("as");
}

public boolean checkPositionCommand (Command command){
	return (command.hasKey("ship") && command.hasKey("location") && command.hasKey("orientation") );
}
    

public boolean checkFireCommand (Command command){
	return command.hasKey("location");
}
    
//TODO:we need this?
    public boolean checkState (Command command){
        if (command.type().equals("join") && lastCommand.type().equals("position") ){
            return true;
        }
        else if     (command.type().equals("position") 
                && lastCommand.type().equals("fire")) { return true;}
        
    return false;
}

}
