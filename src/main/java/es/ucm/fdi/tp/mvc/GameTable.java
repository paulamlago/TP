package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */


public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

	boolean finished;
	S state;
	S initialState; //only in case we want to restart the game!!
	List<GameObserver<S,A>> players;
	
	
    public GameTable(S initState) { //initialize all fields
        state = initState;
        initialState = initState;
        finished = false;
        players = new ArrayList<GameObserver<S,A>>();
    }
    
    public void start() {
    	finished = false;
    	state = initialState;
        notifyGameStart();
    }
    
    public void stop() {
    	finished = true;
    	notifyGameStop();
    }
       
    public void execute(A action){
    	
    	try{
    		if (action.getPlayerNumber() == state.getTurn() && !isFinished()){
    			
    			state = action.applyTo(state);
        		
    			System.out.println("After action:\n" + state);
        		
        		if (isFinished()) {
        			String endText = "The game ended: ";
        			int winner = state.getWinner();
        			if (winner == -1) {
        				endText += "draw!";
        			} else {
        				endText += "player " + winner + " won!";
        			}
        			
        			finished = true;
        			
        			notifyMove(action);
        			notifyGameOver(endText);
        		}
        		else notifyMove(action);
    		}
    	}
    	catch(GameError e){
    		notifyError(e);
    		throw e;
    	}
		
    }
    
    protected void notifyGameStart(){
    	for(GameObserver<S, A> o: players){
    		GameEvent<S, A> a = new GameEvent<S,A>(EventType.Start, null, state, null, "The game has started, and it's player "+ state.getTurn() + " turn!");
    		o.notifyEvent(a);
    	}
    }
    
    protected void notifyGameStop(){
    	for(GameObserver<S, A> o: players){
    		GameEvent<S, A> a = new GameEvent<S,A>(EventType.Stop, null, state, null, "The game has ended!");
    		o.notifyEvent(a);
    	}
    }
    
    protected void notifyError(GameError error){
    	for(GameObserver<S, A> o: players){
    		GameEvent<S, A> a = new GameEvent<S,A>(EventType.Error, null, state, error , error.getMessage());
    		o.notifyEvent(a);
    	}
    }
    
    protected void notifyMove(A ac){
    	for(GameObserver<S, A> o: players){
    		GameEvent<S, A> a = new GameEvent<S,A>(EventType.Change, ac, state, null, "The movement has been correctly executed!");
    		o.notifyEvent(a);
    	}
    }
    
    protected void notifyGameOver(String text){
    	for(GameObserver<S, A> o: players){
    		GameEvent<S, A> a = new GameEvent<S,A>(EventType.Stop, null, state, null, text);
    		o.notifyEvent(a);
    	}
    }
    
    public S getState() {
		return state;
    }

    public void addObserver(GameObserver<S, A> o) {
       players.add(o);
    }
    
    public void removeObserver(GameObserver<S, A> o) {
        players.remove(o);
    }
    
   public boolean isFinished(){
    	return finished || state.isFinished();
    }
    
    public boolean isActive(){
    	return !isFinished(); //returns true if the game is active (not finished or stopped)
    }
    
    public void bomba(){
    	state.bomba();
    }
	
	public void dBomba(int n) {
		state.dBomba(n);
	}
}
