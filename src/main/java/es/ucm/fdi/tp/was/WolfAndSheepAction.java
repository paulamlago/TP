package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;


public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int player;
    private int row;
    private int col;
    private int rowOrig;
    private int colOrig;
    
    public WolfAndSheepAction(int player, int row, int col, int rowOrig, int colOrig) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.rowOrig = rowOrig;
        this.colOrig = colOrig;        
    }
    
	public int getPlayerNumber() {
		return player;
	}

	
	public WolfAndSheepState applyTo(WolfAndSheepState state){
		if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

		// make move
        int[][] board = state.getBoard();
        
        if (player == 1 && (row - rowOrig == 1)&&(col - colOrig == 1 || col - colOrig == -1)){ //si solo se está moviendo una posición
	        board[row][col] = player;
	        board[rowOrig][colOrig] = -1; //empty
        }
        
        else if (player == 0&& (row - rowOrig == 1 || row - rowOrig == -1)&&(col - colOrig == 1 || col - colOrig == -1)){ //lobo
        	board[row][col] = player;
	        board[rowOrig][colOrig] = -1; //empty
        }
        
        else throw new GameError("You are trying to move to an incorrect position!");
        // update state
        WolfAndSheepState next = new WolfAndSheepState(state, board, state.isFinished(), state.getWinner());
        
        if (next.isWinner(board, state.getTurn())) {
            next = new WolfAndSheepState(state, board, true, state.getTurn());
        } else if (next.isWinner(board, (state.getTurn() + 1) % 2)) { //HAY QUE COMPROBAR ADEMÁS DE SI HA GANADO, SI HA PERDIDO TRAS ESE MOVIMIENTO.
            next = new WolfAndSheepState(state, board, true, (state.getTurn() + 1) % 2);
        } else {
            next = new WolfAndSheepState(state, board, false, -1);
        }
        
        return next;	
	} 
	
	public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
    	String name;
    	
    	if (player == 0) name = "Wolf";
    	else name = "Sheep";
    	
        return "place " + name + " (actually in position: " + rowOrig + ", " + colOrig + ")" + " at (" + row + ", " + col + ")";
    }

}
