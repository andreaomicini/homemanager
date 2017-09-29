package it.unibo.homemanager.userinterfaces.agents;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class ViewAllThresholdsPolicy extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> toView;
	private JTextArea textArea;
	
	public ViewAllThresholdsPolicy(List<String> toView) {
		setTitle("View all");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		getContentPane().add(textArea, BorderLayout.CENTER);
		
		this.toView = toView;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		initTextArea();
		pack();
		
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		int screenWidth = (int) defaultToolkit.getScreenSize().getWidth();
		int screenHeight = (int) defaultToolkit.getScreenSize().getHeight();
		
		int windowWidth = (int) getSize().getWidth();
		int windowHeight = (int) getSize().getHeight();
		
		int x = (screenWidth - windowWidth) / 2;
		int y = (screenHeight - windowHeight) / 2;
		setLocation(x, y);
	}

	
	private void initTextArea(){
		for(String row : toView)
			textArea.append(row + System.lineSeparator());
	}
}
