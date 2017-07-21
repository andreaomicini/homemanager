/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.homemanager.predictionWEKA;


import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;


import java.awt.Font;

import javax.swing.JButton;

import java.awt.Color;


import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;




public class ViewSmartPredictorAgentPanel  extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ViewManageAgentPanel butlerPanel;
	private SmartPredictorAgent smartPredictorAgent;
	private TracePanel tp;
	

	private JTable previsioni;
	private JLabel lblUserTemp;
	private JLabel lblDate;
	private JLabel lblTemperature;
	private JLabel lblPrevisione;
	private LocalDateTime data;
	private JTextArea taPolicy;
	private JLabel lblNomeCondizionatore;


	private JLabel foto;
	private final String grado = ""+ "\u00b0";
	

	public ViewSmartPredictorAgentPanel(ViewManageAgentPanel maPanel, SmartPredictorAgent spa, TracePanel tp) {
		setPreferredSize(new Dimension(538, 349));
		setMinimumSize(new Dimension(538, 343));
		setMaximumSize(new Dimension(538, 343));
		data = LocalDateTime.now();
		this.butlerPanel=maPanel;
        this.smartPredictorAgent=spa;
        this.tp=tp;
        
        
        
        JLabel lblSmartPredictorAgent = new JLabel("Smart Device Predictor ");
        lblSmartPredictorAgent.setBounds(22, 9, 254, 21);
        lblSmartPredictorAgent.setFont(new Font("Courier New", Font.BOLD, 18));
        
        JButton jButton4 = new JButton();
        jButton4.setBounds(331, 5, 90, 25);
        jButton4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		backButtonActionPerformed(arg0);
        	}
        });
        jButton4.setText("Back");
        jButton4.setForeground(new Color(102, 0, 102));
        jButton4.setFont(new Font("Courier New", Font.BOLD, 14));
        
        JButton jButton3 = new JButton();
        jButton3.setBounds(431, 5, 97, 25);
        jButton3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		menuButtonActionPerformed(e);
        	}
        });
        setLayout(null);
        jButton3.setText("Menu");
        jButton3.setForeground(new Color(102, 0, 102));
        jButton3.setFont(new Font("Courier New", Font.BOLD, 14));
        
        
        add(jButton3);
        add(jButton4);
        add(lblSmartPredictorAgent);
        
        JPanel InfoPanel = new JPanel();
        InfoPanel.setBorder(new TitledBorder(null, "Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        InfoPanel.setBounds(22, 41, 293, 182);
        add(InfoPanel);
        InfoPanel.setLayout(null);
        
       
        
        JLabel lblTemp = new JLabel("Actual Temperature");
        lblTemp.setFont(new Font("Courier New", Font.BOLD, 13));
        lblTemp.setBounds(10, 60, 165, 21);
        InfoPanel.add(lblTemp);
        
        JLabel lblUserPreferedtemperature = new JLabel("User prefered Temperature");
        lblUserPreferedtemperature.setFont(new Font("Courier New", Font.BOLD, 13));
        lblUserPreferedtemperature.setBounds(10, 105, 218, 21);
        InfoPanel.add(lblUserPreferedtemperature);
        
        lblUserTemp = new JLabel("");
        lblUserTemp.setBackground(new Color(255, 255, 255));
        lblUserTemp.setBounds(216, 103, 77, 25);
        InfoPanel.add(lblUserTemp);
        lblUserTemp.setFont(new Font("Courier New", Font.PLAIN, 13));
        
        JLabel lblForecast = new JLabel("Forecast");
        lblForecast.setFont(new Font("Courier New", Font.BOLD, 13));
        lblForecast.setBounds(10, 137, 82, 21);
        InfoPanel.add(lblForecast);
        
        lblDate = new JLabel("");
        lblDate.setBackground(new Color(255, 255, 255));
        lblDate.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblDate.setBounds(10, 35, 283, 14);
        InfoPanel.add(lblDate);
        
        lblTemperature = new JLabel("");
        lblTemperature.setBackground(new Color(255, 255, 255));
        lblTemperature.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblTemperature.setBounds(167, 60, 126, 14);
        InfoPanel.add(lblTemperature);
        
        lblPrevisione = new JLabel("");
        lblPrevisione.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblPrevisione.setBackground(Color.WHITE);
        lblPrevisione.setBounds(90, 133, 197, 25);
        InfoPanel.add(lblPrevisione);
        
   
        
        foto = new JLabel();
        foto.setBounds(331, 41, 197, 86);
        add(foto);
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        statusPanel.setBounds(22, 234, 293, 98);
        add(statusPanel);
        statusPanel.setLayout(null);
        
        previsioni = new JTable();
        previsioni.setEnabled(false);
        previsioni.setBorder(new LineBorder(new Color(0, 0, 0)));
        previsioni.setBackground(new Color(204, 255, 204));
        
        int orario = data.getHour() % 24;
        
        previsioni.setModel(new DefaultTableModel(
        	new Object[][] {
        		{"Now "+ orario, "Next Hour "+ (orario +1)},
        		{null, null},
        	},
        	new String[] {
        		"New column", "New column"
        	}
        ));
        previsioni.setBounds(35, 21, 222, 32);
        statusPanel.add(previsioni);
        
        JButton btnPredizioniCondizionatore = new JButton("All predicted status");
        btnPredizioniCondizionatore.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		 predCondActionPerformed(arg0);		
        	}
        });
        btnPredizioniCondizionatore.setBounds(35, 64, 222, 23);
        statusPanel.add(btnPredizioniCondizionatore);
        
        lblNomeCondizionatore = new JLabel("");
        lblNomeCondizionatore.setBounds(331, 129, 165, 14);
        add(lblNomeCondizionatore);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(null, "Policy User", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(321, 154, 207, 189);
        add(panel);
        panel.setLayout(null);
        
        
       
     
        
        taPolicy= new JTextArea();
        taPolicy.setEditable ( false ); // set textArea non-editable
        taPolicy.setBounds(10, 21, 145, 96);
        JScrollPane scroll = new JScrollPane (taPolicy);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scroll.setBounds(10, 21, 188, 157);
    
    
        panel.add(scroll);
		
	}
	
private void predCondActionPerformed(ActionEvent arg0) {
	
		
		StatusCondPanel abitudiniCondPanel = new StatusCondPanel();
	    abitudiniCondPanel.setAgent(smartPredictorAgent);
	    abitudiniCondPanel.init();
		
		
	}


	

	private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuButtonActionPerformed
        this.hidePanel();
        butlerPanel.menu();
    }//GEN-LAST:event_menuButtonActionPerformed
	
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.hidePanel();
        butlerPanel.showPanel();
	}//GEN-LAST:event_backButtonActionPerformed
	
	public void hidePanel() {
	        if(isVisible())
	            setVisible(false);
	    }
	     
	public void init() {
	        if(!isVisible())
	        {
	        	setVisible(true);
	        	LocalDateTime ora = LocalDateTime.now();
	        	String ora_format = ora.getDayOfWeek() + "   "+ ora.getDayOfMonth()+" "+ora.getMonth() + " "+ ora.getYear()+ "     " + "Ore "+ ora.getHour()+":"+ ora.getMinute(); 
	        	lblDate.setText(ora_format);
	        	lblUserTemp.setText(String.valueOf(smartPredictorAgent.getUserPreferencePolicy().getTemp()) + " "+ grado + "C");
	        	
	        	
	        	DecimalFormat decForm = new DecimalFormat("#.#", new DecimalFormatSymbols());
	        	decForm.setRoundingMode(RoundingMode.FLOOR);
	        	String temp_arr = decForm.format(smartPredictorAgent.getMeteoAttuale().getTemperatura());
	        	
	        	
	        	lblNomeCondizionatore.setText("Air Conditionair room : " + " "+ smartPredictorAgent.getUserPreferencePolicy().getId_stanza());
	        	lblTemperature.setText(temp_arr+ " "+ grado+ "C");
	        	
	        	lblPrevisione.setText(smartPredictorAgent.getMeteoAttuale().getMeteo());
	        	Map<Integer,String> pred = smartPredictorAgent.getMap();
	             
	        	//setto la previsione
	        	int orario = data.getHour() % 24;
	        	 previsioni.getModel().setValueAt(pred.get(orario),1,0);
	        	 previsioni.getModel().setValueAt(pred.get(orario+1),1,1);
	            
	        	 foto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frizzy.jpeg")));
	       
	             //Set delle policy trovate per la giorata
	        	 StringBuilder sb = new StringBuilder();
	        	sb.append("User id : "+ smartPredictorAgent.getUserPreferencePolicy().getId_user()+"  ");
	        	sb.append("Room id : "+ smartPredictorAgent.getUserPreferencePolicy().getId_stanza()+"\n");
	        	sb.append("---------------------------- \n");
	        	sb.append("User Threshold Temp : " + smartPredictorAgent.getUserPreferencePolicy().getTemp()+grado+"C");
	        	
	        	ArrayList<Policy> politiche = smartPredictorAgent.getUserPreferencePolicy().getPolitiche().get(LocalDate.now().getDayOfWeek().getValue());
	            if(politiche== null)
	               {
	            	 sb.append("\n\n No User Policy today! \n ");
	               }
	            else 
	              {
	            	for(Policy pol : politiche)
	            	{sb.append("\n---------------------------- \n");
	            	sb.append("Today Policy : \n");
	            	sb.append("Start Hour : "+ pol.getOrario_start()+"\n");
	            	sb.append("End Hour : "+ pol.getOrario_finish()+"\n");
	            	sb.append("Status : "+ pol.getStato_condizionatore().toUpperCase()+"\n"); 
	                }
	           }
	            taPolicy.setText(sb.toString());
	            
	            
	        
	    }
}
}
