package it.unibo.homemanager.oven;

import it.unibo.homemanager.fridge.PrologFridgeAgent;
import it.unibo.homemanager.fridge.TucsonAgentInterface;
import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageDevicesPanel;
import it.unibo.homemanager.userinterfaces.ViewOvenPanel;
import it.unibo.homemanager.userinterfaces.ViewPrologFridgePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;


public class PrologOvenAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	 private User user;
	    private String name;
	    private boolean state;/*true=on;false=off*/
	    private int temperature;
	    private EnhancedSynchACC acc1;
	    public TucsonTupleCentreId ovenfridge_tc;
	    private ViewOvenPanel viewOven;
	    private TracePanel tp;
		private ViewManageDevicesPanel mdPanel;
	    
	    public PrologOvenAgent(String id, String name, ViewManageDevicesPanel mdPanel, TucsonTupleCentreId ovenfridge_tc, TracePanel tp) throws TucsonInvalidAgentIdException {
	        super(id);
	        this.name = name;
	        this.state=false;
	        this.temperature=0;
	        this.mdPanel = mdPanel;
	        this.tp = tp;
	        this.ovenfridge_tc=ovenfridge_tc;
	        viewOven = new ViewOvenPanel(mdPanel, this, ovenfridge_tc, tp);
	        
	    }
		
		public String getName() {
	        return this.name;
	    }
		
		public boolean getState(){
			return this.state;
		}
		
		public int getTemperature(){
			return this.temperature;
		}
		
		
		
		protected void main() {
			
			 this.acc1 = this.getContext();
		     tp.appendText("--" + this.getName().toUpperCase() + " AGENT" + "  STARTED.");
		    
		     boolean isthereorder;
		      ITucsonOperation order;
		      int id_recipe;
		     //if(id_recipe>=0)
	    		
		     
		     while(true){
		    	
		    	 try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	
		    	 id_recipe=this.isThereOrder(); 
		    	 while(id_recipe<0){
		    		 tp.appendText("tupla non trovata");
		    		 try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    		 id_recipe=this.isThereOrder(); 
		    	 }
		    	 order=this.getOrder(id_recipe);
		    	 tp.appendText("Forno:ordine trovato\n");
		    	 if(order!=null){
		    	 id_recipe=this.orderConfirm(order, true);
		    	 /*conferma*/
		    	 if(id_recipe>0){
		    		 tp.appendText("Forno:ordine confermato");
		    		 if(this.deleteOrder(id_recipe))
		    			 tp.appendText("Forno:tupla ordine eliminata");
		    	 }
		    	 
		    	 } 
		     }	
		    
		}

		
		public boolean deleteOrder(int id_recipe) {
			try{
				String tuple_name="cooking_order";
				Value id=new Value(id_recipe);
				LogicTuple template=new LogicTuple(tuple_name, new Var("X"), id, new Var("Y"), new Var("Z"));
				ITucsonOperation op_inp=this.acc1.inp(this.ovenfridge_tc,template,Long.MAX_VALUE);
				if(op_inp.isResultSuccess())
					return true;
			}catch (TucsonOperationNotPossibleException ex) {
	             Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
	         } catch (UnreachableNodeException ex) {
	             Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
	         } catch (OperationTimeOutException ex) {
	             Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
	         } catch (InvalidVarNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		public int orderConfirm(ITucsonOperation order,boolean confirmed) {
			 LogicTuple template;
		     ITucsonOperation op_out;
		     String name_template;
		     String fridge_name;
		     int recipe_id, cooking_time, r_temperature;
			 try {
				
				 String result=order.getLogicTupleResult().toString();
					String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
					StringTokenizer t=new StringTokenizer(s1,",");
					if(t.countTokens()==4){
				            tp.appendText("lettura effettuata:"+s1);
				            fridge_name=t.nextToken().trim();
				            fridge_name=fridge_name.substring(1,fridge_name.length()-1);
				        	recipe_id=Integer.parseInt(t.nextToken().trim());
				        	cooking_time=Integer.parseInt(t.nextToken().trim());
				        	r_temperature=Integer.parseInt(t.nextToken().trim());      	
				        	
					
	    		   
	    		    name_template="ok";
	    		    String conf="";
	    		    Value recipe= new Value(recipe_id);
	    		    Value fridgename=new Value(fridge_name);
	    		    Value confirm;
	    		    if(confirmed){
	    		    	conf=conf+"ok";
	    		    	}
	    		    else{
	    		    	conf=conf+"nok";
	    		    	}
	    		    
	    		    confirm=new Value(conf);
					template = new LogicTuple(name_template,fridgename, recipe, confirm);
					op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
					if(op_out.isResultSuccess())					
						                         return recipe_id;        	   
										        
					
					}
					
			 }
				 catch (TucsonOperationNotPossibleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnreachableNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OperationTimeOutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return -1;
			
			
		}

		
		
		public void operationCompleted(AbstractTupleCentreOperation arg0) {
			throw new UnsupportedOperationException("Not supported yet.");
			
		}

		
		public void operationCompleted(ITucsonOperation arg0) {
			throw new UnsupportedOperationException("Not supported yet.");
			
		}
		
		public ITucsonOperation getOrder(int recipe_id){
			try{
				      LogicTuple template;
				      String name_template;
			          ITucsonOperation op_rdp;
			            name_template="cooking_order";
					    template = new LogicTuple(name_template, new Var("X"),new Value(recipe_id),new Var("W"),new Var("Z"));
					   
						op_rdp = this.acc1.rdp(ovenfridge_tc, template, Long.MAX_VALUE);
						
						if(op_rdp.isResultSuccess()){
							return op_rdp;
						}
				        }catch (InvalidVarNameException ex) {
				            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
				        } catch (TucsonOperationNotPossibleException ex) {
				            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
				        } catch (UnreachableNodeException ex) {
				            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
				        } catch (OperationTimeOutException ex) {
				            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
				        } catch(NumberFormatException ex){
				        	Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
				        }
			return null;
		}
		
		public int isThereOrder(){
			
			try{
			 
		    
	    		    /*cerco ordine di cottura*/
		            String name_template=null;
		            LogicTuple template=null;
		            ITucsonOperation op_rdp=null;
		            name_template="cooking_order";
		            String fridge_name;
		            int recipe_id;
				    template = new LogicTuple(name_template, new Var("X"),new Var("Y"),new Var("W"),new Var("Z"));
				   
					op_rdp = this.acc1.rdp(ovenfridge_tc, template, Long.MAX_VALUE);
					
					if(op_rdp.isResultSuccess()){
						this.tp.appendText("Forno:ordine trovato");
						 String result=op_rdp.getLogicTupleResult().toString();
							String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
							StringTokenizer t=new StringTokenizer(s1,",");
							if(t.countTokens()==4){
						            tp.appendText("lettura effettuata:"+s1);
						            fridge_name=t.nextToken().trim();
						            recipe_id=Integer.parseInt(t.nextToken().trim());
						        	
						return recipe_id;
						}
					}
			        }catch (InvalidVarNameException ex) {
			            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (TucsonOperationNotPossibleException ex) {
			            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (UnreachableNodeException ex) {
			            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (OperationTimeOutException ex) {
			            Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
			        } catch(NumberFormatException ex){
			        	Logger.getLogger(PrologOvenAgent.class.getName()).log(Level.SEVERE, null, ex);
			        }	
			          
					
				
			return -1;
	    	
	     }
		
		public void turnOn(int temperature){
			this.state=true;
			this.temperature=temperature;
		}
		
		public void turnOff(){
			this.state=false;
			this.temperature=0;
		}

		public void show(User user) {
			this.viewOven.init(user);
			
		}


		public void hide() {
			this.viewOven.hidePanel();
		}


		public JPanel getInterface() {
			return this.viewOven;
		}

		}

