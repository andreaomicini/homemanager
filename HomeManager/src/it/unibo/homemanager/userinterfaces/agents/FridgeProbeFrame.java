package it.unibo.homemanager.userinterfaces.agents;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FridgeProbeFrame extends JFrame {

	private JTable table;
	private JFrame frame;
	private MyTableModel model;
	private final Object[] columnNames = {"Ingredient","Amount"};
	private String[][] data;
	
	public FridgeProbeFrame(String name){
	        
        // create JFrame and JTable
        frame = new JFrame(name);
        table = new JTable(); 
        
        // create a table model and set a Column Identifiers to this model 
        model = new MyTableModel(data, columnNames);
        model.setColumnIdentifiers(columnNames);
        
        // set the model to the table
        table.setModel(model);
        
        // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,22);
        table.setFont(font);
        table.setRowHeight(25);
        
        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(15, 15, 270, 550);
        
        frame.setLayout(null);
        
        frame.add(pane);
        
        frame.setSize(300,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - frame.getWidth();
        int y = 0;
        frame.setLocation(x, y);
        frame.setVisible(true);
        frame.setResizable(false);
	        
	}

	public void deleteIngredient(String ingredient){
		System.out.println("[FRIDGE PROBE FRAME]: delete method");
		for(int i=0; i<=table.getRowCount(); i++){
			String value = table.getValueAt(i, 0).toString();
			if(value.equals(ingredient)){
				model.removeRow(i);
			}
		}
	}

	public void updateIngredient(String ingredient, int new_quantity) {
		System.out.println("[FRIDGE PROBE FRAME]: update method");
		deleteIngredient(ingredient);
		addIngredient(ingredient, new_quantity);
		
	}

	public void addIngredient(String ingredient, int quantity) {
		if(quantity>0){
			System.out.println("[FRIDGE PROBE FRAME]: add method");
			Object[] row = new Object[2];
			row[0] = ingredient;
			row[1] = Integer.toString(quantity);
			model.addRow(row);
		}
		
	}
	
	/**
     * Custom table model
     */
    public class MyTableModel extends DefaultTableModel{
            
        public MyTableModel(Object[][] data, Object[] columnNames){
            super(data,columnNames);
        }

        public boolean isCellEditable(int row, int col){
            return false;
        }

    }
}