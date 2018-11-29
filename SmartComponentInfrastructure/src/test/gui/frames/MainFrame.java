package test.gui.frames;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import test.controller.Controller;
import test.listener.GeneralListener;
import test.gui.panels.ControllerPanel;
import test.gui.panels.HeaderPanel;
import test.gui.panels.InfoPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	public MainFrame(String string, Controller controller){
		super(string);
		
		/*INSERIMENTO MAIN PANELS*/
		Container c=this.getContentPane();
		GeneralListener listener=new GeneralListener(controller);
		
		c.add(new HeaderPanel(controller,listener),BorderLayout.NORTH);
		c.add(new InfoPanel(listener),BorderLayout.WEST);
		c.add(new ControllerPanel(controller,listener),BorderLayout.CENTER);
		
	}

}
