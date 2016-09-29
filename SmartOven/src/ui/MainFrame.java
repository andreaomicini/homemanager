package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.agent.OvenAgent;
import model.agent.StateChangeListener;
import model.sensors.SensorsManager;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private OvenAgent ovenAgent;
	
	private JTextPane textRecipe;

	public MainFrame(OvenAgent ovenAgent) {
		this.ovenAgent = ovenAgent;
		
		initComponents();
		initGUI();
		
		getOvenAgent().addListener(new StateChangeListener() {
			public void onStateChanged() {
				onStateChangedEvent();
			}
		});
		
		onStateChangedEvent();
		
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent event) { }
			
			public void windowIconified(WindowEvent event) { }
			
			public void windowDeiconified(WindowEvent event) { }
			
			public void windowDeactivated(WindowEvent event) { }
			
			public void windowClosing(WindowEvent event) { }
			
			public void windowClosed(WindowEvent event) {
				try {
					getOvenAgent().removePresenceInformation();
				} catch (Exception e) { }
			}
			
			public void windowActivated(WindowEvent event) { }
		});
	}

	public OvenAgent getOvenAgent() {
		return (ovenAgent);
	}

	public JTextPane getTextRecipe() {
		return (textRecipe);
	}
	private void initComponents() {
		textRecipe = new JTextPane();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		// JLabel labelTitle = new JLabel("Name: " + getOvenAgent().getName());
		// mainPanel.add(labelTitle);
		
		getTextRecipe().setEditable(false);
		getTextRecipe().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		StyledDocument document = getTextRecipe().getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		document.setParagraphAttributes(0, document.getLength(), center, false);
		
		JScrollPane textRecipeScrollPane = new JScrollPane(getTextRecipe());
		textRecipeScrollPane.setPreferredSize(new Dimension(300, 250));
		textRecipeScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel manageOvenPanel = new JPanel();
		manageOvenPanel.setLayout(new BoxLayout(manageOvenPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(textRecipeScrollPane);
		
		getContentPane().add(mainPanel);
		
		pack();
		setTitle("OvenAgent" + " - " + getOvenAgent().getName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void onStateChangedEvent() {
		SensorsManager sensorsManager = SensorsManager.getInstance();
		
		if ( getOvenAgent().getState() ) {
			getTextRecipe().setText("State: ON");
			sensorsManager.turnOnGreenLed();
		} else { // if ( !getOvenAgent().getState() )
			getTextRecipe().setText("State: OFF");
			sensorsManager.turnOffGreenLed();
		}
		
		getTextRecipe().setText(getTextRecipe().getText() + System.lineSeparator() + System.lineSeparator());
		
		if ( getOvenAgent().getCurrentRecipe() != null ) {
			getTextRecipe().setText( getTextRecipe().getText() + getOvenAgent().getCurrentRecipe().toString() );
			sensorsManager.turnOnRedLed();
		} else { // if (getOvenAgent().getCurrentRecipe() == null)
			getTextRecipe().setText(getTextRecipe().getText() + "");
			sensorsManager.turnOffRedLed();
		}
	}
}
