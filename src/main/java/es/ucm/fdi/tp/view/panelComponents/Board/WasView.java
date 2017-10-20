package es.ucm.fdi.tp.view.panelComponents.Board;

import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WasView extends RectBoardView<WolfAndSheepState, WolfAndSheepAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private int origRow;
	private int origCol;
	private boolean lastClick;
	
	public WasView(GameController<WolfAndSheepState, WolfAndSheepAction> gameCtrl, MessageViewer<WolfAndSheepState, WolfAndSheepAction> infoViewer,PlayersInfoViewer<WolfAndSheepState, WolfAndSheepAction> playersInfoViewer,int rows, int cols) {
		super(new WolfAndSheepState(8), gameCtrl, infoViewer, playersInfoViewer, rows, cols);
	}

	
	protected int getNumCols() { return this.numOfCols; }
	
	protected int getNumRows() { return this.numOfRows; }
	
	protected Integer getPosition(int row, int col) {
		return this.board[row][col];
	}
	
	//protected Color getBackground(int row, int col) {  }
	
	protected int getSepPixels() { return 2; }
	
	public void enable() { super.enable(); }
	
	protected void keyTyped(int keyCode) { 
		//if (keyCode == 27) {//escape
			lastClick = false; 
			this.infoViewer.addContent("The movement has been canceled");
		//}
	}
	
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {  
		if (!lastClick) firstClick(row, col);
		
		if (this.active && this.gameCtrl.getPlayerMode() == PlayerMode.manual){
			if (!lastClick){
				if (getPosition(row, col) == this.gameCtrl.getPlayerId()){
					this.infoViewer.addContent("Now click in the destination cell!");
					secondClick();
				}
				else this.infoViewer.addContent("Click on the cell you want to move!");
			}
				
			else{
				WolfAndSheepAction a = new WolfAndSheepAction(this.gameCtrl.getPlayerId(), row, col, origRow, origCol);
				this.gameCtrl.makeManualMove(a);
				lastClick = false;
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
}
