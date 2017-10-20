package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.ControlPanel;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.MessageViewerComp;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.PlayersInfoComp;

public class GameContainer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A> implements GameObserver<S, A> {

	//THIS IS THE ONLY OBSERVER AND THEN THIS CORDINATES ALL THE COMPONENTS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUIView<S, A> gameView;
	private MessageViewer<S, A> messageViewer;
	private PlayersInfoViewer<S, A> playersInfoViewer;
	private ControlPanel<S, A> controlPanel;
	private GameController<S, A> gameCtrl;

	public GameContainer(GUIView<S, A> gameView, GameController<S, A> gameCtrl, GameObservable<S, A> game) {
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		this.gameState = gameView.gameState;
		
		initGUI();
		
		game.addObserver(this);
	}
	
	private void initGUI() {
			
		//1. We create a messageViewer Component
		
		messageViewer = new MessageViewerComp<S, A>();
		playersInfoViewer = new PlayersInfoComp<S, A>(gameView.gameState.getPlayerCount());
		controlPanel = new ControlPanel<S, A>(gameCtrl);
		


		this.setLayout(new BorderLayout(5, 5));
		
		//now we place all the components
		//the control Panel at the PAGE_START
		
		this.add(controlPanel, BorderLayout.PAGE_START);

		//the gameView at the CENTER
		this.add(gameView, BorderLayout.CENTER);
	
		//a new JPanel with BoxLayout at the LINE_END
		JPanel mp = new JPanel();
		mp.setLayout(new BoxLayout(mp, BoxLayout.Y_AXIS));
		
		//and we put on it the messageViewer and the playersinfoVw
		mp.add(messageViewer, BorderLayout.LINE_START);
		mp.add(playersInfoViewer, BorderLayout.LINE_END);
		this.add(mp, BorderLayout.LINE_END);
		
		gameView.setMessageViewer(messageViewer);
		controlPanel.setMessageViewer(messageViewer);
		gameView.setPlayersInfoViewer(playersInfoViewer);
		gameView.setGameController(gameCtrl);
	}
	
	@Override
	public void notifyEvent(final GameEvent<S, A> e) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				handleEvent(e); 
				} });
	}

	public void handleEvent(final GameEvent<S, A> e) {
		
		switch ( e.getType() ) {
		
			case Start:
				enable();
				initialize();
				update(e.getState());
				messageViewer.setContent(e.toString());
				break;
			case Change:
				update(e.getState());
				messageViewer.addContent(e.toString());
				break;
			case Stop:
				disable();
				messageViewer.addContent(e.toString());
				break;
			case Error:
				messageViewer.addContent(e.toString());
				break;
			case Info:
				messageViewer.addContent(e.toString());
				break;
			default:
				break;
		}
		
		
		
		if (e != null){ //if there's an event
			SwingUtilities.invokeLater(new Runnable() { 
				public void run() { 
					gameCtrl.handleEvent(e); 
				}
			});
		}
		
		}
	
	@Override
	public void enable() {
		gameView.enable();
		if (gameView.gameState.getTurn() == gameCtrl.getPlayerId()) controlPanel.enable(); // if it's the players turn we enable it's control panel
	}

	@Override
	public void disable() {
		
		gameView.disable();
		controlPanel.disable();
	}

	@Override
	public void update(S state) {
		gameView.update(state);
		controlPanel.update(state);
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		gameView.setMessageViewer(messageViewer);
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		gameView.setPlayersInfoViewer(playersInfoViewer);
	}

	@Override
	public void setGameController(GameController<S, A> gameCtrl) {
		gameView.setGameController(gameCtrl);
	}

	public void initialize(){
		gameView.setTitle(gameView.gameState.getGameDescription());
	}
}
