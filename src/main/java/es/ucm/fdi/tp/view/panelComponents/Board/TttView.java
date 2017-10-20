package es.ucm.fdi.tp.view.panelComponents.Board;


import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class TttView extends RectBoardView<TttState, TttAction> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TttView(GameController<TttState, TttAction> gameCtrl, MessageViewer<TttState, TttAction> infoViewer, PlayersInfoViewer<TttState, TttAction> playersInfoViewer, int rows, int cols) {
		super(new TttState(3), gameCtrl, infoViewer, playersInfoViewer, rows, cols);
	}

	@Override
	protected int getNumCols() {
		return this.numOfCols;
	}

	@Override
	protected int getNumRows() {
		return this.numOfRows;
	}

	@Override
	protected Integer getPosition(int row, int col) {
		return this.board[row][col];
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) throws IllegalArgumentException{
		if (this.gameCtrl.getPlayerMode() == PlayerMode.manual){
			if (getPosition(row, col) != gameCtrl.getPlayerId()){ //si no está pulsando sobre sí mismo
				TttAction a = new TttAction(this.gameCtrl.getPlayerId(), row, col);
				this.infoViewer.addContent("You have clicked on ("+ row + ", " + col + ")");
				//this.gameCtrl.handleEvent(new GameEvent<TttState, TttAction>(EventType.Change, a, this.state, null, "Mouse : "  + mouseButton + " has clicked on ("+ row + ", " + col + ")"));
				this.gameCtrl.makeManualMove(a);
			}
		}
	}

	@Override
	protected void keyTyped(int keyCode) {
		this.infoViewer.addContent("You can't cancel a movement in TTT");
	}
	
	public void enable() {  
		super.enable();
	}
}
