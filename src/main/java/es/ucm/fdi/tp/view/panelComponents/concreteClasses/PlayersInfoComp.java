package es.ucm.fdi.tp.view.panelComponents.concreteClasses;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jcolor.*;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class PlayersInfoComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends PlayersInfoViewer<S, A> {
	//Based on the use of a JTable with a TableModel and JColorChooser
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int playerNumber;
	private Map<Integer, Color> colors;
	private ColorChooser colorChooser;
	private TableModel tModel;
	
	public PlayersInfoComp(int pNumber){
		
		setNumberOfPlayer(pNumber); //this is the player playing in this moment
		playerColor = new Color[pNumber];
		playerColor[0] = Color.PINK;
		playerColor[1] = Color.CYAN;
		
		initGUI();	
		this.setBorder(BorderFactory.createTitledBorder("Players info"));
	}
		
	private void initGUI() {
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);
		

		tModel = new TableModel(playerNumber);
		
		final JTable table = new JTable(tModel) {
			private static final long serialVersionUID = 1L;
			
			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				
				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1){
					comp.setBackground(colors.get(row));
					colors.put(0, getPlayerColor(0));
					colors.put(1, getPlayerColor(1));
				}
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
			

		};
		
		
		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});

		this.add(table, BorderLayout.SOUTH);
		
	}
		
	private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			repaint();
			
			//AFTER CHANGING THE COLOR WE NEED TO NOTIFY ALL THE OBSERVERS
			notifyObservers(row, colorChooser.getColor());
		}

	}
	
	public Color getPlayerColor(int p) {
		if (p == 0 || p == 1) return playerColor[p];
		else return Color.LIGHT_GRAY;
	}
	
	public void setNumberOfPlayer(int n) { 
		playerNumber = n;
	}

	@Override
	public void enable() {

	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(S state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameController(GameController<S, A> gameCtrl) {
		// TODO Auto-generated method stub
		
	}
	
}
