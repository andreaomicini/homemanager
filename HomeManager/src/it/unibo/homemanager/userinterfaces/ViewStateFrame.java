package it.unibo.homemanager.userinterfaces;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.scheduler.RefreshThread;
import it.unibo.homemanager.scheduler.ScheduledDevice;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;

public class ViewStateFrame extends JFrame {

	private JPanel contentPane;
	private JScrollPane spDev;
	private JTextArea taDev;
	
	private RefreshThread refreshView;
	private int refreshTime = 10000; //time (in ms) between 2 refresh
	//TuCSoN need almost 5 seconds to load all the devices!
	//Don't set a refreshTime under 5 seconds
	private JLabel lblNewLabel;
	private JScrollPane spSch;
	private JTextArea taSch;


	/**
	 * Create the frame.
	 */
	public ViewStateFrame(Vector t, ServiceFactory sf, ArrayList<ScheduledDevice> schDevs) {
		setTitle("View State");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//before closing, the thresh must be terminated
		addWindowListener(new WindowAdapter() { //actions to do before closing the frame
	        @Override
	        public void windowClosing(WindowEvent event) {
	        	refreshView.interrupt();//terminate the thread
	            dispose();//close the frame
	        }
	    });
		setBounds(100, 100, 548, 307);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblViewDevices = new JLabel("Devices and their current state");
		lblViewDevices.setBounds(10, 11, 223, 14);
		contentPane.add(lblViewDevices);
		
		spDev = new JScrollPane();
		spDev.setBounds(10, 31, 512, 90);
		contentPane.add(spDev);
		
		taDev = new JTextArea();
		taDev.setText("");
		taDev.setToolTipText("");
		taDev.setRows(10);
		taDev.setEditable(false);
		taDev.setTabSize(27);
		spDev.setViewportView(taDev);
		
		lblNewLabel = new JLabel("Schedule");
		lblNewLabel.setBounds(10, 132, 152, 14);
		contentPane.add(lblNewLabel);
		
		spSch = new JScrollPane();
		spSch.setBounds(10, 151, 512, 90);
		contentPane.add(spSch);
		
		taSch = new JTextArea();
		taSch.setFont(new Font("Courier New", Font.PLAIN, 13));
		taSch.setToolTipText("");
		taSch.setRows(10);
		taSch.setEditable(false);
		spSch.setViewportView(taSch);
		
		JLabel lblNewLabel_1 = new JLabel("This frame is updated every 10 seconds");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_1.setBounds(331, 252, 191, 14);
		contentPane.add(lblNewLabel_1);
		
		refreshView = new RefreshThread(t, sf, schDevs, refreshTime, taDev, taSch); //thread used for the refresh of the textArea
		refreshView.start();
	}
}
