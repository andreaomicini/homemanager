package it.unibo.homemanager.humanpresence;

import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ViewSmartLampPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private ViewManageAgentPanel butlerPanel;
	private SmartLampAgent smartAgent;
	private TracePanel tp;
	private JPanel body;
	private ArrayList<LightPanel> panels;
	
	public ViewSmartLampPanel(ViewManageAgentPanel maPanel, SmartLampAgent smartAgent, TracePanel tp) {
		setPreferredSize(new Dimension(538, 343));
		setMinimumSize(new Dimension(538, 343));
		setMaximumSize(new Dimension(538, 343));
		
		this.butlerPanel=maPanel;
		this.smartAgent=smartAgent;
		this.tp=tp;
		this.panels=new ArrayList<LightPanel>();
        JLabel lblPresenceAgent = new JLabel(smartAgent.getName());
        lblPresenceAgent.setBounds(20, 12, 150, 21);
        lblPresenceAgent.setFont(new Font("Courier New", Font.BOLD, 18));
		
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
        
        body= new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        for (int i=0; i<this.smartAgent.getLamps().size(); i++){
        	LightPanel pan = new LightPanel(this.smartAgent.getLamps().get(i).getIdLamp(),
				this.smartAgent.getLamps().get(i).getIdRoom(),0);
        	panels.add(pan);
        	body.add(pan);
        }
        JScrollPane list = new JScrollPane(body);
        list.setAlignmentY(LEFT_ALIGNMENT);
        list.setPreferredSize(new Dimension(500, 300));
		list.setMinimumSize(new Dimension(500, 300));
		list.setMaximumSize(new Dimension(500, 300));
        list.setVisible(true);
        list.setBorder(BorderFactory.createTitledBorder(null, "Current state of Lamps:", TitledBorder.DEFAULT_JUSTIFICATION
			, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Courier New", 1, 11), new java.awt.Color(0, 0, 51)));
        
        add(lblPresenceAgent);
        add(jButton4);
        add(jButton3);
        add(list);
	}
	
	private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) { 
		this.hidePanel();
        butlerPanel.menu();
    }
	
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.hidePanel();
        butlerPanel.showPanel();
	}
	
	public void resetImage(int idRoom,int status)
	{
		for (LightPanel pan : panels){
			if (pan.getIdRoom()==idRoom){
				pan.setStatus(status);
			}
		}
	}
	
	public void init()
	{
		if(!isVisible()) setVisible(true);
	}
	
	public void hidePanel() 
	{
        if(isVisible()) setVisible(false);
    }
}
