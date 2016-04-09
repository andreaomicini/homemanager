package it.unibo.homemanager.userinterfaces;

import it.unibo.homemanager.fridge.PrologFridgeAgent;
import it.unibo.homemanager.oven.PrologOvenAgent;
import it.unibo.homemanager.tablemap.User;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;

import alice.tucson.api.TucsonTupleCentreId;

public class ViewOvenPanel extends javax.swing.JPanel {

	private User user;
    private PrologOvenAgent tcsonAgent = null;
    private TracePanel tp;
    private ViewManageDevicesPanel mdPanel;
    private TucsonTupleCentreId ovenfridge_tc;	
	
	
	public ViewOvenPanel(ViewManageDevicesPanel mdPanel, PrologOvenAgent prologOvenAgent, TucsonTupleCentreId ovenfridge_tc, TracePanel tp) {
        
        this.ovenfridge_tc=ovenfridge_tc;
        this.tcsonAgent = prologOvenAgent;
        this.mdPanel = mdPanel;
        this.tp = tp;
        initComponents();
    }
	 private void initComponents() {

	        jLabel1 = new javax.swing.JLabel();
	        jSlider1 = new javax.swing.JSlider();
	        jLabel2 = new javax.swing.JLabel();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jButton1 = new javax.swing.JButton("Accendi");
	        jButton2 = new javax.swing.JButton();
	        jButton3 = new javax.swing.JButton();
	        jLabel5 = new javax.swing.JLabel();
	        jLabel6 = new javax.swing.JLabel("");
	        jLabel7 = new javax.swing.JLabel();
	        jLabel8 = new javax.swing.JLabel("");

	        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
	        jLabel1.setText("Agente Forno");

	        jSlider1.setMaximum(250);
	        jSlider1.setValue(0);
            jSlider1.addChangeListener(new javax.swing.event.ChangeListener(){				
				public void stateChanged(javax.swing.event.ChangeEvent evt) {
					jSlider1StateChanged(evt);						
				}									
				}            	
            );
            
           
	        jLabel2.setText("0");

	        jLabel3.setText("gradi");

	        jLabel4.setText("Temperatura");

	        
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        jButton2.setText("Back");
	        jButton2.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton2ActionPerformed(evt);
	            }
	        });

	        jButton3.setText("Menu");
	        jButton3.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton3ActionPerformed(evt);
	            }
	        });
	        
	        jLabel5.setText("Stato:");


	        jLabel7.setText("Temperatura:");
   

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(21, 21, 21)
	                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(layout.createSequentialGroup()
	                                .addGap(90, 90, 90)
	                                .addComponent(jLabel2)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                .addComponent(jLabel3)))
	                        .addGap(48, 48, 48)
	                        .addComponent(jButton1))
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton3)
	                        .addGap(35, 35, 35)
	                        .addComponent(jLabel1))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(128, 128, 128)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addComponent(jLabel5)
	                            .addComponent(jLabel7))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel6)
	                            .addComponent(jLabel8))))
	                .addContainerGap(59, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel1)
	                    .addComponent(jButton2)
	                    .addComponent(jButton3))
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(12, 12, 12)
	                        .addComponent(jLabel5))
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(jLabel6)))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel7)
	                    .addComponent(jLabel8))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jButton1))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(jLabel3))
	                .addGap(114, 114, 114))
	        );
	    }// </editor-fold>                        

	 
	 
	 //jSlider1 Handler
	 protected void jSlider1StateChanged(ChangeEvent evt) {
			this.jLabel2.setText(""+this.jSlider1.getValue());			
		}
	 
	 
	    //Accendi-Spegni
		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 	
	    	if(this.jButton1.getText().equals("Accendi")){
	    		if(this.jSlider1.getValue()!=0){
	    		this.tcsonAgent.turnOn(this.jSlider1.getValue());
	    		this.jButton1.setText("Spegni");
	    	   
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel6.setText("Acceso");
		         	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel6.setText("Spento");
		         	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	}
	    	//if(this.jButton1.getText().equals("Spegni"))
	    	else{
	    		this.tcsonAgent.turnOff();
	    	    this.jButton1.setText("Accendi");
	    	    
	    	    if(this.tcsonAgent.getState()==true){
		         	this.jLabel6.setText("Acceso");
		         	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
		         }
		         else{
		         	this.jLabel6.setText("Spento");
		         	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
		             }
	    	}
	    	
	    	 
	    	
	    }                                        
	   
	    //Back 
	    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 this.hidePanel();
	         this.mdPanel.menu();	    	 
	    }                                        

	    //Menu 
	    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	 this.hidePanel();
	         this.mdPanel.showPanel();
	    }           

	    // Variables declaration - do not modify                     
	    private javax.swing.JButton jButton1;
	    private javax.swing.JButton jButton2;
	    private javax.swing.JButton jButton3;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JLabel jLabel5;
	    private javax.swing.JLabel jLabel6;
	    private javax.swing.JLabel jLabel7;
	    private javax.swing.JLabel jLabel8;
	    private javax.swing.JSlider jSlider1;
	    // End of variables declaration          
    

	public void init(User usr) {
        user = usr;
        if(this.tcsonAgent.getState()==true){
        	this.jLabel6.setText("Acceso");
        	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
        }
        else{
        	this.jLabel6.setText("Spento");
        	this.jLabel8.setText(""+this.tcsonAgent.getTemperature());
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
