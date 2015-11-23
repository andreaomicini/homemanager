package it.unibo.homemanager.humanpresence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import alice.tucson.api.TucsonTupleCentreId;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.meteo.MeteoAgent;
import it.unibo.homemanager.userinterfaces.MainPanel;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
/**
 * @author 0000628533
 *
 */
public class ViewPresenceAgentPanel extends javax.swing.JPanel  {

	private ViewManageAgentPanel butlerPanel;
	private PresenceAgent presenceAgent;
	private TracePanel tp;
	private JLabel videoFeed;
	
	public ViewPresenceAgentPanel(ViewManageAgentPanel maPanel,
			PresenceAgent presenceAgent, TracePanel tp) {
		setPreferredSize(new Dimension(538, 343));
		setMinimumSize(new Dimension(538, 343));
		setMaximumSize(new Dimension(538, 343));
		
		this.butlerPanel=maPanel;
		this.presenceAgent=presenceAgent;
		this.tp=tp;

        JLabel lblPresenceAgent = new JLabel("Presence Agent");
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
        
        JButton jButton2 = new JButton();
        jButton2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		showFeedButtonActionPerformed(e);
        	}
        });
        jButton2.setText("Show Camera Feed");
        jButton2.setForeground(new Color(102, 0, 102));
        jButton2.setFont(new Font("Courier New", Font.BOLD, 14));
        
        videoFeed= new JLabel();
        add(videoFeed);
        add(lblPresenceAgent);
        add(jButton4);
        add(jButton3);
        add(jButton2);
	}
	private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuButtonActionPerformed
       
		this.hidePanel();
        butlerPanel.menu();
    }//GEN-LAST:event_menuButtonActionPerformed
	
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
		
		this.hidePanel();
        butlerPanel.showPanel();
	}//GEN-LAST:event_backButtonActionPerformed
	
	private void showFeedButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Devo mostrare immagini prese direttamente dal video che l'agente sta analizzando
		// con sopra un overlay 
		this.videoFeed.setVisible(true);
	}
	
	public void setImage(BufferedImage img)
	{
		this.videoFeed.setIcon(new ImageIcon(img));
	}
	public void init()
	{
		if(!isVisible())
			setVisible(true);
		videoFeed.setVisible(false);
		       
	}
	public void hidePanel() {
        if(isVisible())
            setVisible(false);
    }
	
	
}
