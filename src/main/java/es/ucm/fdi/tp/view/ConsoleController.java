package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {
	public List<GamePlayer> players;
	public GameTable<S,A> game;
	
	public ConsoleController(List<GamePlayer> players, GameTable<S,A> game) {
		this.players = players;
		this.game = game;
	}
	
	public void run() {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		
		game.start();
		
		S currentState = (S) game.getState();

		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);

			// apply move
			game.execute(action);
			currentState = game.getState();
		}
	}
}
