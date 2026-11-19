package test.gui.frames;

import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import test.controller.Controller;

public class HelpFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public HelpFrame(String title, Controller controller) {
		super(title);
		
		this.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.50),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.50));
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		Container c=this.getContentPane();
		
		JTextArea info=new JTextArea(40,40);
		/*INSERIRE TESTO*/
		StringBuilder message=new StringBuilder();
		message.append("-----GUIDA ALL'USO-----\n\n");
		message.append("--PARTE SUPERIORE\n");
		message.append("Aggiungi Componente: Permette di registrare un nuovo componente da testare\n");
		message.append("Aggiungi Configuratore: Permette di associare i permessi di configuratore ad un particolare componente"
				+"; necessario per operazioni di modifica di theory o goal\n");
		message.append("Rimuovi Componente: Rimuove un componente dalla lista degli elementi da testare\n\n");
		message.append("--PARTE DI CONTROLLO\n");
		message.append("Suddivisa in tre parti: Theory Control, Goal Control, Solutions\n");
		message.append("Ogni operazione eseguita in queste parti è realtiva al componente selezionato nella ComboBox superiore!\n\n");
		message.append("--THEORY CONTROL\n");
		message.append("Le tre ComboBox rappresentano rispettivamente l'elenco delle teorie,\n delle versioni relative ad una teoria"
				+ " e dei functor relativi ad una specifica versione di una specifica teoria\n");
		message.append("Tramite il 'Cerca Teoria' è possibile andare a visualizzare la teoria selezionata, eventualmente filtrando per versione e functor\n");
		message.append("Tramite 'Aggiungi Versione' è possibile aggiungere una nuova versione alla teoria selezionata,\n"
				+ "i facts con cui si intende inizializzare la versione vanno scritti nella TextArea sottostante\n");
		message.append("Tramite 'Elimina Teoria' si elimina la teoria selezionata\n");
		message.append("Tramite 'Sostituisci Teoria' si andrà a sostituire la versione 0 della teoria selezionata\n"
				+ "i facts da sostituire vanno aggiunti nella TextArea sottostante\n");
		message.append("Tramite 'Crea Teoria' si andrà a creare una nuova teoria\n"
				+ "i facts per inizializzare la teoria vanno inseriti nella TextArea sottostante\n"
				+ "IMPORTANTE: Per creare correttamente la teoria va inserita una riga del tipo TITOLO:<titoloTeoria>\n\n");
		message.append("--GOAL CONTROL\n");
		message.append("La ComboBox rappresenta l'elenco delle GoalLists\n");
		message.append("Tramite il 'Cerca Goal' è possibile andare a visualizzare i goal della GoalList selezionata\n");
		message.append("Tramite 'Aggiungi Goal' è possibile aggiungere un nuovo Goal alla GoalList selezionata,\n"
				+ "i goals vanno scritti nella TextArea sottostante\n");
		message.append("Tramite 'Elimina Goal' si elimina la GoalList selezionata\n");
		message.append("Tramite 'Sostituisci Goal' si andrà a sostituire la GoalList selezionata\n"
				+ "i goal da sostituire vanno aggiunti nella TextArea sottostante\n");
		message.append("Tramite 'Crea Goal' si andrà a creare una nuova GoalList\n"
				+ "i goals per inizializzare la GoalList vanno inseriti nella TextArea sottostante\n"
				+ "IMPORTANTE: Per creare correttamente la GoalList va inserita una riga del tipo TITOLO:<titoloGoalList>\n\n");
		message.append("--SOLUTIONS\n");
		message.append("Qui si possono cercare le solution relative ad una teoria ed un goal, in particolare la teoria e il goal\n"
				+ "che si andranno ad utilizzare saranno quelle selezionate nelle comboBox delle sezioni precedenti");
		info.setText(message.toString());
		/**/
		info.setEditable(false);
		JScrollPane scroll=new JScrollPane(info);
		c.add(scroll);
		
		this.setVisible(true);
	}

	

}
