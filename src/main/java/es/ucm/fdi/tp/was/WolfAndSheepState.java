package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.*;


public class WolfAndSheepState  extends GameState<WolfAndSheepState, WolfAndSheepAction>{

private static final long serialVersionUID = -2641387354190472377L;
	
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private int winner;

    private final int dim;

    final static int EMPTY = -1;
    final static int OVEJA = 1;
    final static int LOBO = 0;
    
	public WolfAndSheepState(int dim) {
		super(2);
		if (dim < 8 || dim > 8) {
            throw new IllegalArgumentException("Error: the dim is " + dim + " but this game requires an 8x8 board");
        }
		this.dim = dim;
        board = new int[dim][];
        
        for (int i=0; i<dim; i++) {
            board[i] = new int[dim];
            for (int j=0; j<dim; j++){
            	if (i == 0 && j % 2 != 0){ //en la primera fila y en posiciones impares ponemos ovejas
            		board[i][j] = OVEJA;
            	}
            	else{
            		board[i][j] = EMPTY;
            	}
            }
        }
        
        board[7][0] = LOBO; //en la esquina inferior izq ponemos al lobo
        
        this.turn = 0; //empieza jugando el lobo siempre!
        this.winner = -1;
        this.finished = false;
	}
	
	public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2);
    	this.dim = prev.dim;
        this.board = board;
        this.turn = (prev.getTurn() + 1) % 2;
        this.finished = finished;
        this.winner = winner;
    }    
	
    public boolean isWinner(int[][] board, int playerNumber) {
      boolean won = false;
    	
    	if (playerNumber == LOBO){ //estamos trabajando con el lobo -> Gana si está en la fila 0
    	   int i = 0;
    	   while (i < dim){
    		   if (board[0][i] == LOBO){
    			   won = true; 
    			   i = dim;//to exit
    		   }
    		  i++;
    	   }
    	 
    	   if (won == false){
    	   //puede haber ganado llegando a la fila 0 o si las ovejas se quedan sin movimientos posibles(si han llegado al otro lado del talbero
    	   //automáticamente contará como que ha ganado el lobo
    	   List<WolfAndSheepAction> v = validActions(OVEJA);
		   		if (v.isEmpty()){
		   			won = true;
		   			winner = 0;
		   		}
    	   }
      }
    	
    	if (playerNumber ==OVEJA){ //Sabremos que han ganado si el lobo no tiene movimientos posibles!
    		List<WolfAndSheepAction> v = validActions(LOBO);
    		if (v.isEmpty()){
    			won = true;
    			winner = 1;
    		}
       }
       
       return won;
    }    

	public int getTurn() {
		return turn;
	}

	public int at(int row, int col) {
	       return board[row][col];
	 }

	public List<WolfAndSheepAction> validActions(int playerNumber) {
		ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
        if (finished) {
            return valid; //DEVUELVES UNA LISTA VACÍA
        }

        //validActions dependen de si estamos jugando con el lobo o con las ovejas
        int i = 0, j;
        
        if (playerNumber == LOBO){
        	while (i < dim) {
        		j = 0;
                while (j < dim) {
                    if (at(i, j) == LOBO) { //el lobo puede moverse en diagonal, hacia alante o hacia atrás.
                    	WolfActions(i,j, playerNumber, valid);
                    	j = dim;
                    	i = dim; //para salir porque solo hay un lobo!
                    }
                    j++;
                }
                
                i++;
            }
        }
        
        int x = 0, y, counterOvejas = 0; //so if I find 4 I can stop searching 
        
        if (playerNumber == OVEJA){
        	
        	while (x < dim && counterOvejas < 4) {
        		y = 0;
                while (y < dim && counterOvejas < 4) {
                    if (at(x, y) == OVEJA) { //las ovejas solo pueden moverse hacia delante y en diagonal!
                    	SheepActions(x, y, playerNumber, valid);
                    	counterOvejas++;
                    }
                    y++;
                }
                x++;
            }
        }
    
        return valid;
	}

	void SheepActions(int i, int j, int player, ArrayList<WolfAndSheepAction> valid){
		if (i + 1 < 8  && j - 1 > -1 && board[i + 1][j - 1] == EMPTY){ //diagonal abajo izq
			valid.add(new WolfAndSheepAction(player, i + 1, j - 1, i, j));
		}
		if (i + 1 < 8  && j + 1 < 8 && board[i + 1][j + 1] == EMPTY){ //diagonal abajo derecha
			valid.add(new WolfAndSheepAction(player, i + 1, j + 1, i, j));
		}
	}
	
	void WolfActions(int i, int j, int playerNumber, ArrayList<WolfAndSheepAction> valid){
		if (i - 1 > -1 && j + 1 < 8 && board[i - 1][j + 1] == EMPTY){ //diagonal arriba derecha
			valid.add(new WolfAndSheepAction(playerNumber, i - 1, j + 1, i, j));
		}
		if (i - 1 > -1 && j - 1 > -1 && board[i - 1][j - 1] == EMPTY){ //diagonal arriba izq
			valid.add(new WolfAndSheepAction(playerNumber, i - 1, j - 1, i, j));
		}
		if (i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] == EMPTY){ //diagonal abajo derecha
			valid.add(new WolfAndSheepAction(playerNumber, i + 1, j + 1, i, j));
		}
		if (i + 1 < 8 && j - 1 > -1 && board[i + 1][j - 1] == EMPTY){ //diagonal abajo izq
			valid.add(new WolfAndSheepAction(playerNumber, i + 1, j - 1, i, j));
		}
	}
	
	public boolean isFinished() {
		return finished;
	}

	public int getWinner() {
		return winner;
	}
	
	public int getDim(){
		return dim;
	}
	
	 public int[][] getBoard() {
	       int[][] copy = new int[board.length][];
	       for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
	       return copy;
	  }
	 
	 public String toString() {
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("    ");
	        int i = 0;
	        while (i < dim){
	        	sb.append(i + "   ");
	        	i++;
	        }
	        sb.append("\n");
	        
	        
	        for (i=0; i<board.length; i++) {
	            sb.append(i + " |");
	            for (int j=0; j<board.length; j++) {
	            	if (board[i][j] == EMPTY){ 
	            		sb.append("   |");
	            	}
	            	else if (board[i][j] == LOBO){
	            		sb.append(" W |");
	            	}
	            	else if (board[i][j] == OVEJA){ 
	            		sb.append(" S |");
	            	}
	            }
	            sb.append("\n");
	        }
	        return sb.toString();
	    }

	 public boolean outOfTheBoard(int row, int col){
		 return row <= dim && col <= dim;
	 }

	 protected String name(){
	    	return "Wholf And Sheep";
	    }

	@Override
	public void dBomba(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bomba() {
		// TODO Auto-generated method stub
		
	}

}
