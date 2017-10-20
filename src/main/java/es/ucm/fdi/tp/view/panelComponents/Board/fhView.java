package es.ucm.fdi.tp.view.panelComponents.Board;

import es.ucm.fdi.tp.Junio2016_2.FyHaction;
import es.ucm.fdi.tp.Junio2016_2.FyHstate;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class fhView extends RectBoardView<FyHstate, FyHaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public fhView(GameController<FyHstate, FyHaction> gameCtrl,
			MessageViewer<FyHstate, FyHaction> infoViewer, PlayersInfoViewer<FyHstate, FyHaction> playersInfoViewer,
			int rows, int cols) {
		super(new FyHstate(), gameCtrl, infoViewer, playersInfoViewer, rows, cols);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getNumCols() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Integer getPosition(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void keyTyped(int keyCode) {
		// TODO Auto-generated method stub
		
	}

}
