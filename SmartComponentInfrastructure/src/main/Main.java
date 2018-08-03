package main;

import java.awt.Toolkit;

import javax.swing.JFrame;

import test.controller.Controller;
import test.gui.frames.MainFrame;

public class Main {

	public static void main(String[] args) {
		
		Controller controller=new Controller();
		
		JFrame frame=new MainFrame("LPaaS Test",controller);
		frame.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.80),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.80));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
}
