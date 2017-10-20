package es.ucm.fdi.tp.view.panelComponents;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GUIView;

public abstract class MessageViewer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void setMessageViewer(MessageViewer<S,A> infoViewer) { 
		//setMessageViewer is empty by
		//default since a MessageViewer
		//component is not connected to
		//another MessageViewer usually.
	}
	
	abstract public void addContent(String msg);
	abstract public void setContent(String msg);
}
