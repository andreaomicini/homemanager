package test.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import classes.Solution;
import test.controller.Controller;
import exceptions.NotCreatedException;

public class GeneralListener implements ActionListener {
	
	private JComboBox<String> components;
	private Controller controller;
	
	private JButton createT;
	private JButton replaceT;
	private JButton removeT;
	private JButton getT;
	private JButton addVersionT;
	private JComboBox<String> theories;
	private JComboBox<String> versions;
	private JComboBox<String> functors;
	private JTextArea modifyAreaT;
	
	private JButton createG;
	private JButton replaceG;
	private JButton removeG;
	private JButton getG;
	private JButton addGoal;
	private JComboBox<String> goals;
	private JTextArea modifyAreaG;
	
	private JButton getS;
	
	private JTextArea infoArea;
	
	public GeneralListener(Controller controller){
		this.controller=controller;
	}

	public void setComponents(JComboBox<String> components) {
		this.components = components;
		this.components.addActionListener(this);
	}

	public void setCreateT(JButton createT) {
		this.createT = createT;
		this.createT.addActionListener(this);
	}

	public void setReplaceT(JButton replaceT) {
		this.replaceT = replaceT;
		this.replaceT.addActionListener(this);
	}

	public void setRemoveT(JButton removeT) {
		this.removeT = removeT;
		this.removeT.addActionListener(this);
	}

	public void setGetT(JButton getT) {
		this.getT = getT;
		this.getT.addActionListener(this);
	}

	public void setAddVersionT(JButton addVersionT) {
		this.addVersionT = addVersionT;
		this.addVersionT.addActionListener(this);
	}

	public void setTheories(JComboBox<String> theories) {
		this.theories = theories;
		this.theories.addActionListener(this);
	}

	public void setVersions(JComboBox<String> versions) {
		this.versions = versions;
		this.versions.addActionListener(this);
	}

	public void setFunctors(JComboBox<String> functors) {
		this.functors = functors;
		this.functors.addActionListener(this);
	}

	public void setModifyAreaT(JTextArea modifyAreaT) {
		this.modifyAreaT = modifyAreaT;
	}

	public void setCreateG(JButton createG) {
		this.createG = createG;
		this.createG.addActionListener(this);
	}

	public void setReplaceG(JButton replaceG) {
		this.replaceG = replaceG;
		this.replaceG.addActionListener(this);
	}

	public void setRemoveG(JButton removeG) {
		this.removeG = removeG;
		this.removeG.addActionListener(this);
	}

	public void setGetG(JButton getG) {
		this.getG = getG;
		this.getG.addActionListener(this);
	}

	public void setAddGoal(JButton addGoal) {
		this.addGoal = addGoal;
		this.addGoal.addActionListener(this);
	}

	public void setGoals(JComboBox<String> goals) {
		this.goals = goals;
		this.goals.addActionListener(this);
	}

	public void setGetS(JButton getS) {
		this.getS = getS;
		this.getS.addActionListener(this);
	}

	public void setInfoArea(JTextArea infoArea) {
		this.infoArea = infoArea;
	}
	
	public void setModifyAreaG(JTextArea modifyAreaG) {
		this.modifyAreaG = modifyAreaG;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String componenteSelezionato=(String)this.components.getSelectedItem();
		String teoria=(String)this.theories.getSelectedItem();
		String functor=(String)this.functors.getSelectedItem();
		String goal=(String)this.goals.getSelectedItem();
		long versione=Long.parseLong((((String)this.versions.getSelectedItem())==null) ? "-1":(String)this.versions.getSelectedItem());
		
		if(e.getSource()==this.components){
			if(this.components.getItemCount()!=controller.getComponents().length/* || this.components.getItemCount()==0*/)
				update();
			init();
		}
		if(e.getSource()==this.getT){
			
			String[] result=null;
			if(functor.equals("None")){
				result=controller.getFacts(componenteSelezionato, teoria, versione);
			}else{
				result=controller.getFactsWithFunctor(componenteSelezionato, teoria, versione, functor,-1,-1);
			}
			
			String text="";
			if(result.length!=0){
				for(String s : result){
					text+=s+"\n";
				}
			}
			
			this.infoArea.setText(text);
		}
		if(e.getSource()==this.theories){
			this.versions.removeActionListener(this);
			
			this.versions.removeAllItems();
			
			for(int i=0;i<=controller.getCountVersion(componenteSelezionato, teoria);i++){
				this.versions.addItem(""+i);
			}
			
			this.versions.addActionListener(this);
			this.functors.removeActionListener(this);
			
			this.functors.removeAllItems();
			this.functors.addItem("None");
			
			this.functors.addActionListener(this);
		}
		if(e.getSource()==this.versions){
			if(!teoria.equals("None") && versione>=0){
				this.functors.removeActionListener(this);
				
				this.functors.removeAllItems();
				this.functors.addItem("None");
				
				String[] facts=controller.getFacts(componenteSelezionato, teoria, versione);
				for(String s : facts){
					String single_functor="";
					for(int i=0;i<s.length();i++){
						if(s.charAt(i)!=':' && s.charAt(i)!='(' && s.charAt(i)!='.')
							single_functor+=s.charAt(i);
						else{
							break;
						}
					}
					this.functors.addItem(single_functor);
				}
				this.functors.addActionListener(this);
			}
		}
		if(e.getSource()==this.addVersionT){
			String text=this.modifyAreaT.getText();
			
			String[] teorie=text.split("\n");
			try {
				if(!controller.addTheoryVersion(componenteSelezionato, teoria, teorie)){
					JOptionPane.showMessageDialog(null, "Errore inserimento nuova versione\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
				}else{
					//Aggiorno versioni
					this.versions.removeActionListener(this);
					
					this.versions.removeAllItems();
					
					for(int i=0;i<=controller.getCountVersion(componenteSelezionato, teoria);i++){
						this.versions.addItem(""+i);
					}
					
					this.versions.addActionListener(this);
				}
			} catch (NotCreatedException e1) {
				JOptionPane.showMessageDialog(null, "Teoria non esistente", "alert", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
		if(e.getSource()==this.removeT){
			if(!controller.removeTheory(componenteSelezionato, teoria)){
				JOptionPane.showMessageDialog(null, "Errore eliminazione teoria\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
			}else{
				//ripopolo le teorie disponibili
				init();
			}
		}
		if(e.getSource()==this.replaceT){
			String text=this.modifyAreaT.getText();
			
			String[] teorie=text.split("\n");
			
			if(!controller.replaceTheory(componenteSelezionato, teoria, teorie)){
				JOptionPane.showMessageDialog(null, "Errore replace teoria\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(e.getSource()==this.createT){
			String text=this.modifyAreaT.getText();
			
			String[] spices=text.split("\n");
			String titolo=null;
			List<String> teorie=new ArrayList<>();
			
			for(String s : spices){
				if(s.startsWith("TITOLO:"))
					titolo=s.split("TITOLO:")[1];
				else{
					teorie.add(s);
				}
			}
			
			if(titolo==null){
				JOptionPane.showMessageDialog(null, "Titolo mancante", "alert", JOptionPane.ERROR_MESSAGE);

			}else{
				if(!controller.createTheory(componenteSelezionato, titolo, teorie.toArray(new String[teorie.size()]))){
					JOptionPane.showMessageDialog(null, "Errore creazione teoria\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			this.modifyAreaT.setText("");
			init();
		}
		
		if(e.getSource()==this.getG){
			String[] goals= controller.getGoals(componenteSelezionato, goal);
			
			String text="";
			if(goals.length!=0){
				for(String s : goals){
					text+=s+"\n";
				}
			}
			
			this.infoArea.setText(text);
		}
		if(e.getSource()==this.addGoal){
			String text=this.modifyAreaG.getText();
			if(text.equals(""))
				JOptionPane.showMessageDialog(null, "Nessun goal inserito", "alert", JOptionPane.ERROR_MESSAGE);
			else{
				String[] goals=text.split("\n");
				
				if(!controller.addGoals(componenteSelezionato, goal, goals)){
					JOptionPane.showMessageDialog(null, "Errore aggiunta goals \nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
				}else{
					this.modifyAreaG.setText("");
				}
			}
		}
		if(e.getSource()==this.removeG){
			if(!controller.removeGoal(componenteSelezionato, goal))
				JOptionPane.showMessageDialog(null, "Errore rimozione goals \nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
			else{
				init();
			}
		}
		
		if(e.getSource()==this.replaceG){
			String text=this.modifyAreaG.getText();
			
			String[] goals=text.split("\n");
			
			if(!controller.replaceGoal(componenteSelezionato, goal, goals)){
				JOptionPane.showMessageDialog(null, "Errore replace goal\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
			}
			this.modifyAreaG.setText("");
		}
		
		if(e.getSource()==this.createG){
			String text=this.modifyAreaG.getText();
			
			String[] spices=text.split("\n");
			String titolo=null;
			List<String> goals=new ArrayList<>();
			
			for(String s : spices){
				if(s.startsWith("TITOLO:"))
					titolo=s.split("TITOLO:")[1];
				else{
					goals.add(s);
				}
			}
			
			if(titolo==null){
				JOptionPane.showMessageDialog(null, "Titolo mancante", "alert", JOptionPane.ERROR_MESSAGE);

			}else{
				if(!controller.createGoal(componenteSelezionato, titolo, goals.toArray(new String[goals.size()]))){
					JOptionPane.showMessageDialog(null, "Errore creazione goal\nAssicurarsi di avere i diritti di configuratore", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			this.modifyAreaG.setText("");
			init();
		}

		if(e.getSource()==this.getS){
			Solution result=controller.getSolutions(componenteSelezionato, teoria, versione, goal, null, -1, -1, null, null);
			
			if(result!=null)
				this.infoArea.setText(result.toString());
			else{
				JOptionPane.showMessageDialog(null, "Errore solutions!", "alert", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

	private void update(){
		this.components.removeActionListener(this);
		this.components.removeAllItems();
		String[] componenti=controller.getComponents();
		for(String s : componenti){
			this.components.addItem(s);
		}
		
		this.components.addActionListener(this);
	}
	
	private void init(){
		String componenteSelezionato=(String)this.components.getSelectedItem();
		this.infoArea.setText("SELEZIONATO: "+componenteSelezionato);
		
		//POPOLO THEORIES
		this.theories.removeActionListener(this);
		
		this.theories.removeAllItems();
		String[] teorie=controller.getTheories(componenteSelezionato);
		if(teorie!=null){
			for(String s : teorie){
				this.theories.addItem(s);
			}
		}
		
		this.theories.addActionListener(this);
		
		//POPOLO VERSIONS
		this.versions.removeActionListener(this);
		
		this.versions.removeAllItems();
		this.versions.addItem("0");
		
		this.versions.addActionListener(this);
		
		//POPOLO FUNCTORS
		this.functors.removeActionListener(this);
		
		this.functors.removeAllItems();
		this.functors.addItem("None");
		
		this.functors.addActionListener(this);
		
		//POPOLO GOALS
		this.goals.removeActionListener(this);
		
		this.goals.removeAllItems();
		String[] goals=controller.getGoalLists(componenteSelezionato);
		if(goals!=null){
			for(String s : goals){
				this.goals.addItem(s);
			}
		}
		this.goals.addActionListener(this);
	}
	
}
