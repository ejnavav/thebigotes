package test;

import org.junit.* ;
import static org.junit.Assert.* ;
import server.*;

public class BattleShipsLogicTest {
	
	@Test
	public void player_with_turn_can_fire(){
		Player p1 = new Player(1);
		assertTrue("Given Player "+p1.number()+" has turn, it can fire", BattleShipsLogic.canFire(p1, p1));
	}

	@Test
	public void player_without_turn_can_not_Fire(){	
		Player p1 = new Player(1);
		Player p2 = new Player(2);
		String msg = "Given Player "+p1.number()+" has turn, Player "+p2.number()+" can't fire";
		assertFalse(msg, BattleShipsLogic.canFire(p1, p2));
	}
	
	// @Test
	// public void can_position_ship_inside_board(){
	// 	assertFalse("Given "+p1+"has turn, "+p2+" can't fire", BattleShipLogic.canFire(p1, p2));
	// }
	
	// public void positionShipOutsideBoard(){
	// 	assertFalse("Given "+p1+"has turn, "+p2+" can't fire", BattleShipLogic.canFire(p1, p2));
	// 	assertFalse
	// }
}