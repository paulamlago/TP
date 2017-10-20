package es.ucm.fdi.tp.view.panelComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GUIView;

public abstract class PlayersInfoViewer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1202384818165996382L;
	
	protected List<PlayersInfoObserver> observers;
	protected Color[] playerColor;
	
	public interface PlayersInfoObserver {
		public void colorChanged(int p, Color color);
	}
	
	public PlayersInfoViewer(){
		observers = new ArrayList<>();
	}
	
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) { 
		//setPlayersInfoViewer is empty by
		//default since a PlayersInfoViewer
		//component is not connected to
		//another PlayersInfoViewer
		//usually.
	}
	
	abstract public void setNumberOfPlayer(int n);
	abstract public Color getPlayerColor(int p);
	
	public void setPlayerColor(int p, Color c){
		playerColor[p] = c;
	}
	
	public void addObserver(PlayersInfoObserver o) { observers.add(o); }
	
	public void notifyObservers(int p, Color c) {
		for (PlayersInfoObserver o : observers) {
			o.colorChanged(p, c);
		}
	}
}
