package es.ucm.fdi.tp;

import static org.junit.Assert.assertEquals;

import java.util.List;


import org.junit.Test;


import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WolfAndSheepStateTest {
	
	@Test
	public void trappedWolf(){ //si los movimientos del lobo son 0 tienen que ganar las ovejas
		WolfAndSheepState was = new WolfAndSheepState(8);
		//colocamos a las ovejas al rededor del lobo para atraparle.
		
		was = new WolfAndSheepState(was, was.getBoard(), was.isFinished(), was.getWinner());
		WolfAndSheepAction ac = new WolfAndSheepAction(1, 6, 1, 0, 1); //colocamos una oveja en la posición 6,1 y el lobo ya no se puede mover
		was = ac.applyTo(was);
		
		assertEquals("The winner once the wolf is trapped should be the sheeps!", 1, was.getWinner());
	}
	
	@Test
	public void WolfWon(){
		WolfAndSheepState was = new WolfAndSheepState(8);
		WolfAndSheepAction ac = new WolfAndSheepAction(0, 0, 0, 7, 0); //colocamos al lobo en la primera celda
		
		was = ac.applyTo(was);
		assertEquals("The winner once the wolf is in the row 0 should be the wolf!", 0, was.getWinner());
	}
	
	@Test
	public void ChechValidAcctions(){
		WolfAndSheepState was = new WolfAndSheepState(8);
		
		List<WolfAndSheepAction> valid = was.validActions(0);
		assertEquals("The valid actions should be just 1" , 1, valid.size());
		
		WolfAndSheepAction ac = new WolfAndSheepAction(0, 6, 1, 7, 0); //we place the wolf in the position 6, 1
		was = ac.applyTo(was);
		valid = was.validActions(0);
		assertEquals("The valid actions should be 4" , 4, valid.size());
	}
	@Test
	public void CheckSheepActions(){
		WolfAndSheepState was = new WolfAndSheepState(8);

		//we leave only the first sheep (placed in 0,1 and erease the other ones)
		
		int i = 2;
		int[][] b = was.getBoard();
		while(i < was.getDim()){
			b[0][i] = -1;
			i++;
		}
		was = new WolfAndSheepState(was, b, was.isFinished(), was.getWinner());
		
		List<WolfAndSheepAction> valid = was.validActions(1);
		assertEquals("The valid actions should be 2" , 2, valid.size());
		
		WolfAndSheepAction ac = new WolfAndSheepAction(1, 0, 7, 0, 1); //we place the wolf in the position 6, 1
		was = ac.applyTo(was);

		valid = was.validActions(1);
		assertEquals("The valid actions should be just 1" , 1, valid.size());
	}
}
