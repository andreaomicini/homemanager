package it.unibo.homemanager.userinterfaces.agents;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import it.unibo.homemanager.agents.ShopperAgent;
import it.unibo.homemanager.agents.SupermarketsChangeListener;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

public class ShopperPanel extends javax.swing.JPanel{

	private static final long serialVersionUID = 1L;

	private ShopperAgent shopperAgent;
	private ViewManageAgentPanel butlerPanel;
	
	private DefaultListModel<String> listModel;
	private JList<String> supermarketList;
	private JTextField txtNewMail;
	private JTextField txtNewName;
	private JTextField txtNewIngredient;
	JSpinner spinnerThreshold;
	
	public ShopperPanel(ViewManageAgentPanel butlerPanel, ShopperAgent shopperAgent, TracePanel tracePanel){
		setBackground(Color.WHITE);
		this.shopperAgent = shopperAgent;
		this.butlerPanel = butlerPanel;
		
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonMenu(e);
			}
		});
		button.setText("Menu");
		button.setForeground(new Color(102, 0, 102));
		button.setFont(new Font("Courier New", Font.BOLD, 14));
		
		JButton button_1 = new JButton();
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonBack(e);
			}
		});
		button_1.setText("Back");
		button_1.setForeground(new Color(102, 0, 102));
		button_1.setFont(new Font("Courier New", Font.BOLD, 14));
		
		JScrollPane supermarketScrollPane = new JScrollPane();
		
		JLabel lblSupermarketList = new JLabel();
		lblSupermarketList.setText("Supermarket List");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Insert supermarket", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBorder(new TitledBorder(null, "New threshold", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnAddNewPolicy = new JButton();
		btnAddNewPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonAddNewPolicy();
			}
		});
		btnAddNewPolicy.setText("Add new policy");
		btnAddNewPolicy.setForeground(new Color(102, 0, 102));
		btnAddNewPolicy.setFont(new Font("Courier New", Font.BOLD, 14));
		
		JButton btnViewAllPolicies = new JButton();
		btnViewAllPolicies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonViewAllPolicies();
			}
		});
		btnViewAllPolicies.setText("View all policies");
		btnViewAllPolicies.setForeground(new Color(102, 0, 102));
		btnViewAllPolicies.setFont(new Font("Courier New", Font.BOLD, 14));
		
		JButton btnPoliciesPriority = new JButton();
		btnPoliciesPriority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonPoliciesPriority();
			}
		});
		btnPoliciesPriority.setText("Policies priority");
		btnPoliciesPriority.setForeground(new Color(102, 0, 102));
		btnPoliciesPriority.setFont(new Font("Courier New", Font.BOLD, 14));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(supermarketScrollPane, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(btnAddNewPolicy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblSupermarketList, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnViewAllPolicies, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)))
							.addGap(36))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnPoliciesPriority, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(42)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(panel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSupermarketList)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(supermarketScrollPane, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, Short.MAX_VALUE)
							.addComponent(btnPoliciesPriority, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddNewPolicy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewAllPolicies, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
							.addGap(1)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		
		JButton btnAddNewThreshold = new JButton();
		btnAddNewThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButonAddThreshold();
			}
		});
		btnAddNewThreshold.setText("Add");
		btnAddNewThreshold.setForeground(new Color(102, 0, 102));
		btnAddNewThreshold.setFont(new Font("Courier New", Font.BOLD, 14));
		
		JLabel lblThreshold = new JLabel();
		lblThreshold.setText("Threshold");
		
		JLabel lblIngredient = new JLabel();
		lblIngredient.setText("Ingredient");
		
		txtNewIngredient = new JTextField();
		txtNewIngredient.setName("");
		txtNewIngredient.setFont(new Font("Courier New", Font.BOLD, 14));
		
		spinnerThreshold = new JSpinner();
		
		JButton btnViewAllThresholds = new JButton();
		btnViewAllThresholds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonViewAllThresholds();
			}
		});
		btnViewAllThresholds.setText("View all");
		btnViewAllThresholds.setForeground(new Color(102, 0, 102));
		btnViewAllThresholds.setFont(new Font("Courier New", Font.BOLD, 14));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(btnAddNewThreshold, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnViewAllThresholds, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(lblIngredient, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblThreshold, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
								.addComponent(spinnerThreshold)
								.addComponent(txtNewIngredient, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(lblIngredient)
						.addComponent(txtNewIngredient, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(spinnerThreshold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblThreshold))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddNewThreshold, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnViewAllThresholds, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		JLabel label = new JLabel();
		label.setText("Name");
		
		JLabel lblContact = new JLabel();
		lblContact.setText("Contact");
		
		JButton button_2 = new JButton();
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickButtonAddNewSupermarket();
			}
		});
		button_2.setText("Add new supermarket");
		button_2.setForeground(new Color(102, 0, 102));
		button_2.setFont(new Font("Courier New", Font.BOLD, 14));
		
		txtNewMail = new JTextField();
		txtNewMail.setName("");
		txtNewMail.setFont(new Font("Courier New", Font.BOLD, 14));
		
		txtNewName = new JTextField();
		txtNewName.setName("");
		txtNewName.setFont(new Font("Courier New", Font.BOLD, 14));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(22, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
								.addGap(32)
								.addComponent(txtNewName, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(lblContact, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(txtNewMail, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addComponent(txtNewName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtNewMail, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblContact))
					.addPreferredGap(ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
					.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
		
		setBackground(new java.awt.Color(255, 255, 255));
		setMaximumSize(new java.awt.Dimension(538, 343));
		setMinimumSize(new java.awt.Dimension(538, 343));
		setPreferredSize(new java.awt.Dimension(538, 343));
		
		listModel = new DefaultListModel<>();
		supermarketList = new JList<>(listModel);
		supermarketScrollPane.getViewport().setView(supermarketList);
		
		
		getShopperAgent().addSupermarketsListener(new SupermarketsChangeListener() {
			
			@Override
			public void onSupermarketsChanged() {
				onSupermarketChangedEvent();
			}
		});
		onSupermarketChangedEvent();
		
	}	
	
	private void onClickButtonBack(ActionEvent event) {
		hidePanel();
		getButlerPanel().menu();
	}

	private void onClickButtonMenu(java.awt.event.ActionEvent evt) {
		hidePanel();
		getButlerPanel().showPanel();
	}
	
	public ShopperAgent getShopperAgent() {
		return shopperAgent;
	}

	public ViewManageAgentPanel getButlerPanel() {
		return butlerPanel;
	}

	public void init() {
		onSupermarketChangedEvent();
		setVisible(true);
	}

	public void hidePanel() {
		setVisible(false);
	}
	
	private void onSupermarketChangedEvent() {
		listModel.clear();
		List<String> supermarkets = getShopperAgent().getAllSupermarkets();
		for ( String supermarket : supermarkets )
			listModel.addElement(supermarket);
	}
	
	private void onClickButtonAddNewSupermarket(){
		String name = txtNewName.getText().trim();
		String contact = txtNewMail.getText().trim();
		
		if(name.isEmpty() || contact.isEmpty()){
			JOptionPane.showMessageDialog(null,
					"One or more fields are empty.\nPlease fill all the fields.", "ACTION INVALID",
					JOptionPane.ERROR_MESSAGE);
			txtNewName.grabFocus();
			return;
		}
		int choice = JOptionPane.showConfirmDialog(null,
				"Do you really want to insert the supermarket " + name + "?", "INSERT CHECK",
				JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION) {
			try {
				getShopperAgent().addSupermarket(name, contact);
			} catch (Exception e) {}
		}

		txtNewName.setText("");
		txtNewMail.setText("");
	}
	
	private void onClickButtonAddNewPolicy(){
		
		String supermarket = supermarketList.getSelectedValue();
		
		if(supermarket == null){
			JOptionPane.showMessageDialog(null,
					"No supermarket selected. Please select a supermarket\n from those listed on the left.", "ACTION INVALID",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		AddPolicyFrame addPolicyFrame = new AddPolicyFrame(getShopperAgent().getPoliciesTemplate(), supermarket.split(" ")[0]);
		addPolicyFrame.setVisible(true);
		
		if(addPolicyFrame.getAction() == 1){
			try {
				getShopperAgent().addPolicy(addPolicyFrame.getResult());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void onClickButonAddThreshold(){
		String ingredient = txtNewIngredient.getText();
		String user = "1"; //dovrei recuperare l'utente loggato
		Integer threshold = (Integer)spinnerThreshold.getValue();
		
		if(ingredient.isEmpty()){
			JOptionPane.showMessageDialog(null,
					"Please insert the name of the ingredient.",
					"INCORRECT VALUE ", JOptionPane.ERROR_MESSAGE);
			txtNewIngredient.grabFocus();
			return;
		}
		if(threshold == null || threshold == 0){
			JOptionPane.showMessageDialog(null,
					"Please insert the value of the threshold.",
					"INCORRECT VALUE ", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int choice = JOptionPane.showConfirmDialog(null, "Do you really want to insert the threshold for " + ingredient + "?",
				"INSERT CHECK", JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION){
			try {
				getShopperAgent().addThreshold(user, ingredient, threshold);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		txtNewIngredient.setText("");
		spinnerThreshold.setValue(new Integer(0));
	}
	
	private void onClickButtonViewAllThresholds(){
		try{
			new ViewAllThresholdsPolicy(getShopperAgent().getAllThresholds()).setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onClickButtonViewAllPolicies(){
		try{
			new ViewAllThresholdsPolicy(getShopperAgent().getAllPolicies()).setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onClickButtonPoliciesPriority(){
		PoliciesPriorityFrame policiesPriorityFrame = new PoliciesPriorityFrame(getShopperAgent().getPriorityTemplate());
		policiesPriorityFrame.setVisible(true);
		
		if(policiesPriorityFrame.getAction() == 1){
			try {
				for(String policy : policiesPriorityFrame.getResult())
					getShopperAgent().addPolicy(policy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
