package es.ucm.fdi.tp.view;

import java.util.concurrent.Future;

import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.SmartMovesPanel.SmartMoveObserver;


public interface GameController <S extends GameState<S,A>, A extends GameAction<S,A>>{
	public void makeManualMove(A a);
	public void makeRandomMove();
	public Future<?> makeSmartMove();
	public void restratGame();
	public void stopGame();
	public void changePlayerMode(PlayerMode p);
	public void handleEvent(GameEvent<S, A> e);
	public PlayerMode getPlayerMode();
	public int getPlayerId();
	public void smartPlayerConcurrencyLevel(int nThreads);
	public void smartPlayerTimeLimit(int nThreads);
	public void stopSmartPlayer();
	public void addSmartPlayerObserver(SmartMoveObserver o);
	public void dBomba(int n);
	public void bomba();
	
}
