package es.ucm.fdi.tp.view.panelComponents.Board;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class ChessView extends RectBoardView<ChessState, ChessAction>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int origRow;
	private int origCol;
	private boolean lastClick;
	
	public ChessView(GameController<ChessState, ChessAction> gameCtrl, MessageViewer<ChessState, ChessAction> infoViewer, PlayersInfoViewer<ChessState, ChessAction> playersInfoViewer, int rows, int cols) {
		super(new ChessState(), gameCtrl, infoViewer, playersInfoViewer, rows, cols);
	}

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
	protected Image getIcon(int p) {
		String imageName = ChessBoard.Piece.iconName((byte) p);
		try {			
			Image i = ImageIO.read(Utils.class.getClassLoader().getResource("chess/"+imageName));
			return i;
		} catch (IOException e) {
			return null;
		}
	}
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) throws IllegalArgumentException{
		if (!lastClick) firstClick(row, col);
		
		if (this.active && this.gameCtrl.getPlayerMode() == PlayerMode.manual){
			if (!lastClick){
				if (this.state.getTurn() == this.gameCtrl.getPlayerId()){
					this.infoViewer.addContent("Now click in the destination cell!");
					secondClick();
				}
				else this.infoViewer.addContent("Click on the cell you want to move!");
			}
				
			else{
				ChessAction a = new ChessAction(this.gameCtrl.getPlayerId(), origRow, origCol, row, col);
				
				if (this.state.isValid(a)) {
					this.gameCtrl.makeManualMove(a);
					lastClick = false;
				}
				else this.infoViewer.addContent("Not a valid move!");
			}
		}
	}

	public void firstClick(int row, int col){
		this.origCol = col;
		this.origRow = row;
		lastClick = false;
	}
	
	public void secondClick(){
		lastClick = true;
	}
	
	@Override
	protected void keyTyped(int keyCode) {
		lastClick = false; 
		this.infoViewer.addContent("The movement has been canceled");
	}

}
