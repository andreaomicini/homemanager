package test.gui.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import test.controller.Controller;
import test.listener.GeneralListener;

public class ControllerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ControllerPanel(Controller controller, GeneralListener listener){
		super();
		
		//this.setBackground(Color.WHITE);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		/*PARTE THEORIES*/
		JLabel lab1=new JLabel("Theories");
		JLabel lab2=new JLabel("Version");
		JLabel lab3=new JLabel("Functor");
		JButton createT=new JButton("Crea Teoria"); 
		JButton replaceT=new JButton("Sostituisci Teoria");
		JButton removeT=new JButton("Elimina Teoria"); 
		JButton getT=new JButton("Cerca Teoria"); 
		JButton addVersionT=new JButton("Aggiungi Versione"); 
		
		JComboBox<String> theories=new JComboBox<>();
		JComboBox<String> versions=new JComboBox<>();
		JComboBox<String> functors=new JComboBox<>();
		JTextArea modifyAreaT=new JTextArea(8,2);
		modifyAreaT.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		JPanel theoriesBox=new JPanel();
		Box boxT_1=new Box(BoxLayout.PAGE_AXIS);
		Box boxT_2=new Box(BoxLayout.LINE_AXIS);
		Box boxT_3=new Box(BoxLayout.LINE_AXIS);
		
		boxT_2.add(lab1);
		boxT_2.add(theories);
		boxT_2.add(Box.createHorizontalGlue());
		boxT_2.add(lab2);
		boxT_2.add(versions);
		boxT_2.add(Box.createHorizontalGlue());
		boxT_2.add(lab3);
		boxT_2.add(functors);
		
		boxT_3.add(getT);
		boxT_3.add(addVersionT);
		boxT_3.add(removeT);
		boxT_3.add(replaceT);
		boxT_3.add(createT);
		
		boxT_1.add(boxT_2);
		boxT_1.add(boxT_3);
		boxT_1.add(modifyAreaT);
		
		theoriesBox.add(boxT_1);	
		
		this.add(theoriesBox);
		
		listener.setCreateT(createT);
		listener.setRemoveT(removeT);
		listener.setReplaceT(replaceT);
		listener.setAddVersionT(addVersionT);
		listener.setGetT(getT);
		listener.setTheories(theories);
		listener.setVersions(versions);
		listener.setFunctors(functors);
		listener.setModifyAreaT(modifyAreaT);
		
		/*PARTE GOALS*/
		JButton createG=new JButton("Crea Goal");
		JButton replaceG=new JButton("Sostituisci Goal");
		JButton removeG=new JButton("Elimina Goal");
		JButton getG=new JButton("Cerca Goal");
		JButton addGoal=new JButton("Aggiungi Goal");
		JLabel label=new JLabel("Goals");
		JTextArea modifyAreaG=new JTextArea(8,2);
		modifyAreaG.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		JComboBox<String> goals=new JComboBox<>();
		
		JPanel goalsPanel=new JPanel();
		goalsPanel.setLayout(new GridLayout(2,3));
		goalsPanel.add(goals); goalsPanel.add(createG);
		goalsPanel.add(replaceG);	goalsPanel.add(removeG);
		goalsPanel.add(getG);	goalsPanel.add(addGoal);
		
		//this.add(Box.createRigidArea(new Dimension(0,20)));
		this.add(goalsPanel);
		
		JPanel goalsBox=new JPanel();
		Box boxG_1=new Box(BoxLayout.PAGE_AXIS);
		Box boxG_2=new Box(BoxLayout.LINE_AXIS);
		Box boxG_3=new Box(BoxLayout.LINE_AXIS);
		
		boxG_2.add(getG);
		boxG_2.add(addGoal);
		boxG_2.add(removeG);
		boxG_2.add(replaceG);
		boxG_2.add(createG);
		
		boxG_3.add(label);
		boxG_3.add(goals);
		
		boxG_1.add(boxG_3);
		boxG_1.add(boxG_2);
		boxG_1.add(modifyAreaG);
		
		goalsBox.add(boxG_1);	
		
		this.add(goalsBox);
		
		listener.setGoals(goals);
		listener.setCreateG(createG);
		listener.setReplaceG(replaceG);
		listener.setRemoveG(removeG);
		listener.setGetG(getG);
		listener.setAddGoal(addGoal);
		listener.setModifyAreaG(modifyAreaG);
		
		/*PARTE SOLUTIONS*/
		JButton getS=new JButton("Cerca Solution");
		
		JPanel solutionBox=new JPanel();
		solutionBox.add(getS);
		
		//this.add(Box.createRigidArea(new Dimension(0,20)));
		this.add(solutionBox);
		
		listener.setGetS(getS);
	}

}
