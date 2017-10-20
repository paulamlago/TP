package es.ucm.fdi.tp.demo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class TableExample_1  extends JFrame {

	SomeTableModel tableModel;
			
	public TableExample_1() {
		super("[=] AbstractTableModel Example [=]");
		initGUI();
	}


	class SomeTableModel extends AbstractTableModel {

		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G", "H" };
		Object[][] rowData;
		static final int initNumOfRows = 10;

		public SomeTableModel() {
			int k = 0;
			rowData = new Object[initNumOfRows][columnNames.length];
			for(int i=0; i<rowData.length; i++)
				for(int j=0; j<columnNames.length; j++)
					rowData[i][j] = new Integer(k++);
		}
		public String getColumnName(int col) {
			return columnNames[col].toString();
		}

		public int getRowCount() {
			return rowData.length;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public Object getValueAt(int row, int col) {
			return rowData[row][col];
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			rowData[row][col] = value;
			fireTableCellUpdated(row, col);	
		}
		

	}


	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));

		tableModel = new SomeTableModel();
		JTable table = new JTable(tableModel);
		mainPanel.add(new JScrollPane(table));
		table.setFillsViewportHeight(true);

		JPanel p = new JPanel();
		mainPanel.add(p, BorderLayout.PAGE_START);
		
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TableExample_1();
			}
		});
	}


}

