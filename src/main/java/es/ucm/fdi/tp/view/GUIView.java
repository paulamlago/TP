package es.ucm.fdi.tp.view;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;


public abstract class GUIView<S extends GameState<S,A>, A extends GameAction<S,A>> extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JFrame window;
	protected GameState<S,A> gameState;
	
	public abstract void enable();
	public abstract void disable();
	public abstract void update(S state);
	public abstract void setMessageViewer(MessageViewer<S,A> infoViewer);
	public abstract void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer);
	public abstract void setGameController(GameController<S, A> gameCtrl);

	public void enableWindowMode(int x) {
		
		if (window == null){
			window = new JFrame("Player " + x);
			window.setContentPane(this);
		} 
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	public void disableWindowMode() {
		if (this.window != null) window.dispose();
		
		window = null;
	}
		
	public JFrame getWindow() {
		return window;
	}
		
	public void setTitle(String title) {
		if (window != null) {
			window.setTitle(title);
		} 
		else {
			this.setBorder(BorderFactory.createTitledBorder(title));
		}
	}
	
}
