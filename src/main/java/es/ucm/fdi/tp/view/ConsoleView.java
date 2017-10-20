package es.ucm.fdi.tp.view;


import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView<S extends GameState<S,A>, A extends GameAction<S,A>> implements GameObserver<S,A>{
	//
	public ConsoleView(GameObservable<S,A> gameTable) {
		gameTable.addObserver(this);
	}

	public void notifyEvent(GameEvent<S, A> e) {
		System.out.println(e); //Just the description
		if (e.getAction() != null){
			System.out.println("The executed action was: " + e.getAction());
		}
		else if (e.getError() != null){
			System.out.println("The throwed error was: " + e.getError());
		}
	}
	
	public void pritState(S state){
		System.out.println(state);
	}
}