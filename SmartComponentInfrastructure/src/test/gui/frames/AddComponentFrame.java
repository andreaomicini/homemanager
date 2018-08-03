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
import exceptions.AlreadyRegisteredException;

public class AddComponentFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	/*PROPRIETÀ*/
	private Controller controller;
	private JComboBox<String> components;
	private JButton aggiungi;
	private JTextField nome;
	private JTextField url;
	
	public AddComponentFrame(String title, Controller controller, JComboBox<String> components){
		super(title);
		this.controller=controller;
		this.components=components;
		
		this.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.40),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.40));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		Container c=this.getContentPane();
		
		/*CREAZIONE COMPONENTI*/
		JLabel nomeLabel=new JLabel("Nome Componente");
		
		JLabel urlLabel=new JLabel("URL");
		
		this.nome=new JTextField(30);
		
		this.url=new JTextField(30);
		
		this.aggiungi=new JButton("Aggiungi");
		this.aggiungi.addActionListener(this);
		
		
		/*AGGIUNTA COMPONENTI*/
		Box finalBox=new Box(BoxLayout.PAGE_AXIS);
		Box nomeBox=new Box(BoxLayout.LINE_AXIS);
		Box urlBox=new Box(BoxLayout.LINE_AXIS);
		
		nomeBox.add(Box.createRigidArea(new Dimension(30,0)));
		nomeBox.add(nomeLabel);
		nomeBox.add(Box.createHorizontalGlue());
		nomeBox.add(nome);
		nomeBox.add(Box.createRigidArea(new Dimension(30,0)));
		
		urlBox.add(Box.createRigidArea(new Dimension(30,0)));
		urlBox.add(urlLabel);
		urlBox.add(Box.createHorizontalGlue());
		urlBox.add(url);
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
			try {
				if(!controller.register(this.nome.getText(),this.url.getText())){
					JOptionPane.showMessageDialog(null, "Errore registrazione componente!", "alert", JOptionPane.ERROR_MESSAGE);
				}else{
					this.components.getActionListeners()[0].actionPerformed(new ActionEvent(this.components, ActionEvent.ACTION_PERFORMED, "Anything"));
					this.setVisible(false);
				}
			} catch (AlreadyRegisteredException e1) {
				JOptionPane.showMessageDialog(null, "Componente già registrato", "alert", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
