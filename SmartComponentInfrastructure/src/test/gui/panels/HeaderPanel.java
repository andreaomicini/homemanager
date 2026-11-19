package test.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.Main;
import test.listener.GeneralListener;
import test.controller.Controller;
import test.gui.frames.AddComponentFrame;
import test.gui.frames.AddConfiguratorFrame;
import test.gui.frames.HelpFrame;

public class HeaderPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	/*PROPRIETÀ*/
	private Controller controller;
	private JComboBox<String> components;
	private JButton addComponent;
	private JButton addConfigurator;
	private JButton removeComponent;
	private JButton helpButton;
	
	public HeaderPanel(Controller controller, GeneralListener listener){
		super();
		this.controller=controller;
		
		/*CARATTERISTICHE BASE*/
		//this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		
		/*CREAZIONE ELEMENTI*/
		JLabel logo=new JLabel();
		logo.setIcon(new ImageIcon(Main.class.getResource("/LPaaS.jpg")));
		
		JLabel choose=new JLabel("Componente selezionato");
		
		this.addComponent=new JButton();
		this.addComponent.setIcon(new ImageIcon(Main.class.getResource("/Add.png")));
		this.addComponent.setToolTipText("Aggiungi Componente");
		this.addComponent.addActionListener(this);
		
		this.addConfigurator=new JButton();
		this.addConfigurator.setIcon(new ImageIcon(Main.class.getResource("/configurator.png")));
		this.addConfigurator.setToolTipText("Aggiungi configuratore");
		this.addConfigurator.addActionListener(this);
		
		this.removeComponent=new JButton();
		this.removeComponent.setIcon(new ImageIcon(Main.class.getResource("/remove.png")));
		this.removeComponent.setToolTipText("Rimuovi componente");
		this.removeComponent.addActionListener(this);
		
		this.helpButton=new JButton();
		this.helpButton.setIcon(new ImageIcon(Main.class.getResource("/help.png")));
		this.helpButton.setToolTipText("Help");
		this.helpButton.addActionListener(this);
		
		this.components=new JComboBox<>();
		listener.setComponents(components);
		this.components.setBorder(BorderFactory.createMatteBorder(25, 0, 25, 0, this.getBackground()));
		
		/*AGGIUNTA ELEMENTI*/
		Box headerBox=new Box(BoxLayout.LINE_AXIS);
		headerBox.add(logo);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		headerBox.add(addComponent);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		headerBox.add(addConfigurator);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		headerBox.add(removeComponent);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		headerBox.add(helpButton);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		headerBox.add(choose);
		headerBox.add(components);
		headerBox.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(headerBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==this.addComponent){
			new AddComponentFrame("Aggiungi Componente",controller,components);
			
		}
		if(e.getSource()==this.addConfigurator){
			new AddConfiguratorFrame("Aggiungi Configuratore",controller,components);
			
		}
		if(e.getSource()==this.removeComponent){
			String componenteSelezionato=(String)this.components.getSelectedItem();
			if(!controller.unregister(componenteSelezionato)){
				JOptionPane.showMessageDialog(null, "Errore eliminazione componente!", "alert", JOptionPane.ERROR_MESSAGE);
			}
			
			this.components.removeAllItems();
			String[] componenti=controller.getComponents();
			for(String s : componenti){
				this.components.addItem(s);
			}
		}
		if(e.getSource()==this.helpButton){
			new HelpFrame("Help",controller);
			
		}
		
	}

}
