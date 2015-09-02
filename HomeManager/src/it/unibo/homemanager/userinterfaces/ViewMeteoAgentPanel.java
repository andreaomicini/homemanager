/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.homemanager.userinterfaces;

import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.TucsonTupleCentreId;
import it.unibo.homemanager.meteo.AgentManager;
import it.unibo.homemanager.meteo.MeteoAgent;
import it.unibo.homemanager.tablemap.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;

/**
 *
 * @author Alessandro
 */
public class ViewMeteoAgentPanel extends javax.swing.JPanel {

	private ViewManageAgentPanel butlerPanel;
	private MeteoAgent meteoAgent;
	private TracePanel tp;
	private static List<String> siti;

	public ViewMeteoAgentPanel(ViewManageAgentPanel maPanel, MeteoAgent meteoAgent, TracePanel tp) {
		setPreferredSize(new Dimension(538, 343));
		setMinimumSize(new Dimension(538, 343));
		setMaximumSize(new Dimension(538, 343));
		this.butlerPanel=maPanel;
        this.meteoAgent=meteoAgent;
        this.tp=tp;
        
        //inizializzo la lista dei siti
        siti=meteoAgent.getSiti();
        
        
        JLabel lblMeteoAgent = new JLabel("Meteo Agent");
        lblMeteoAgent.setBounds(20, 12, 121, 21);
        lblMeteoAgent.setFont(new Font("Courier New", Font.BOLD, 18));
        
        JButton jButton4 = new JButton();
        jButton4.setBounds(240, 11, 109, 25);
        jButton4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		backButtonActionPerformed(arg0);
        	}
        });
        jButton4.setText("Back");
        jButton4.setForeground(new Color(102, 0, 102));
        jButton4.setFont(new Font("Courier New", Font.BOLD, 14));
        
        JButton jButton3 = new JButton();
        jButton3.setBounds(367, 11, 110, 25);
        jButton3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		menuButtonActionPerformed(e);
        	}
        });
        jButton3.setText("Menu");
        jButton3.setForeground(new Color(102, 0, 102));
        jButton3.setFont(new Font("Courier New", Font.BOLD, 14));
        
        JLabel lblSelezionaSitoMeteo = new JLabel("Seleziona sito meteo");
        lblSelezionaSitoMeteo.setBounds(20, 48, 140, 14);
        lblSelezionaSitoMeteo.setFont(new Font("Courier New", Font.PLAIN, 11));
        
        siticomboBox = new JComboBox();
        siticomboBox.setBounds(168, 47, 237, 20);
        siticomboBox.removeAllItems();
        for(String s : siti)
        {
        	siticomboBox.addItem(s);
        }
        siticomboBox.setSelectedIndex(-1);
        
        siticomboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sitiComboBoxActionPerformed(evt);
            }
        });
        
        
        JLabel imagelabel = new JLabel("");
        imagelabel.setBounds(20, 73, 140, 108);
        imagelabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icone-meteo.jpg")));
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(216, 70, 100, 132);
        lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/google-iconBis.png")));
        
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setBounds(306, 63, 99, 127);
        lblNewLabel_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/OpenWeatherMap_logo.png")));
        
        JLabel label = new JLabel("");
        label.setBounds(392, 87, 100, 100);
        label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yahoo.jpg")));
        
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 192, 508, 43);
        
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(20, 246, 508, 43);
        
        scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(20, 300, 508, 43);
        
        table_2 = new JTable();
        scrollPane_2.setViewportView(table_2);
        
        table_1 = new JTable();
        scrollPane_1.setViewportView(table_1);
        setLayout(null);
        
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateColumnsFromModel(false);
        scrollPane.setViewportView(table);
        add(scrollPane);
        add(scrollPane_1);
        add(scrollPane_2);
        add(lblMeteoAgent);
        add(jButton4);
        add(jButton3);
        add(lblSelezionaSitoMeteo);
        add(imagelabel);
        add(lblNewLabel);
        add(lblNewLabel_1);
        add(siticomboBox);
        add(label);
	}
	
	private JComboBox siticomboBox;
	private JTable table;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JTable table_1;
	private JTable table_2;
	

	private void sitiComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jlistAgentComboBoxActionPerformed
        if(siti.contains(siticomboBox.getSelectedItem())) 
        {
        	//cerca nel centro di tuple e stampa fuori le tuple
        	String[] columnNames = meteoAgent.getColumnNames(""+siticomboBox.getSelectedItem());
        	if(columnNames!=null)
        	{
        		Object[][] data1 = meteoAgent.getData1(""+siticomboBox.getSelectedItem());
        		Object[][] data2 = meteoAgent.getData2(""+siticomboBox.getSelectedItem());
        		Object[][] data3 = meteoAgent.getData3(""+siticomboBox.getSelectedItem());
        		if(data1!=null)
        		{
        			table = new JTable(data1, Arrays.copyOfRange(columnNames, 0, 6));
        			scrollPane.setViewportView(table);
        		}
        		if(data2!=null)
        		{
        			table_1 = new JTable(data2, Arrays.copyOfRange(columnNames, 6,meteoAgent.getIndex(""+siticomboBox.getSelectedItem()) ));
        			scrollPane_1.setViewportView(table_1);
        		}
        		if(data3!=null)
        		{
        			table_2 = new JTable(data3, Arrays.copyOfRange(columnNames,meteoAgent.getIndex(""+siticomboBox.getSelectedItem()), columnNames.length));
        			scrollPane_2.setViewportView(table_2);
        		}
        		else
        		{
        			scrollPane_2.setViewportView(null);
        		}
        		

        		
        	}
        }
        
    }//GEN-LAST:event_jlistAgentComboBoxActionPerformed
	

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
	            setVisible(true);
	    }
}
