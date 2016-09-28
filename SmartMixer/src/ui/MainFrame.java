package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.Ingredient;
import model.Recipe;
import model.Result;
import model.agent.MixerAgent;
import model.agent.RecipesChangeListener;
import model.agent.StateChangeListener;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private MixerAgent mixerAgent;
	
	private RecipesTable tableRecipes;
	private JTextPane textRecipe;
	private JButton buttonCookRecipe;
	private JButton buttonDeleteRecipe;
	private JButton buttonNewRecipe;
	
	public MainFrame(MixerAgent mixerAgent) {
		this.mixerAgent = mixerAgent;
		
		initComponents();
		initGUI();
		
		getMixerAgent().addStateListener(new StateChangeListener() {
			public void onStateChanged() {
				onStateChangedEvent();
			}
		});
		
		onStateChangedEvent();
		
		getMixerAgent().addRecipesListener(new RecipesChangeListener() {
			public void onRecipesChanged() {
				onRecipesChangedEvent();
			}
		});
		
		onRecipesChangedEvent();
		
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent event) { }
			
			public void windowIconified(WindowEvent event) { }
			
			public void windowDeiconified(WindowEvent event) { }
			
			public void windowDeactivated(WindowEvent event) { }
			
			public void windowClosing(WindowEvent event) { }
			
			public void windowClosed(WindowEvent event) {
				try {
					getMixerAgent().removePresenceInformation();
				} catch (Exception e) { }
			}
			
			public void windowActivated(WindowEvent event) { }
		});
	}

	public MixerAgent getMixerAgent() {
		return (mixerAgent);
	}

	public RecipesTable getTableRecipes() {
		return (tableRecipes);
	}

	public JTextPane getTextRecipe() {
		return (textRecipe);
	}

	public JButton getButtonCookRecipe() {
		return (buttonCookRecipe);
	}

	public JButton getButtonDeleteRecipe() {
		return (buttonDeleteRecipe);
	}

	public JButton getButtonNewRecipe() {
		return (buttonNewRecipe);
	}
	
	private void initComponents() {
		tableRecipes = new RecipesTable();
		textRecipe = new JTextPane();
		buttonCookRecipe = new JButton("Cook");
		buttonDeleteRecipe = new JButton("Delete");
		buttonNewRecipe = new JButton("New");
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel recipesPanel = new JPanel();
		recipesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));

		// tableRecipes
		getTableRecipes().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				onListSelectionChanged(event);
			}
		});
		// ---
		
		// textRecipe
		getTextRecipe().setEditable(false);
		getTextRecipe().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		StyledDocument document = getTextRecipe().getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		document.setParagraphAttributes(0, document.getLength(), center, false);
		// ---
		
		// tableRecipesScrollPane
		JScrollPane tableRecipesScrollPane = new JScrollPane(getTableRecipes());
		tableRecipesScrollPane.setPreferredSize(new Dimension(400, 125));
		// ---
		
		// textRecipeScrollPane
		JScrollPane textRecipeScrollPane = new JScrollPane(getTextRecipe());
		textRecipeScrollPane.setPreferredSize(new Dimension(300, 135));
		textRecipeScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		// ---
		
		// buttonCookRecipe
		getButtonCookRecipe().setFocusPainted(false);
		getButtonCookRecipe().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onClickButtonCookRecipe(event);
			}
		});
		// ---
		
		// buttonDeleteRecipe
		getButtonDeleteRecipe().setFocusPainted(false);
		getButtonDeleteRecipe().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onClickButtonDeleteRecipe(event);
			}
		});
		// ---
		
		// buttonNewRecipe
		getButtonNewRecipe().setFocusPainted(false);
		getButtonNewRecipe().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onClickButtonNewRecipe(event);
			}
		});
		// ---
		
		// recipesInformationPanel
		JPanel recipesInformationPanel = new JPanel();
		recipesInformationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		recipesInformationPanel.setLayout(new BoxLayout(recipesInformationPanel, BoxLayout.Y_AXIS));
		// ---
		
		// recipesHandlerPanel
		JPanel recipesHandlerPanel = new JPanel();
		recipesHandlerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		recipesHandlerPanel.add(getButtonCookRecipe());
		recipesHandlerPanel.add(getButtonDeleteRecipe());
		recipesHandlerPanel.add(getButtonNewRecipe());
		// ---
		
		recipesInformationPanel.add(textRecipeScrollPane);
		recipesInformationPanel.add(recipesHandlerPanel);
		recipesPanel.add(tableRecipesScrollPane);
		recipesPanel.add(recipesInformationPanel);
		mainPanel.add(recipesPanel);
		
		getContentPane().add(mainPanel);
		
		pack();
		setTitle("MixerAgent" + " - " + getMixerAgent().getName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void onListSelectionChanged(ListSelectionEvent e) {
		Recipe recipe = getTableRecipes().getSelectedItem();
		if ( recipe == null )
			getTextRecipe().setText("");
		else // if ( recipe != null )
			getTextRecipe().setText(recipe.toString());
	}

	private void onClickButtonCookRecipe(ActionEvent event) {
		Recipe recipe = getTableRecipes().getSelectedItem();
		if ( recipe != null )
			doCook(recipe);
	}
	
	private void onClickButtonDeleteRecipe(ActionEvent event) {
		Recipe recipe = getTableRecipes().getSelectedItem();
		if (recipe != null) {
			try {
				getMixerAgent().deleteRecipe(recipe);
				getMixerAgent().readAllRecipes();
				JOptionPane.showMessageDialog(this, "Recipe has been deleted!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void onClickButtonNewRecipe(ActionEvent event) {
		NewRecipeFrame newRecipeFrame = new NewRecipeFrame();
		newRecipeFrame.setVisible(true);
		
		if ( newRecipeFrame.getAction() == 0 ) { // save recipe.
			try {
				getMixerAgent().saveRecipe(newRecipeFrame.getRecipe());			
				getMixerAgent().readAllRecipes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ( newRecipeFrame.getAction() == 1 ) // cook temp recipe.
			doCook(newRecipeFrame.getRecipe());
	}
	
	private void doCook(Recipe recipe) {
		try {
			Result result = getMixerAgent().prepareRecipe(recipe);
			if (result.getId() == 0)
				JOptionPane.showMessageDialog(this, "Request sent successfully!");
			if (result.getId() == -1)
				JOptionPane.showMessageDialog(this, "Mixer already on!");
			else if (result.getId() == -2)
				JOptionPane.showMessageDialog(this, "Mixer can't turn on");
			else if (result.getId() == -3) {
				String message = "";
				message = message + "Fridge doesn't contains the following ingredients: " + System.lineSeparator();
				for ( Ingredient ingredient : result.getIngredients() )
					message = message + " - " + ingredient.getName() + " " + "(" + ingredient.getQuantity() + ")" + System.lineSeparator();
				message = message + "Would you like to send a purchase order?";
				int option = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_OPTION);
	    		if (option==JOptionPane.YES_OPTION)
	    			getMixerAgent().insertOrder(result.getIngredients());
			}
			else if (result.getId() == -4)
				JOptionPane.showMessageDialog(this, "No ovens available!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onStateChangedEvent() {
		getButtonCookRecipe().setEnabled( !getMixerAgent().getState() );
	}
	
	private void onRecipesChangedEvent() {
		List<Recipe> recipes = getMixerAgent().getCurrentRecipes();
		getTableRecipes().setData(recipes);
	}
}
