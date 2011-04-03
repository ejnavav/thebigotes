package server;
public class BattleShipsLogic {
	
	static public final String player1 = "p1";
	static public final String player2 = "p2";
	static int gridSize = 36;
	
	public static boolean canFire(Player hasTurn, Player wantsToFire){
		return hasTurn == wantsToFire;
	}
	
	//battleship
	// public static boolean canPositionShip(Ship ship, String board String loc, String orientation){
	// 	// TODO implement
	// 	return true;
	// }
}

// BattleShipLogic.canPositionShip()


	