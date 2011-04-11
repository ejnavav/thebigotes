package Utility;
import java.util.*;
import java.io.*;

/* Naval Force contains and array of ships 
   Class to manage of fleet of ships
*/

public class NavalForce extends ArrayList implements BattleShipConstants
{
   // creates a specified number of non-overlapping ships
   // creates (numTypes * count) ships in all
   public void createRandom(int numTypes, int count)
   {
      for (int i=1; i<=numTypes; i++)
         for (int j=1; j<=count; j++)
         {
            boolean okay;
            do {
               okay = false;
               try {
                  int orientation = (int) (Math.random() * 2);
                  int topLeft = (int) (Math.random() * 100) + 1;
                  add(new Ship(i,orientation,topLeft));
                  okay = true;
               }
               catch ( ShipException se)
               {
               }
            } while (!okay); 
         }
   }   
   public boolean add(Ship s) throws ShipException
   {
      for (int i=0; i<size(); i++)
         if ( ((Ship) get(i)).intersects(s) )
            throw new ShipException("Ship overlaps");     
      super.add(s);  
      return true;
   }
   // returns the remaining undamaged locations of the naval force
   public int getTotalRemainingLocs()
   {
      int total = 0;
      for (int i=0; i<size(); i++)
            total += ((Ship)get(i)).getNumRemainingLocs();
      return total;
   }

   // one of the ships in the naval force is hit
   public boolean isHit(int loc)
   {
      for (int i=0; i<size(); i++)
         if ( ((Ship)get(i)).contains(loc) )
            return true;     
      return false;
   } 

   public Ship getShipHit(int loc)
   {
      for (int i=0; i<size(); i++)
         if ( ((Ship)get(i)).contains(loc) )
            return (Ship) get(i);     
      return null;
   }

   // destroys part of the ship at given location
   // returns the Ship affected if any
   public Ship destroy(int loc)
   {
      Ship s = getShipHit(loc);
      if ( s != null )
         s.destroy(loc);
      return s;
   }
   // returns the ship that will be fully destroyed by the bomb at loc
   // returns the Ship affected if any
   public Ship getShipDestroyed(int loc)
   {
      Ship s = getShipHit(loc);
      if (s.getNumRemainingLocs() == 1)
         return s;
      return null;
   }
}