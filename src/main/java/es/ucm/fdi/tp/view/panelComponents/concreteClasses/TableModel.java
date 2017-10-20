package es.ucm.fdi.tp.view.panelComponents.concreteClasses;

import javax.swing.table.AbstractTableModel;


public class TableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames;
	private String[][] data;
	
	
	public TableModel(int numberOfPlayers) {
		this.colNames = new String[] {"Player", "Color"};
		data = new String[numberOfPlayers][2];

		initializeData(numberOfPlayers);
	}

	public int getColumnCount() {
		return colNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public Object getValueAt(int columnIndex, int rowIndex) {
		return data[columnIndex][rowIndex];
	}
	
	public void refresh() {
		fireTableStructureChanged();
	}
	
	private void initializeData(int playerNumber) {
		 
		for (int i = 0; i < playerNumber; i++){
			for (int j = 0; j < playerNumber; j++){
				if (j == 0){
					data[i][j] = String.valueOf(i);
				}
				else {
					data[i][j] = null;
				}
			}
		}
		
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}
}
