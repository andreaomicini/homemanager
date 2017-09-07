package it.unibo.homemanager.userinterfaces.agents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PoliciesPriorityFrame extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> policies;
	List<String> result;
	int action;
	private JTextField txtIngredientName;
	private JPanel policiesPanel;
	
	
	public List<String> getResult() {
		return result;
	}

	public int getAction() {
		return action;
	}

	public PoliciesPriorityFrame(List<String> policies) {
		this.policies = policies;
		setTitle("Policies Priority");
		
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblIngredientName = new JLabel("Ingredient name");
		panel.add(lblIngredientName);
		
		txtIngredientName = new JTextField();
		panel.add(txtIngredientName);
		txtIngredientName.setColumns(10);
		
		policiesPanel = new JPanel();
		getContentPane().add(policiesPanel, BorderLayout.CENTER);
		policiesPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonCancel();
			}
		});
		panel_1.add(btnCancel);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonOk();
			}
		});
		panel_1.add(btnOk);
	
		
		
		initPolicies();
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		pack();
		
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		int screenWidth = (int) defaultToolkit.getScreenSize().getWidth();
		int screenHeight = (int) defaultToolkit.getScreenSize().getHeight();
		
		int windowWidth = (int) getSize().getWidth();
		int windowHeight = (int) getSize().getHeight();
		
		int x = (screenWidth - windowWidth) / 2;
		int y = (screenHeight - windowHeight) / 2;
		setLocation(x, y);
	}

	private void initPolicies() {
		for(String policy : policies){
			JLabel label = new JLabel(policy);
			JTextField text = new JTextField(3);
			text.setName(policy);
			policiesPanel.add(label);
			policiesPanel.add(text);
		}		
	}
	
	private void onClickButtonCancel(){
		action = 0; //niente
		dispose();
	}
	
	private void onClickButtonOk(){
		if(txtIngredientName.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(this, "Please insert an ingredient name.");
			return;
		}

		result = new ArrayList<>();
		
		List<Component> textboxes = Arrays.asList(policiesPanel.getComponents()).stream().filter(component -> component.getClass() == JTextField.class).collect(Collectors.toList());
		
		for(Component comp : textboxes)
			if(((JTextField)comp).getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(this, "Please fill all the fields.");
				return;
			}
		
		int choice = JOptionPane.showConfirmDialog(null, "Do you really want to insert policies priority?",
				"INSERT CHECK", JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION){
			
			for(int index = 1; index <= textboxes.size(); index++){
				String stringIndex = "" + index;
				Optional<Component> txt = textboxes.stream().filter(comp -> ((JTextField)comp).getText().trim().equals(stringIndex)).findFirst();
				if(!txt.isPresent()){
					JOptionPane.showMessageDialog(this, "Please fill all the fields with the numbers from 1 to " + textboxes.size() + ".");
					return;
				}
				else{
					JTextField textbox = (JTextField)txt.get();
					result.add("policies_priority(" + txtIngredientName.getText() + "," + textbox.getText().trim() + "," + textbox.getName() + ")");
				}
			}
			action = 1;
			dispose();
		}

	}
}
