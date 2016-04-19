package it.unibo.homemanager.userinterfaces;

import java.util.ArrayList;
import java.util.List;

import it.unibo.homemanager.fridge.Ingredient;
import it.unibo.homemanager.fridge.PrologFridgeAgent;
import it.unibo.homemanager.fridge.Recipe;
import it.unibo.homemanager.oven.PrologOvenAgent;
import it.unibo.homemanager.tablemap.User;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

public class ViewOvenPanel extends javax.swing.JPanel {

	private User user;
    private PrologOvenAgent tcsonAgent = null;
    private TracePanel tp;
    private ViewManageDevicesPanel mdPanel;
    private TucsonTupleCentreId ovenfridge_tc;	
    private List<Recipe> recipes;
    private List<Ingredient> ingredients;
	
	public ViewOvenPanel(ViewManageDevicesPanel mdPanel, PrologOvenAgent prologOvenAgent, TucsonTupleCentreId ovenfridge_tc, TracePanel tp) {
        
        this.ovenfridge_tc=ovenfridge_tc;
        this.tcsonAgent = prologOvenAgent;
        this.mdPanel = mdPanel;
        this.tp = tp;
        this.recipes=this.tcsonAgent.readAllRecipes();
        this.ingredients=new ArrayList<Ingredient>();
        initComponents();
    }
	 
	 private void initComponents() {

	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jTextField1 = new javax.swing.JTextField();
	        jTextField2 = new javax.swing.JTextField();
	        jTextField3 = new javax.swing.JTextField();
	        jLabel5 = new javax.swing.JLabel();
	        jTextField4 = new javax.swing.JTextField();
	        jLabel6 = new javax.swing.JLabel();
	        jTextField5 = new javax.swing.JTextField();
	        jButton1 = new javax.swing.JButton();
	        jScrollPane1 = new javax.swing.JScrollPane();
	        jButton2 = new javax.swing.JButton();
	        jButton3 = new javax.swing.JButton();
	        jLabel7 = new javax.swing.JLabel();
	        jScrollPane2 = new javax.swing.JScrollPane();
	        jButton4 = new javax.swing.JButton();
	        jButton5 = new javax.swing.JButton();
	        jButton6 = new javax.swing.JButton();
	        jButton7 = new javax.swing.JButton();
	        jButton8 = new javax.swing.JButton();
	        jLabel8 = new javax.swing.JLabel();
	        jLabel9 = new javax.swing.JLabel();
	        jLabel10 = new javax.swing.JLabel();
	        jLabel11 = new javax.swing.JLabel();
	        jSeparator1 = new javax.swing.JSeparator();
	        jSlider1 = new javax.swing.JSlider();
	        jLabel12 = new javax.swing.JLabel();
	        jLabel13 = new javax.swing.JLabel();
	        jButton9 = new javax.swing.JButton();

	        jSlider1.setMinimum(0);
	        jSlider1.setMaximum(250);
	        jSlider1.setValue(0);
	        jSlider1.addChangeListener(new ChangeListener(){				
				public void stateChanged(ChangeEvent arg0) {
					jSlider1StateChanged(arg0); 				
				}	        	
	        });
	        
	        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
	        jLabel1.setText("Agente Forno");

	        jLabel2.setText("Nome");

	        jLabel3.setText("Tempo Cottura");

	        jLabel4.setText("Temperatura");

	        jTextField1.setName("nametext"); // NOI18N
	       
	        jTextField2.setName("cookingtimetext"); // NOI18N

	        jTextField3.setName("temperaturetext"); // NOI18N

	        jLabel5.setText("Ingrediente");

	        jTextField4.setName("ingredienttext"); // NOI18N

	        jLabel6.setText("Quantità");

	        jTextField5.setName("quantitytext"); // NOI18N

	        jButton1.setText("Aggiungi Ingrediente");
	        jButton1.setName("addingredientbutton"); // NOI18N
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        this.listModel1=new DefaultListModel<Ingredient>();
	        this.populateIngredientList();
	        jList1 = new javax.swing.JList<Ingredient>(this.listModel1);
	        jList1.setName("ingredientlist"); // NOI18N
	        jScrollPane1.setViewportView(jList1);

	        jButton2.setText("Memorizza");
	        jButton2.setName("memorizebutton"); // NOI18N
	        jButton2.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton2ActionPerformed(evt);
	            }
	        });

	        jButton3.setText("Cuoci");
	        jButton3.setName("cookbutton"); // NOI18N
	        jButton3.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton3ActionPerformed(evt);
	            }
	        });

	        jLabel7.setText("Ricette Memorizzate");

	        
	        this.listModel2=new DefaultListModel<Recipe>();
	        this.populateRecipeList();
	        jList2 = new javax.swing.JList<Recipe>(this.listModel2);
	        jList2.setName("recipelist"); // NOI18N
	        jScrollPane2.setViewportView(jList2);

	        jButton4.setText("Cuoci");
	        jButton4.setName("cook2button"); // NOI18N
	        jButton4.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton4ActionPerformed(evt);
	            }
	        });

	        jButton5.setText("elimina");
	        jButton5.setName("eliminaingbutton"); // NOI18N
	        jButton5.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton5ActionPerformed(evt);
	            }
	        });

	        jButton6.setText("Back");
	        jButton6.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton6ActionPerformed(evt);
	            }
	        });

	        jButton7.setText("Menu");
	        jButton7.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton7ActionPerformed(evt);
	            }
	        });

	        jButton8.setText("Cancella");
	        jButton8.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton8ActionPerformed(evt);
	            }
	        });

	        jLabel8.setText("Stato:");

	        jLabel9.setText("Temperatura:");

	        jLabel10.setText("Spento");

	        jLabel11.setText("0");

	        jLabel12.setText("0");

	        jLabel13.setText("gradi");

	        jButton9.setText("Accendi");
	        jButton9.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton9ActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(18, 18, 18)
	                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(18, 18, 18)
	                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(0, 0, Short.MAX_VALUE))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addContainerGap()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(jLabel7)
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addComponent(jButton1)
	                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                        .addComponent(jButton5))
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(23, 23, 23)
	                                .addComponent(jLabel5)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(jLabel6)
	                                .addGap(38, 38, 38)))
	                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jButton2)
	                            .addComponent(jButton3))))
	                .addGap(32, 32, 32))
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(42, 42, 42)
	                        .addComponent(jLabel2)
	                        .addGap(77, 77, 77)
	                        .addComponent(jLabel3)
	                        .addGap(57, 57, 57)
	                        .addComponent(jLabel4))
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(47, 47, 47)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jButton8)
	                            .addComponent(jButton4)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(101, 101, 101)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addComponent(jLabel8)
	                            .addComponent(jLabel9))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel10)
	                            .addComponent(jLabel11))))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addComponent(jLabel12)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jLabel13)
	                        .addGap(99, 99, 99))
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addComponent(jButton9)
	                        .addGap(83, 83, 83))))
	            .addGroup(layout.createSequentialGroup()
	                .addComponent(jButton6)
	                .addGap(5, 5, 5)
	                .addComponent(jButton7)
	                .addGap(18, 18, 18)
	                .addComponent(jLabel1)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGap(23, 23, 23))
	            .addComponent(jSeparator1)
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addComponent(jLabel1)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                .addComponent(jLabel8)
	                                .addComponent(jLabel10))
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                .addComponent(jLabel12)
	                                .addComponent(jLabel13))))
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(jButton6)
	                            .addComponent(jButton7)))
	                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(12, 12, 12)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel11)
	                            .addComponent(jLabel9)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton9)))
	                .addGap(18, 18, 18)
	                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(jLabel3)
	                    .addComponent(jLabel4))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(jLabel5)
	                            .addComponent(jLabel6))
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jButton1)
	                                    .addComponent(jButton5))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(jLabel7))
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(8, 8, 8)
	                                .addComponent(jButton2)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(jButton3))))
	                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addContainerGap())
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addComponent(jButton4)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton8)
	                        .addGap(36, 36, 36))))
	        );
	    }// </editor-fold>  
	 
	 
	 
	 
	
	 //jSlider1 Handler
	 protected void jSlider1StateChanged(ChangeEvent evt) {
			this.jLabel12.setText(""+this.jSlider1.getValue());			
		}
	 
	
	    //Accendi-Spegni
	 private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 	
	    	if(this.jButton9.getText().equals("Accendi")){
	    		if(this.jSlider1.getValue()!=0){
	    		this.tcsonAgent.turnOn(this.jSlider1.getValue());
	    		this.jButton9.setText("Spegni");
	    	   
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel10.setText("Acceso");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel10.setText("Spento");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	}
	    	//if(this.jButton1.getText().equals("Spegni"))
	    	else{
	    		this.tcsonAgent.turnOff();
	    	    this.jButton9.setText("Accendi");
	    	    
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel10.setText("Acceso");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel10.setText("Spento");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	
	    	 
	    	
	    }                                        
	   
	//memorizza ricetta
	    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String name=this.jTextField1.getText();
	    	int cookingtime=Integer.parseInt(this.jTextField2.getText());
	    	int temperature=Integer.parseInt(this.jTextField3.getText());
	    	Recipe tmp=new Recipe();
	    	//tmp.setId(0);
	    	tmp.setName(name);
	    	tmp.setCookingtime(cookingtime);
	    	tmp.setTemperature(temperature);
	    	tmp.setIngredients(this.ingredients);
	    	this.tcsonAgent.memorizeRecipe(tmp);
	    	this.recipes=this.tcsonAgent.readAllRecipes();
	    	this.populateRecipeList();
	    	this.ingredients.clear();
	    	this.populateIngredientList();
	    	this.jTextField1.setText("");
	    	this.jTextField2.setText("");
	    	this.jTextField3.setText("");
	    }                                        

	    
	    //aggiungi ingrediente
	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String ingredient=this.jTextField4.getText();
	    	int quantity=Integer.parseInt(this.jTextField5.getText());
	    	this.ingredients.add(new Ingredient(ingredient, quantity));
	    	this.populateIngredientList();
	    	this.jTextField4.setText("");
	    	this.jTextField5.setText("");
	    }                                        

	    
	    //Cuoci ricette non memorizzate
	    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String name=this.jTextField1.getText();
	    	int cookingtime=Integer.parseInt(this.jTextField2.getText());
	    	int temperature=Integer.parseInt(this.jTextField3.getText());
	    	Recipe tmp=new Recipe();
	    	//gli id delle ricette memorizzate vanno da 0 9999
	    	tmp.setId(10000);
	    	tmp.setName(name);
	    	tmp.setCookingtime(cookingtime);
	    	tmp.setTemperature(temperature);
	    	tmp.setIngredients(this.ingredients);
	    	
	    	this.jTextField1.setText("");
	    	this.jTextField2.setText("");
	    	this.jTextField3.setText("");
	    	
	    	int result=this.tcsonAgent.cookRecipe(tmp,"FridgeArmando",1);  
	    	this.handleCookRecipeResult(result,tmp);
	    	
	    	this.ingredients.clear();    	
	    	this.populateIngredientList();
	    }                                        

	    
	    //Cuoci ricette memorizzate
	    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	Recipe tmp=this.jList2.getSelectedValue();
	    	
	    	this.tp.appendText(tmp.getIngredient(0).toString());
	    	int result=this.tcsonAgent.cookRecipe(tmp,"FridgeArmando",0);  
	    	this.handleCookRecipeResult(result,tmp);
	    }
	    
	    //Elimina ingrediente selezionato
	    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	Ingredient tmp=this.jList1.getSelectedValue();
	    	this.ingredients.remove(this.ingredients.indexOf(tmp));
	    	this.populateIngredientList();
	    }    
	    
	    //Back
	    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 this.hidePanel();
	         this.mdPanel.menu();
	    }                                        

	    //Menu
	    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 this.hidePanel();
	         this.mdPanel.showPanel(this.user);
	    }    
	    
	    //Cancella ricetta
	    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	Recipe tmp=this.jList2.getSelectedValue();
	    	this.tcsonAgent.deleteRecipe(tmp);
	    	this.populateRecipeList();
	    }                                        

	    
	    private void populateRecipeList(){
	    	this.recipes=this.tcsonAgent.readAllRecipes();  
	    	this.listModel2.clear();
	    	if(this.recipes!=null){
	    	for(Recipe tmp:this.recipes)
	    		this.listModel2.addElement(tmp);
	    	}
	    }
	    
	    private void populateIngredientList(){
	    	this.listModel1.clear();
	    	if(this.ingredients!=null){
	    	for(Ingredient tmp:this.ingredients){
	    		this.listModel1.addElement(tmp); 
	    			}  
	    	}
	    	
	    }
	    
	   
	    
	    private void handleCookRecipeResult(int result, Recipe recipe){
	    	/*
	   	 cookRecipe
	   	  1 se tutto è andato a buon fine
	   	  0 non ci sono gli ingredienti necessari
	   	 -1 errore nell'eliminazione degli ingredienti utilizzati dal contenuto del frigo
	   	 -2 errore comunicazione con ovenfridge_tc
	   	 -3 conferma non ricevuta dal Frigo a scadenza timeout
	   	   */
	    	int mandaordine;
	    	switch(result){
	    	case 1:
	    		JOptionPane.showMessageDialog(null,"Richiesta inviata con successo","", JOptionPane.INFORMATION_MESSAGE);
	    		break;
	    	
	    	case 0:
	    		mandaordine=JOptionPane.showConfirmDialog(null,"Nel frigo non sono presenti gli ingredienti necessari, si vuole inviare un ordine?","", JOptionPane.YES_NO_OPTION);
	    		if(mandaordine==JOptionPane.YES_OPTION){
	    			this.tcsonAgent.orderBuyConfirm(recipe, true);}
	    		else
	    			this.tcsonAgent.orderBuyConfirm(recipe, false);
	    		break;
	    	
	    	case -1:JOptionPane.showMessageDialog(null,"Errore comunicazione con fridge_tc","", JOptionPane.ERROR_MESSAGE);
	    		break;
	    	
	    	case -2:JOptionPane.showMessageDialog(null,"Conferma non ricevuta dal Forno a scadenza timeout","", JOptionPane.ERROR_MESSAGE);
	    		break;
	    	
	    	case -3:JOptionPane.showMessageDialog(null,"Errore nell'eliminazione degli ingredienti utilizzati dal contenuto del frigo","", JOptionPane.ERROR_MESSAGE);
	    		break;
	    	
	    	default:break;
	    	}
	    	
	    }

	    // Variables declaration - do not modify    
	    private DefaultListModel<Ingredient> listModel1;
	    private DefaultListModel<Recipe> listModel2;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JButton jButton2;
	    private javax.swing.JButton jButton3;
	    private javax.swing.JButton jButton4;
	    private javax.swing.JButton jButton5;
	    private javax.swing.JButton jButton6;
	    private javax.swing.JButton jButton7;
	    private javax.swing.JButton jButton8;
	    private javax.swing.JButton jButton9;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel10;
	    private javax.swing.JLabel jLabel11;
	    private javax.swing.JLabel jLabel12;
	    private javax.swing.JLabel jLabel13;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JLabel jLabel5;
	    private javax.swing.JLabel jLabel6;
	    private javax.swing.JLabel jLabel7;
	    private javax.swing.JLabel jLabel8;
	    private javax.swing.JLabel jLabel9;
	    private javax.swing.JList<Ingredient> jList1;
	    private javax.swing.JList<Recipe> jList2;
	    private javax.swing.JScrollPane jScrollPane1;
	    private javax.swing.JScrollPane jScrollPane2;
	    private javax.swing.JSeparator jSeparator1;
	    private javax.swing.JSlider jSlider1;
	    private javax.swing.JTextField jTextField1;
	    private javax.swing.JTextField jTextField2;
	    private javax.swing.JTextField jTextField3;
	    private javax.swing.JTextField jTextField4;
	    private javax.swing.JTextField jTextField5;
	    // End of variables declaration          
    

	public void init(User usr) {
        user = usr;
        this.recipes=this.tcsonAgent.readAllRecipes();
        this.populateRecipeList();
        if(this.tcsonAgent.getState()==true){
        	this.jLabel10.setText("Acceso");
        	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
        }
        else{
        	this.jLabel10.setText("Spento");
        	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
            }
        if (!isVisible()) {
            setVisible(true);
        }
    }
    
    public void hidePanel() {
        if (isVisible()) {
            setVisible(false);
        }
    }
}
