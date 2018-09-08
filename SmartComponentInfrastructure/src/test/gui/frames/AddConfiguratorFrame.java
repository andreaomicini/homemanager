package test.gui.frames;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import test.controller.Controller;

public class AddConfiguratorFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	/*PROPRIETÀ*/
	private Controller controller;
	private JComboBox<String> components;
	private JButton aggiungi;
	private JTextField username;
	private JTextField password;
	
	public AddConfiguratorFrame(String title, Controller controller, JComboBox<String> components){
		super(title);
		this.controller=controller;
		this.components=components;
		
		this.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.40),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.40));
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		Container c=this.getContentPane();
		
		/*CREAZIONE COMPONENTI*/
		JLabel usernameLabel=new JLabel("Username");
		
		JLabel passwordLabel=new JLabel("Password");
		
		this.username=new JTextField(30);
		
		this.password=new JTextField(30);
		
		this.aggiungi=new JButton("Aggiungi");
		this.aggiungi.addActionListener(this);
		
		
		/*AGGIUNTA COMPONENTI*/
		Box finalBox=new Box(BoxLayout.PAGE_AXIS);
		Box nomeBox=new Box(BoxLayout.LINE_AXIS);
		Box urlBox=new Box(BoxLayout.LINE_AXIS);
		
		nomeBox.add(Box.createRigidArea(new Dimension(30,0)));
		nomeBox.add(usernameLabel);
		nomeBox.add(Box.createHorizontalGlue());
		nomeBox.add(username);
		nomeBox.add(Box.createRigidArea(new Dimension(30,0)));
		
		urlBox.add(Box.createRigidArea(new Dimension(30,0)));
		urlBox.add(passwordLabel);
		urlBox.add(Box.createHorizontalGlue());
		urlBox.add(password);
		urlBox.add(Box.createRigidArea(new Dimension(30,0)));
		
		
		finalBox.add(Box.createRigidArea(new Dimension(0,20)));
		finalBox.add(nomeBox);
		finalBox.add(Box.createRigidArea(new Dimension(0,20)));
		finalBox.add(urlBox);
		finalBox.add(Box.createRigidArea(new Dimension(0,20)));
		finalBox.add(aggiungi);
		finalBox.add(Box.createRigidArea(new Dimension(0,20)));
		
		JPanel panel=new JPanel();
		panel.add(finalBox);
		c.add(panel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.aggiungi){
			String selected=(String)this.components.getSelectedItem();
			if(!controller.addConfigurator(selected,this.username.getText(),this.password.getText())){
				JOptionPane.showMessageDialog(null, "Errore registrazione configuratore!", "alert", JOptionPane.ERROR_MESSAGE);
			}else{
				this.setVisible(false);
			}
		}
	}

}
