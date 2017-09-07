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
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class AddPolicyFrame extends JDialog {
	
	private Map<String, List<String>> policies;
	
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel policyPanel;
	
	private int action;
	private String result;
	
	private String supermarketId;
	
	public int getAction() {
		return action;
	}

	public String getResult() {
		return result;
	}

	public AddPolicyFrame(Map<String, List<String>> policies, String supermarketId) {
		setTitle("Add policy");
		
		mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		topPanel = new JPanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		policyPanel = new JPanel();
		mainPanel.add(policyPanel, BorderLayout.CENTER);
		policyPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel = new JPanel();
		mainPanel.add(panel, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonCancel();
			}
		});
		panel.add(btnCancel);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonInsert();
			}
		});
		panel.add(btnInsert);
		
		this.policies = policies;
		
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
		
		this.supermarketId = supermarketId;
		
	}
	
	private void initPolicies(){
		ButtonGroup buttonGroup = new ButtonGroup();
		for(String policy : policies.keySet()){
			JRadioButton rb = new JRadioButton(policy);
			rb.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					onRadioButtonActionPerformed(e);
				}
			});
			rb.setActionCommand(policy);
			buttonGroup.add(rb);
			topPanel.add(rb);
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton btnCancel;
	private JButton btnInsert;

	private void onRadioButtonActionPerformed(ActionEvent e){
		policyPanel.removeAll();
		String policy = e.getActionCommand();
		List<String> elements = policies.get(policy);
		for(String element : elements){
			policyPanel.add(new JLabel(element));
			JTextField text = new JTextField();
			text.setName("txt" + element.replace(" ", ""));
			policyPanel.add(text);
		}
		pack();
		policyPanel.repaint();
		result = policy;
	}
	
	
	private void onClickButtonInsert(){
		//crea la stringa di policy
		
		/*
		 * fidelty_card(supermarket_id,days_of_week)
		 * offer(nro_day_of_week,ingredient_name,supermarket_id)
		 * big_quantity(ingredient_name,big_quantity,supermarket_id)
		 */
		
		List<String> elements = new ArrayList<>();
		for(Component comp : Arrays.asList(policyPanel.getComponents()).stream().filter(component -> component.getClass() == JTextField.class).collect(Collectors.toList())){
			JTextField text = (JTextField)comp;
			elements.add(text.getText());
		}
		
		if(elements.contains(""))
			JOptionPane.showMessageDialog(this, "One or more fields are empty.");
		else{
			int choice = JOptionPane.showConfirmDialog(null,
					"Do you really want to insert the policy?", "INSERT CHECK",
					JOptionPane.OK_CANCEL_OPTION);
			if (choice == JOptionPane.OK_OPTION) {
				action = 1; //salva la policy
				
				if(result.equals("fidelty_card"))
					result = result + "(" + supermarketId + ",\"" + elements.get(0) + "\")";
				else if(result.equals("offer"))
					result = result + "(\"" + elements.get(0) + "\"," + elements.get(1) + "," + supermarketId + ")";
				else if(result.equals("big_quantity"))
					result = result + "(" + elements.get(0) + "," + elements.get(1) + "," + supermarketId + ")";
				else if(result.equals("min_order"))
					result = result + "(" + supermarketId + "," + elements.get(0) + ")"; 
				dispose();
			}
		}
	}
	
	private void onClickButtonCancel(){
		action = 0; //niente
		dispose();
	}
	
}
