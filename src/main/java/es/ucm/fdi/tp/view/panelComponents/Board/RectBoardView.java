package es.ucm.fdi.tp.view.panelComponents.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public abstract class RectBoardView<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A> implements PlayersInfoViewer.PlayersInfoObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected GameController<S, A> gameCtrl;
	protected MessageViewer<S,A> infoViewer;
	protected PlayersInfoViewer<S,A> playersInfoViewer; 
	protected S state;
	private JBoard jboard; //to call repaint
	protected int[][] board;
	
	protected boolean active;
	protected int numOfRows;
	protected int numOfCols;
	
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract Integer getPosition(int row, int col);
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	protected abstract void keyTyped(int keyCode);
	
	public RectBoardView(S gameState, GameController<S, A> gameCtrl, MessageViewer<S,A> infoViewer, PlayersInfoViewer<S,A> playersInfoViewer, int rows, int cols) {
		setGameController(gameCtrl);
		setMessageViewer(infoViewer);
		setPlayersInfoViewer(playersInfoViewer);
		numOfRows = rows;
		numOfCols = cols;
		
		initGUI();
		
		update(gameState);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	 }
	
	private void initGUI() { 
		
		createBoardData(numOfRows, numOfCols);

		jboard = new JBoard() { 
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				try{
					if (active){
						RectBoardView.this.mouseClicked(row, col, clickCount, mouseButton);
					}
					else infoViewer.addContent("Not your turn!");
				} catch(IllegalArgumentException e){
					infoViewer.addContent(e.getMessage());
				}
				
			}

			
			protected void keyTyped(int keyCode) {
				
				RectBoardView.this.keyTyped(keyCode);
				
			}

			
			protected Shape getShape(int player) {
				return RectBoardView.this.getShape(player);
			}

			
			protected Integer getPosition(int row, int col) {
				return RectBoardView.this.getPosition(row, col);
			}

			protected int getNumRows() {
				return numOfRows;
			}

			
			protected int getNumCols() {
				return numOfCols;
			}

			
			protected Color getColor(int player) {
				return RectBoardView.this.getPlayerColor(player);
			}

			@Override
			protected Color getBackground(int row, int col) {
				return RectBoardView.this.getBackground(row, col);
			}

			@Override
			protected int getSepPixels() {
				return 0; // put to 0 if you don't want a separator between
							// cells
			}


			@Override
			protected Image getIcon(int player) {
				return RectBoardView.this.getIcon(player);
			}
			
		};
		
		this.add(jboard, BorderLayout.CENTER); 
	 }

	private void createBoardData(int numOfRows, int numOfCols) {
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		board = new int[numOfRows][numOfCols];
		
		for (int i = 0; i < numOfRows; i++)
			for (int j = 0; j < numOfCols; j++) {
				board[i][j] = -1; //empty
			}
	}
	
	@Override
	public void colorChanged(int p, Color color) {
		infoViewer.addContent("You have changed the color of player " + p);
		playersInfoViewer.setPlayerColor(p, color);
		jboard.repaint();
	}
	
	public void setGameController(GameController<S, A> gameCtrl){
		this.gameCtrl = gameCtrl;
	}
	
	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer){
		this.playersInfoViewer = playersInfoViewer;
		this.playersInfoViewer.addObserver(this);
	}
	
	public void setMessageViewer(MessageViewer<S,A> infoViewer){
		this.infoViewer = infoViewer;
	}
	
	protected Shape getShape(int player) { 
		if (state.getGameDescription().toUpperCase().equalsIgnoreCase("CHESS")){
			if (ChessBoard.black((byte) player) || ChessBoard.white((byte) player)){
				return Shape.CIRCLE;
			}
			else return null;
		}
		else if (player != -1) return Shape.CIRCLE;
		
		else return null;
	}
	
	protected Color getBackground(int row, int col) { 

		return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;

	}
	
	protected int getSepPixels() { 
		return 2;
	}
	 
	protected Color getPlayerColor(int id) {
		if (state.getGameDescription().toUpperCase().equalsIgnoreCase("CHESS")){
			if (ChessBoard.black((byte) id)){
				return playersInfoViewer.getPlayerColor(1);
			}
			else if (ChessBoard.white((byte) id)){
				return playersInfoViewer.getPlayerColor(0);
			}
			else return playersInfoViewer.getPlayerColor(id);
		}
		return playersInfoViewer.getPlayerColor(id);
	}
	
	public void enable() {  
		if (gameCtrl.getPlayerId() == state.getTurn()) active = true;
	}
	
	public void disable() {  
		active = false;
		this.disableWindowMode();
	}
	
	public void update(S state) { 
		this.state = state;
		this.gameState = state;
		board = state.getBoard();
		
		if (state.getTurn() == gameCtrl.getPlayerId()) enable();
		else disable();

		jboard.repaint();
	}
	
	
	protected Image getIcon(int p) {
		return null;
	}


}
