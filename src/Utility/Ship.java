package Utility;
import java.util.*;

/* This class keeps track of the ship orientation, size, position 
   as well as the remaining undamaged location the ship occpies. The 
   location of the ship varies from 1 to 100 */

public class Ship
{
   private int size;
   private int orientation; // 0 = horozontal   1 = vertical   
   private int topLeftPosition;
   private ArrayList remainLocs = new ArrayList();
   public int size() { return size; }
   public int getNumRemainingLocs() { return remainLocs.size();    }
   public int topLeftPosition() { return topLeftPosition; }
   public int orientation() { return orientation; }

   // Ship constructor - throws execption if ship falls outside square of 10 x 10 
   // if ship orientation vertical cell numbers are incremented by 10 and 1 otherwise
   public Ship(int size, int orientation, int topLeftPosition) throws ShipException
   { 
      this.size = size;
      this.orientation = orientation;
      this.topLeftPosition = topLeftPosition;   
      int inc = orientation == 0 ? 1:10; 
      if (orientation == 1 && (topLeftPosition-1)/10 + size > 10 ||
          orientation == 0 && (topLeftPosition-1)%10 + size > 10)
             throw new ShipException("Invalid Dimensions");         
      for (int i=0; i<size; i++)
         remainLocs.add(new Integer(topLeftPosition + inc * i));
   }

   // returns all ship locations both damaged and undamaged
   ArrayList getAllLocs()
   {
      ArrayList list = new ArrayList();
      int curr = topLeftPosition;
      int inc = orientation == 0 ? 1 : 10;
      for ( int i=0; i<size; i++)
      {
         list.add(new Integer(curr));
         curr += inc;
      }
      return list;
   }

   // updates the remaining (undamaged) locations 
   // returns true if undamaged part of the ship is hit 
   boolean destroy(int loc)
   {
      int index = remainLocs.indexOf(new Integer(loc));
      if (index == -1)
         return false;
      remainLocs.remove(index);
      return true;
   }

   // returns true if another ship overlaps
   boolean intersects(Ship s)
   {
      ArrayList allThis = getAllLocs();
      ArrayList allOther = s.getAllLocs();
        
      for ( int i=0; i<size; i++)
         for ( int j=0; j < allOther.size(); j++)
            if ( allThis.get(i).equals(allOther.get(j)))
               return true;   
      return false;  
   } 

   // returns true if sepcified location is undamaged part of ship 
   public boolean contains(int loc)
   {
       if (remainLocs.indexOf(new Integer(loc)) == -1)
          return false;
       return true;
   }
}