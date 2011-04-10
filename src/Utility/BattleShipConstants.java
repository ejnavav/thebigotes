package Utility;
import java.awt.*;
public interface BattleShipConstants {
   public static int BOMBED_AT = 1;
   public static int RESPONSE = 2;
   public static int SHIP_HIT = 3;
   public static int SHIP_DESTROYED = 4; 
   public static int ALL_SHIPS_DESTROYED = 5;
   public static int RESPONSE_END = 6;
   public static int PLAYER1 = 1; // indicate player 1
   public static int PLAYER2 = 2; // indicate player 2
   public static String shipTypes[]= {"Mine Sweeper", "Submarine","Destroyr","Warship","Aircraft Carrier"};  
   public static Color shipColors[]= {Color.red, Color.blue,Color.yellow,Color.cyan, Color.green};  
}
