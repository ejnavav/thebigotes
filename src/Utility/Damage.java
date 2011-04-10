package Utility;
import java.io.*;
import java.util.*;

/* This class helps to manage the interaction between client and server
*/

public class Damage implements BattleShipConstants
{
   /* This form of constructor allows the damage to own ships to be assessed
      and creates a packet conating an array of values using specific format.   */

   public Damage(NavalForce nf, int loc)
   {
      Ship s;
      if ( (s = nf.destroy(loc)) != null)
      {
         shipHit = true;
         resp.add(new Integer(SHIP_HIT));
         resp.add(new Integer(loc));
         if ( s.getNumRemainingLocs() == 0)
         {
            shipDestroyed = true;
            ship = s;
            resp.add(new Integer((int)SHIP_DESTROYED));
            resp.add(new Integer(s.size()));
            resp.add(new Integer(s.orientation()));
            resp.add(new Integer(s.topLeftPosition()));
            if ( nf.getTotalRemainingLocs() == 0) {
                allShipsDestroyed = true;
                resp.add(new Integer((int)ALL_SHIPS_DESTROYED));
            }
         }
      }       
   }

   /* The second form of the constructor reads the damage from opponent (predefined format)
	Once constructed the accessor methods provides the extent and type of damage
   */

   public Damage(DataInputStream fromServer)
   {
      try {
         if ( fromServer.readInt() == SHIP_HIT)
         {
            shipHit = true;
            pos = fromServer.readInt();
            if ( fromServer.readInt() == SHIP_DESTROYED)
            {
               shipDestroyed = true;
               int size = fromServer.readInt();
               int orientation = fromServer.readInt();
               int topLeft = fromServer.readInt();
               ship = new Ship(size,orientation,topLeft);
               if ( fromServer.readInt() == ALL_SHIPS_DESTROYED)
               {
                   allShipsDestroyed = true;
               }   
            }
         }
      }
      catch(Exception ex)
      {
          System.out.println(ex);
      }         
   }

   /* This method sends the packet formed by the first constructor
      to the peer 
   */

   public void send(DataOutputStream toServer) throws IOException
   {
      toServer.writeInt(RESPONSE);
      for (int i=0; i<resp.size(); i++)
         toServer.writeInt(((Integer)resp.get(i)).intValue());               
      toServer.writeInt(RESPONSE_END);
   }

   public ArrayList getResponse() { return resp; } 
   public boolean areAllShipsDestroyed() { return allShipsDestroyed; }
   public boolean isShipHit() { return shipHit; }
   public boolean isShipDestroyed() { return shipDestroyed; }  
   public Ship getShipDestroyed() { return ship; }
   public int getPositionHit() { return pos; }

   // This packet keeps an array of damage information (packet) to be sent
   private boolean shipHit = false;   private ArrayList resp = new ArrayList(); 
   private Ship ship = null;
   private boolean shipDestroyed = false; 
   private boolean allShipsDestroyed = false;
   private int pos = -1;
}