package es.ucm.fdi.tp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.SmartMovesPanel.SmartMoveObserver;

public class GUIController<S extends GameState<S,A>, A extends GameAction<S,A>> implements GameController<S, A> {

	volatile private GameTable<S, A> game;
	volatile private RandomPlayer randPlayer;
	volatile private ConcurrentAiPlayer smartPlayer;
	volatile private int playerId;
	volatile private PlayerMode playerMode;
	volatile private ExecutorService executor;
	volatile private  Future<?> smartPlayerFuture;
	volatile private List<SmartMoveObserver> observers;
	
	
	public GUIController(int playerId, GamePlayer randPlayer, ConcurrentAiPlayer smartPlayer, GameTable<S, A> game, ExecutorService executor) {
		this.playerMode = PlayerMode.manual;
		this.playerId = playerId;
		this.randPlayer = (RandomPlayer) randPlayer;
		this.smartPlayer = smartPlayer;
		this.game = game; 
		this.executor = executor;
		this.observers = new ArrayList<>();
	}


	public void makeManualMove(A a){
		try{
			game.execute(a);
		}
		catch(IllegalArgumentException e){
			
		}
	}

	public void makeRandomMove() {
		//if it's the turn of the player who owns this controller:
		try{
			if (playerId == game.getState().getTurn()){
				game.execute(randPlayer.requestAction(game.getState()));
			}
		}
		catch(IllegalArgumentException e){
			
		}
	}

	public Future<?> makeSmartMove() {
		 if (playerId == game.getState().getTurn() && gameIsActive()) {
			 smartPlayerFuture =  executor.submit(new Runnable() { 
				 @Override 
				 public void run() {
					 long time1, time2;
					 
					 for (SmartMoveObserver o: observers){
						 o.onStart();
					 }
					 
					 time1 = System.currentTimeMillis();
					 A Action = smartPlayer.requestAction(game.getState());
					 time2 = System.currentTimeMillis();
					 
					 if (Action == null){
						 changePlayerMode(PlayerMode.manual);
					 }
					 else {
						
						 try{
							 game.execute(Action);

							 for (SmartMoveObserver o: observers){
								 o.onEnd(true, time2 - time1, smartPlayer.getEvaluationCount(), smartPlayer.getValue());
							 }
						 }
						 catch(IllegalArgumentException e){
							 for (SmartMoveObserver o: observers){
								 o.onEnd(false, time2 - time1, smartPlayer.getEvaluationCount(), smartPlayer.getValue());
							 }
						 }
					 }
				}
		 });
			 return smartPlayerFuture;
		 } else {
			 return null;
		 }
		 
		}

	private void gameStart() { 
		Future<?> r = executor.submit(new Runnable() { @Override public void run() { 
			game.start(); 
			}
	 });
			
			try {
				r.get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} 
	
	}
	
	public void restratGame() {
		stopGame();
		
		gameStart();
	}

	public void stopGame() {
		Future<?> r = executor.submit(new Runnable() { @Override public void run() { 
			game.stop(); 
			}
	 });
		try {  r.get(); } catch (InterruptedException | ExecutionException e) { }
	
	}

	public void changePlayerMode(PlayerMode p) {
		//This method is called when
		//the user selects a new
		//player mode. When switching
		//from MANUAL to a non manual
		//mode you should call
		//decideMakeAutomaticMove().
		
		playerMode = p;
		if (playerMode != PlayerMode.manual) decideMakeAutomaicMove();
		
		
	}


	public void handleEvent(GameEvent<S, A> e) {
//		This method will be called by the view when
		//it gets a notification from the model, in such
		//case, if the event is INFO or CHANGE, and it
		//is the turn player of this controller, then call
		//decideMakeAutomaticMove(). It is important
		//to get the current turn from the state
		//e.getState() and not from game.getState()
		
		if (playerId == e.getState().getTurn() && !game.isFinished()){
			if (playerMode != PlayerMode.manual && (e.getType() == GameEvent.EventType.Info || e.getType() == GameEvent.EventType.Change)) {
				decideMakeAutomaicMove();
			}

			else if (e.getType() == GameEvent.EventType.Start){
				if (playerMode == PlayerMode.random) makeRandomMove();
				else if (playerMode == PlayerMode.smart) makeSmartMove();
			}
		}
	}


	public PlayerMode getPlayerMode() {
		return playerMode;
	}


	public int getPlayerId() {
		return playerId;
	}
	
	private void decideMakeAutomaicMove(){
		//The purpose of this private method is to make an automatic move if needed. It
		//first checks if the player mode is not
		//manual, and in such case it should call
		//makeRandomMove or makeSmartMove
		//depending on the mode
		
		if (playerMode != PlayerMode.manual){
			if (playerMode == PlayerMode.random) makeRandomMove();
			else makeSmartMove();
		}
	}


	@Override
	public void smartPlayerConcurrencyLevel(int nThreads) {
		smartPlayer.setMaxThreads(nThreads);
	}


	@Override
	public void smartPlayerTimeLimit(int nThreads) {
		smartPlayer.setTimeout(nThreads);
		
	}
	
	private boolean gameIsActive() { 
		Future<Boolean> r = executor.submit(
				new Callable<Boolean>() { 
					@Override public Boolean call() { 
						return game.isActive(); 
					}
		 });
		try { return r.get(); } catch (InterruptedException | ExecutionException e) { return false; }
	}


	@Override
	public void stopSmartPlayer() {
		smartPlayerFuture.cancel(true);
	}


	@Override
	public void addSmartPlayerObserver(SmartMoveObserver o) {
		observers.add(o);
	}
	
	
	public void bomba(){
		game.bomba();
	}


	@Override
	public void dBomba(int n) {
		game.dBomba(n);
	}
	

}


