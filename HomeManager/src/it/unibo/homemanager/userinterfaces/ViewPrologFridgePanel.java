package it.unibo.homemanager.userinterfaces;

import java.util.ArrayList;
import java.util.List;

import it.unibo.homemanager.fridge.Ingredient;
import it.unibo.homemanager.fridge.PrologFridgeAgent;
import it.unibo.homemanager.fridge.Recipe;
import it.unibo.homemanager.fridge.TucsonFridgeAgent;
import it.unibo.homemanager.tablemap.User;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.TucsonTupleCentreId;

public class ViewPrologFridgePanel extends javax.swing.JPanel{
	private User user;
    private PrologFridgeAgent tcsonAgent = null;
    private TracePanel tp;
    private ViewManageDevicesPanel mdPanel;
    private TucsonTupleCentreId fridge_tc;
    private TucsonTupleCentreId ovenfridge_tc;
    private List<Recipe> recipes;
    private List<Ingredient> ingredients;

    public ViewPrologFridgePanel(ViewManageDevicesPanel mdPanel, PrologFridgeAgent prologFridgeAgent, TucsonTupleCentreId fridge_tc, TucsonTupleCentreId ovenfridge_tc, TracePanel tp) {
        this.fridge_tc = fridge_tc;
        this.ovenfridge_tc=ovenfridge_tc;
        this.tcsonAgent = prologFridgeAgent;
        this.mdPanel = mdPanel;
        this.tp = tp;
        this.recipes=this.tcsonAgent.readAllRecipes();
        this.ingredients=new ArrayList<Ingredient>();
        initComponents();
        
    }

    public void init(User usr) {
        user = usr;
        this.recipes=this.tcsonAgent.readAllRecipes();
        this.populateRecipeList();
        if (!isVisible()) {
            setVisible(true);
        }
    }
    
    public void hidePanel() {
        if (isVisible()) {
            setVisible(false);
        }
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
        this.listModel1=new DefaultListModel<Ingredient>();
        jList1 = new javax.swing.JList<Ingredient>(this.listModel1);
        this.populateIngredientList();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        this.listModel2=new DefaultListModel<Recipe>();
        this.populateRecipeList();
        jList2 = new javax.swing.JList<Recipe>(this.listModel2);
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        
        
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Agente Frigo");

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

       /* jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " ", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });*/
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
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jTextField4)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel2)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(5, 5, 5)
                        .addComponent(jButton7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton8)
                            .addComponent(jButton4))))
                .addContainerGap(145, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(34, 34, 34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6))
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton1)
                                            .addComponent(jButton5))
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel7))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8)
                        .addGap(36, 36, 36))))
        );
    }// </editor-fold>
    
    
    
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
    	
    	int result=this.tcsonAgent.cookRecipe(tmp);  
    	this.handleCookRecipeResult(result,tmp);
    	
    	this.ingredients.clear();    	
    	this.populateIngredientList();
    }                                        

    
    //Cuoci ricette memorizzate
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	Recipe tmp=this.jList2.getSelectedValue();
    	
    	this.tp.appendText(tmp.getIngredient(0).toString());
    	int result=this.tcsonAgent.cookRecipe(tmp);  
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
         mdPanel.showPanel();
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
   	 -1 errore comunicazione con fridge_tc
   	 -2 conferma non ricevuta dal Forno a scadenza timeout
   	 -3 errore nell'eliminazione degli ingredienti utilizzati dal contenuto del frigo
   	   */
    	int mandaordine;
    	switch(result){
    	case 1:
    		JOptionPane.showMessageDialog(null,"Richiesta inviata con successo","", JOptionPane.INFORMATION_MESSAGE);
    		break;
    	
    	case 0:
    		mandaordine=JOptionPane.showConfirmDialog(null,"Nel frigo non sono presenti gli ingredienti necessari, si vuole inviare un ordine?","", JOptionPane.YES_NO_OPTION);
    		if(mandaordine==JOptionPane.YES_OPTION){
    			this.tcsonAgent.insertOrder(recipe.getMissingIngredients(this.tcsonAgent.getContent()));
    	}
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<Ingredient> jList1;
    private javax.swing.JList<Recipe> jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
