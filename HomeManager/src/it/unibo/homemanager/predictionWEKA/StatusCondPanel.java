package it.unibo.homemanager.predictionWEKA;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class StatusCondPanel extends JFrame {

	private JPanel contentPane;
	private JTable table_notte;
	private JLabel lblNotte;
	private JLabel lblMattino;
	private JTable table_mattino;
	private JTable table_PM;
	private JLabel lblPomeriggioSera;
	private JLabel lblHome;

	private SmartPredictorAgent spa;
	private JLabel lblSaluti;
	private JLabel lblSera;
	private JTable table_sera;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StatusCondPanel frame = new StatusCondPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public void initComponents()
	  {
		
    
    
     
		
	    setTitle("Predictor Status Conditionair");
		setResizable(false);
		
		setBounds(0, 0, 947, 716);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 921, 676);
		contentPane.add(panel);
		panel.setLayout(null);
		
		table_notte = new JTable();
		table_notte.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_notte.setForeground(new Color(255, 255, 255));
		table_notte.setBounds(10, 73, 901, 96);
		panel.add(table_notte);
		table_notte.setBackground(new Color(51, 102, 204));
		table_notte.setModel(new DefaultTableModel(
			new Object[][] {
				{"", "Hour 00:00", "Hour 01:00", "Hour 02:00", "Hour 03:00", "Hour 04:00", "Hour 05:00", "Hour 06:00"},
				{"Rain?", null, null, null, null, null, null, null},
				{"Home Predicted?", null, null, null, null, null, null, null},
				{"User Policy", null, null, null, null, null, null, null},
				{" Exceeds threshold Temp?", null, null, null, null, null, null, null},
				{"Status Predicted Conditionair", null, null, null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_notte.getColumnModel().getColumn(0).setPreferredWidth(144);
		
		lblNotte = new JLabel("");
		lblNotte.setBounds(10, 24, 50, 38);
		panel.add(lblNotte);
		
		lblMattino = new JLabel("");
		lblMattino.setBounds(10, 190, 50, 38);
		panel.add(lblMattino);
		
		table_mattino = new JTable();
		table_mattino.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_mattino.setBounds(10, 239, 901, 96);
		panel.add(table_mattino);
		table_mattino.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "Hour 07:00", "Hour 08:00", "Hour 09:00", "Hour 10:00", "Hour 11:00", "Hour 12:00", "Hour 13:00"},
				{"Rain?", null, null, null, null, null, null, null},
				{"Home Predicted?", null, null, null, null, null, null, null},
				{"User Policy", null, null, null, null, null, null, null},
				{"Exceeds thershold Temp?", null, null, null, null, null, null, null},
				{"Status Predicted Conditionair", null, null, null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_mattino.getColumnModel().getColumn(0).setPreferredWidth(208);
		table_mattino.setBackground(new Color(255, 255, 153));
		 		
		 				
		 				table_PM = new JTable();
		 				table_PM.setBounds(10, 400, 901, 96);
		 				panel.add(table_PM);
		 				table_PM.setFont(new Font("Tahoma", Font.PLAIN, 12));
		 				
		 				table_PM.setModel(new DefaultTableModel(
		 					new Object[][] {
		 						{"", "Hour 14:00", "Hour 15:00", "Hour 16:00", "Hour 17:00", "Hour 18:00", "Hour 19:00", "Hour 20:00"},
		 						{"Rain?", null, null, null, null, null, null, null},
		 						{"Home Predicted?", null, null, null, null, null, null, null},
		 						{"User Policy?", null, null, null, null, null, null, null},
		 						{"Exceeds thershold temp?", null, null, null, null, null, null, null},
		 						{"Status Conditionair Predicted", null, null, null, null, null, null, null},
		 					},
		 					new String[] {
		 						"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
		 					}
		 				) {
		 					boolean[] columnEditables = new boolean[] {
		 						false, false, false, false, false, false, false, false
		 					};
		 					public boolean isCellEditable(int row, int column) {
		 						return columnEditables[column];
		 					}
		 				});
		 				table_PM.getColumnModel().getColumn(0).setPreferredWidth(174);
		 				table_PM.setBackground(new Color(255, 204, 102));
		 				
		 				 lblPomeriggioSera = new JLabel("");
		 				lblPomeriggioSera.setBounds(10, 346, 50, 38);
		 				panel.add(lblPomeriggioSera);
		 				
		 				lblHome = new JLabel("");
		 				lblHome.setBounds(836, 11, 75, 51);
		 				panel.add(lblHome);
		 				
		 				lblSaluti = new JLabel("Hi, today is "+ LocalDate.now().getDayOfWeek());
		 				lblSaluti.setFont(new Font("Courier New", Font.PLAIN, 20));
		 				lblSaluti.setBounds(191, 24, 314, 23);
		 				panel.add(lblSaluti);
		 				
		 				lblSera = new JLabel("");
		 				lblSera.setBounds(10, 513, 50, 38);
		 				panel.add(lblSera);
		 				
		 				table_sera = new JTable();
		 				table_sera.setBackground(new Color(255, 153, 102));
		 				table_sera.setModel(new DefaultTableModel(
		 					new Object[][] {
		 						{null, "Hour 21:00", "Hour 22:00", "Hour 23:00"},
		 						{"Rain?", null, null, null},
		 						{"Home Predicted?", null, null, null},
		 						{"User Policy?", null, null, null},
		 						{"Exceeds thershold temp?", null, null, null},
		 						{"Status Predicted Conditionair", null, null, null},
		 					},
		 					new String[] {
		 						"New column", "New column", "New column", "New column"
		 					}
		 				) {
		 					boolean[] columnEditables = new boolean[] {
		 						false, false, false, false
		 					};
		 					public boolean isCellEditable(int row, int column) {
		 						return columnEditables[column];
		 					}
		 				});
		 				table_sera.getColumnModel().getColumn(0).setPreferredWidth(181);
		 				table_sera.setBounds(10, 562, 901, 96);
		 				panel.add(table_sera);
		 				
		 				JScrollBar scrollBar = new JScrollBar();
		 				scrollBar.setBounds(631, 11, 17, 281);
		 				scrollBar.setAutoscrolls(true);

		table_notte.setRowSelectionInterval(0, 0);
		table_mattino.setRowSelectionInterval(0, 0);
		table_PM.setRowSelectionInterval(0, 0);
		table_sera.setRowSelectionInterval(0, 0);
	
		JScrollPane scroll = new JScrollPane (panel);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scroll.setBounds(10, 11, 921, 676);
		 getContentPane().add(scroll);
	  }
	/**
	 * Create the frame.
	 */
	public StatusCondPanel() {
		
		
		initComponents();
		
		
		
	}
	
	public void init() {
        if(!isVisible())
        {
        	setVisible(true);
        	
             lblNotte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/notte.png")));
             lblMattino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mattino.png")));
             lblPomeriggioSera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pomeriggio.png")));
             lblSera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sera.png")));
             lblHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home.png")));
             
           
			try {
				  UserPreferencePolicy upp = spa.getUserPreferencePolicy();
				 
				  Map<Integer,String> mappa = spa.getMap();
				  WEKAPredictor wp = spa.getWEKAPredictor();
				  List<HomePresencePrediction> myHome;
				  myHome = wp.makePredictionByDay(LocalDate.now().getDayOfWeek().getValue());
			
	             int cont_mattino = 1;
	             int cont_PM = 1;
	             int cont_notte = 1;
	             int cont_sera=1;
	             for(int i = 0; i<=23; i++)
	             {
	            	 //se orario della notte
	            	 if ((i>=0) && (i<=6)) 
	            		 {
	            		  table_notte.getModel().setValueAt(spa.isRainingDay(spa.getMeteoAttuale()),1,cont_notte);
	            		  table_notte.getModel().setValueAt(myHome.get(i).getIn_home().toString(),2,cont_notte);
	            		  String none ="None";
	            		  if((spa.checkPolicy(i)!=null))
	            		     {
	            			  none = "Yes";
	            		     }
	            		  table_notte.getModel().setValueAt(none,3,cont_notte);
	            		  if(spa.getMeteoAttuale().getTemperatura()>=upp.getTemp()) none = "Yes";
	            		      else none = "No";
	            		  table_notte.getModel().setValueAt(none,4,cont_notte);
	            		  table_notte.getModel().setValueAt(mappa.get(i).toUpperCase(),5,cont_notte);            		  
	            		  cont_notte++;
	            		 }
	            	
	            	 else if ((i>=7) && (i<=13)) 
            		 {
            		  table_mattino.getModel().setValueAt(spa.isRainingDay(spa.getMeteoAttuale()),1,cont_mattino);
            		  table_mattino.getModel().setValueAt(myHome.get(i).getIn_home().toString(),2,cont_mattino);
            		  String none ="None";
            		  if((spa.checkPolicy(i)!=null))
            		     {
            			  none = "Yes";
            		     }
            		  table_mattino.getModel().setValueAt(none,3,cont_mattino);
            		  if(spa.getMeteoAttuale().getTemperatura()>=upp.getTemp()) none = "Yes";
            		      else none = "No";
            		  table_mattino.getModel().setValueAt(none,4,cont_mattino);
            		  table_mattino.getModel().setValueAt(mappa.get(i).toUpperCase(),5,cont_mattino);            		  
            		  cont_mattino++;
            		 }
	            	
	            	 else if ((i>=14) && (i<=20)) 
            		 {
            		  table_PM.getModel().setValueAt(spa.isRainingDay(spa.getMeteoAttuale()),1,cont_PM);
            		  table_PM.getModel().setValueAt(myHome.get(i).getIn_home().toString(),2,cont_PM);
            		  String none ="None";
            		  if((spa.checkPolicy(i)!=null))
            		     {
            			  none = "Yes";
            		     }
            		  table_PM.getModel().setValueAt(none,3,cont_PM);
            		  if(spa.getMeteoAttuale().getTemperatura()>=upp.getTemp()) none = "Yes";
            		      else none = "No";
            		  table_PM.getModel().setValueAt(none,4,cont_PM);
            		  table_PM.getModel().setValueAt(mappa.get(i).toUpperCase(),5,cont_PM);            		  
            		  cont_PM++;
            		 }
	            	 
	            	 
	            	 else if ((i>=21) && (i<=23)) 
            		 {
            		  table_sera.getModel().setValueAt(spa.isRainingDay(spa.getMeteoAttuale()),1,cont_sera);
            		  table_sera.getModel().setValueAt(myHome.get(i).getIn_home().toString(),2,cont_sera);
            		  String none ="None";
            		  if((spa.checkPolicy(i)!=null))
            		     {
            			  none = "Yes";
            		     }
            		  table_sera.getModel().setValueAt(none,3,cont_sera);
            		  if(spa.getMeteoAttuale().getTemperatura()>=upp.getTemp()) none = "Yes";
            		      else none = "No";
            		  table_sera.getModel().setValueAt(none,4,cont_sera);
            		  table_sera.getModel().setValueAt(mappa.get(i).toUpperCase(),5,cont_sera);            		  
            		  cont_sera++;
            		 }
	            	 
	             }
	             
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
         	
        }
        
    }



	public void setAgent(SmartPredictorAgent spa) {
		this.spa = spa;
	}
}

