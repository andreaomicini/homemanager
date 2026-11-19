package test.gui.panels;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import test.listener.GeneralListener;


public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/*PROPRIETÀ*/
	private JTextArea infoArea;
	
	public InfoPanel(GeneralListener listener){
		super();
		this.infoArea=new JTextArea(30,30);
		this.infoArea.setEditable(false);
		listener.setInfoArea(infoArea);
		JScrollPane pane=new JScrollPane(infoArea);
		this.add(pane);
		//this.setBackground(Color.WHITE);
		
	}

}
