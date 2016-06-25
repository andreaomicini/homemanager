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
	
   	private PrologOvenAgent tcsonAgent = null;
    private TracePanel tp;
    private ViewManageAgentPanel maPanel;
    private List<Recipe> recipes;
    private List<Ingredient> ingredients;
	
	public ViewOvenPanel(ViewManageAgentPanel maPanel, PrologOvenAgent prologOvenAgent, TracePanel tp) {
        
        this.tcsonAgent = prologOvenAgent;
        this.maPanel = maPanel;
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
	        jSeparator2 = new javax.swing.JSeparator();
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
	        jLabel1.setText("Oven Agent");

	        jLabel2.setText("Recipe Name");

	        jLabel3.setText("Cooking Time");

	        jLabel4.setText("Temperature");

	        jTextField1.setName("nametext"); // NOI18N
	       
	        jTextField2.setName("cookingtimetext"); // NOI18N

	        jTextField3.setName("temperaturetext"); // NOI18N

	        jLabel5.setText("Ingredient");

	        jTextField4.setName("ingredienttext"); // NOI18N

	        jLabel6.setText("Quantity");

	        jTextField5.setName("quantitytext"); // NOI18N

	        jButton1.setText("Add Ingredient");
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

	        jButton2.setText("Save Recipe");
	        jButton2.setName("memorizebutton"); // NOI18N
	        jButton2.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton2ActionPerformed(evt);
	            }
	        });

	        jButton3.setText("Cook Without Saving");
	        jButton3.setName("cookwithoutsavingbutton"); // NOI18N
	        jButton3.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton3ActionPerformed(evt);
	            }
	        });

	        jLabel7.setText("Saved Recipes");

	        
	        this.listModel2=new DefaultListModel<Recipe>();
	        this.populateRecipeList();
	        jList2 = new javax.swing.JList<Recipe>(this.listModel2);
	        jList2.setName("recipelist"); // NOI18N
	        jScrollPane2.setViewportView(jList2);

	        jButton4.setText("Cook Recipe");
	        jButton4.setName("cook2button"); // NOI18N
	        jButton4.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton4ActionPerformed(evt);
	            }
	        });

	        jButton5.setText("Delete Ingredient");
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

	        jButton8.setText("Delete Recipe");
	        jButton8.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton8ActionPerformed(evt);
	            }
	        });

	        jLabel8.setText("Status:");

	        jLabel9.setText("Temperature:");

	        jLabel10.setText("Off");

	        jLabel11.setText("0");

	        jLabel12.setText("0");

	        jLabel13.setText("degrees");

	        jButton9.setText("Turn On");
	        jButton9.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton9ActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(jSeparator1)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addGap(18, 18, 18)
	                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jButton1)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addGap(18, 18, 18)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(jButton8)
	                                    .addComponent(jButton4)))
	                            .addGroup(layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                    .addComponent(jLabel5))
	                                .addGap(18, 18, 18)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                        .addGap(18, 18, 18)
	                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addComponent(jLabel6)
	                                        .addGap(119, 119, 119)
	                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))))
	                        .addGap(18, 18, 18)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jButton2)
	                            .addComponent(jButton3))
	                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jLabel2)
	                                .addGap(56, 56, 56)
	                                .addComponent(jLabel3)
	                                .addGap(62, 62, 62)
	                                .addComponent(jLabel4))
	                            .addComponent(jLabel7))
	                        .addGap(0, 0, Short.MAX_VALUE))))
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(101, 101, 101)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                                    .addComponent(jLabel8)
	                                    .addComponent(jLabel9))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(jLabel10)
	                                    .addComponent(jLabel11))
	                                .addGap(170, 170, 170)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                                    .addComponent(jButton9)
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                        .addComponent(jLabel13))))
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jButton6)
	                                .addGap(5, 5, 5)
	                                .addComponent(jButton7)
	                                .addGap(18, 18, 18)
	                                .addComponent(jLabel1)
	                                .addGap(98, 98, 98)
	                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                        .addGap(0, 0, Short.MAX_VALUE))
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addGap(0, 10, Short.MAX_VALUE)
	                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel1)
	                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                .addComponent(jLabel8)
	                                .addComponent(jLabel10))
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addComponent(jLabel13))))
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(jButton6)
	                            .addComponent(jButton7))))
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(12, 12, 12)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel11)
	                            .addComponent(jLabel9)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(jButton9)))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addComponent(jButton2)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton3)
	                        .addGap(33, 33, 33))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(7, 7, 7)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jLabel3)
	                                    .addComponent(jLabel4)))
	                            .addGroup(layout.createSequentialGroup()
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(jLabel2)))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jLabel5)
	                                    .addComponent(jLabel6))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                .addGap(18, 18, 18)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                                    .addComponent(jButton1)
	                                    .addComponent(jButton5))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
	                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(38, 38, 38)
	                        .addComponent(jButton4)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton8))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(1, 1, 1)
	                        .addComponent(jLabel7)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap())
	        );
	    }// </editor-fold> 
	 
	 
	
	 //jSlider1 Handler
	 protected void jSlider1StateChanged(ChangeEvent evt) {
			this.jLabel12.setText(""+this.jSlider1.getValue());			
		}
	 
	
	    //Accendi-Spegni
	 private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 	
	    	if(this.jButton9.getText().equals("Turn On")){
	    		if(this.jSlider1.getValue()!=0){
	    		this.tcsonAgent.turnOn(this.jSlider1.getValue());
	    		this.jButton9.setText("Turn Off");
	    	   
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel10.setText("On");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel10.setText("Off");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	}
	    	//if(this.jButton1.getText().equals("Spegni"))
	    	else{
	    		this.tcsonAgent.turnOff();
	    	    this.jButton9.setText("Turn On");
	    	    
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel10.setText("On");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel10.setText("Off");
		         	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	
	    	 
	    	
	    }                                        
	   
	//memorizza ricetta
	    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String name=this.jTextField1.getText();
	    	int cookingtime,temperature;
	    	try{
	    	cookingtime=Integer.parseInt(this.jTextField2.getText());
	    	temperature=Integer.parseInt(this.jTextField3.getText());
	    	}catch(Exception e){
	    		JOptionPane.showMessageDialog(null,
	                    "Value not Integer;\nPlease insert Integer value in cooking time and/or temperature area.",
	                    "INCORRECT VALUE ", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
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
	    	int quantity;
	    	try{
	    		
	        quantity=Integer.parseInt(this.jTextField5.getText());
	    }catch(Exception e){
	    	JOptionPane.showMessageDialog(null,
                    "Value in quantity area not Integer;\nPlease insert Integer value in quantity area.",
                    "INCORRECT VALUE ", JOptionPane.ERROR_MESSAGE);
            return;
	    }
	    	this.ingredients.add(new Ingredient(ingredient, quantity));
	    	this.populateIngredientList();
	    	this.jTextField4.setText("");
	    	this.jTextField5.setText("");
	    }                                        

	    
	    //Cuoci ricette non memorizzate
	    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String name=this.jTextField1.getText();
	    	int cookingtime,temperature;
	    	try{
	    	cookingtime=Integer.parseInt(this.jTextField2.getText());
	    	temperature=Integer.parseInt(this.jTextField3.getText());
	    	}catch(Exception e){
	    		JOptionPane.showMessageDialog(null,
	                    "Value not Integer;\nPlease insert Integer value in cooking time and/or temperature area.",
	                    "INCORRECT VALUE ", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
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
	    	
	    	int result=this.tcsonAgent.cookRecipe(tmp,"fridgeAgent",1);  
	    	this.handleCookRecipeResult(result,tmp);
	    	
	    	this.ingredients.clear();    	
	    	this.populateIngredientList();
	    }                                        

	    
	    //Cuoci ricette memorizzate
	    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	Recipe tmp=this.jList2.getSelectedValue();
	    	
	    	this.tp.appendText(tmp.getIngredient(0).toString());
	    	int result=this.tcsonAgent.cookRecipe(tmp,"fridgeAgent",0);  
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
	         this.maPanel.menu();
	    }                                        

	    //Menu
	    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 this.hidePanel();
	         this.maPanel.showPanel();
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
	    		JOptionPane.showMessageDialog(null,"Request sent successfully","", JOptionPane.INFORMATION_MESSAGE);
	    		break;
	    	
	    	case 0:
	    		mandaordine=JOptionPane.showConfirmDialog(null,"Fridge doesn't contains required ingredients, would you like to send a purchase order?","", JOptionPane.YES_NO_OPTION);
	    		if(mandaordine==JOptionPane.YES_OPTION){
	    			this.tcsonAgent.orderBuyConfirm(recipe, true);}
	    		else
	    			this.tcsonAgent.orderBuyConfirm(recipe, false);
	    		break;
	    	
	    	case -1:JOptionPane.showMessageDialog(null,"Communication error with ovenfridge_tc","", JOptionPane.ERROR_MESSAGE);
	    		break;
	    	
	    	case -2:JOptionPane.showMessageDialog(null,"Confirm not recived after timeout","", JOptionPane.ERROR_MESSAGE);
	    		break;
	    	
	    	case -3:JOptionPane.showMessageDialog(null,"Errors in deleting used ingredients from fridge content","", JOptionPane.ERROR_MESSAGE);
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
	    private javax.swing.JSeparator jSeparator2;
	    private javax.swing.JSlider jSlider1;
	    private javax.swing.JTextField jTextField1;
	    private javax.swing.JTextField jTextField2;
	    private javax.swing.JTextField jTextField3;
	    private javax.swing.JTextField jTextField4;
	    private javax.swing.JTextField jTextField5;
	    // End of variables declaration          
    

	public void init() {
        
        this.recipes=this.tcsonAgent.readAllRecipes();
        this.populateRecipeList();
        if(this.tcsonAgent.getState()==true){
        	this.jLabel10.setText("On");
        	this.jLabel11.setText(""+this.tcsonAgent.getTemperature());
        }
        else{
        	this.jLabel10.setText("Off");
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
