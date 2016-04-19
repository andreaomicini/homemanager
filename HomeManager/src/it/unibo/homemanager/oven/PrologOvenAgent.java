package it.unibo.homemanager.oven;

import it.unibo.homemanager.fridge.Ingredient;
import it.unibo.homemanager.fridge.PrologFridgeAgent;
import it.unibo.homemanager.fridge.Recipe;
import it.unibo.homemanager.fridge.TucsonAgentInterface;
import it.unibo.homemanager.fridge.TucsonShopperAgent;
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
	    private String fridge_name;//default fridge for operations
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
		    
		     
		     while(true){
		    	
		    	
		     }	
		    
		}

		
		public boolean deleteOrder(int id_recipe) {
			try{
				String tuple_name="cooking_order";
				Value id=new Value(id_recipe);
				LogicTuple template=new LogicTuple(tuple_name, new Var("X"), id, new Var("Y"), new Var("Z"),new Var("J"));
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

		/*
		  1:
		  0:mancano ingredienti
		  -2:errore comunicazione
		  */
		public int orderConfirm(ITucsonOperation order) {
			 LogicTuple template;
		     ITucsonOperation op_out;
		     String name_template;
		     String fridge_name;
		     int recipe_id, cooking_time, r_temperature,r_result;
			 try {
				
				 String result=order.getLogicTupleResult().toString();
					String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
					StringTokenizer t=new StringTokenizer(s1,",");
					if(t.countTokens()==5){
				            tp.appendText("lettura effettuata:"+s1);
				            fridge_name=t.nextToken().trim();
				            fridge_name=fridge_name.substring(1,fridge_name.length()-1);
				        	recipe_id=Integer.parseInt(t.nextToken().trim());
				        	cooking_time=Integer.parseInt(t.nextToken().trim());
				        	r_temperature=Integer.parseInt(t.nextToken().trim());      	
				        	r_result=Integer.parseInt(t.nextToken().trim());
					
	    		   
	    		    name_template="ok";
	    		    String conf="";
	    		    Value recipe= new Value(recipe_id);
	    		    Value fridgename=new Value(fridge_name);
	    		    Value confirm;
	    		    if(r_result==1){
	    		    	conf=conf+"ok";
	    		    	}
	    		    else{
	    		    	this.tp.appendText("Forno:non trovati ingredienti");
	    		    	return 0;
	    		    	}
	    		    
	    		    confirm=new Value(conf);
					template = new LogicTuple(name_template,fridgename, recipe, confirm);
					op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
					if(op_out.isResultSuccess())					
						                         return 1;        	   
										        
					
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
			return -2;
			
			
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
					    template = new LogicTuple(name_template, new Var("X"),new Value(recipe_id),new Var("W"),new Var("Z"),new Var("J"));
					   
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
		
		public int isThereOrder(Recipe recipe){
			
			try{
			 
		    
	    		    /*cerco ordine di cottura*/
		            String name_template=null;
		            LogicTuple template=null;
		            ITucsonOperation op_rdp=null;
		            name_template="cooking_order";
		            String fridge_name;
		            int recipe_id;
				    template = new LogicTuple(name_template, new Var("X"),new Value(recipe.getId()),new Var("W"),new Var("Z"),new Var("J"));
				   
					op_rdp = this.acc1.rdp(ovenfridge_tc, template, Long.MAX_VALUE);
					
					if(op_rdp.isResultSuccess()){
						this.tp.appendText("Forno:ordine trovato");
						 String result=op_rdp.getLogicTupleResult().toString();
							String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
							StringTokenizer t=new StringTokenizer(s1,",");
							if(t.countTokens()==5){
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
		
		public List<Recipe> readAllRecipes(){
		    List<Recipe> recipes=new ArrayList<Recipe>();
		    List<LogicTuple> l = null;
		    int n;
		    String name_template = "recipe";
		    LogicTuple template;
		    String result;
		    Recipe tmp;
		    try {
		        template = new LogicTuple(name_template, new Var("X"), new Var("Y"), new Var("J"), new Var("K"), new Var("W"));
		        ITucsonOperation op_rdAll = this.acc1.rdAll(ovenfridge_tc, template, Long.MAX_VALUE);
		        
		        if (op_rdAll.isResultSuccess()) {
		        	l = op_rdAll.getLogicTupleListResult();
		            
		            n=l.size();
		            for(int i=0;i<n;i++){	            	
		                result=l.get(i).toString();
		              
		            	String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
		            	  
				        StringTokenizer t = new StringTokenizer(s1, ",");
				        tmp=new Recipe();
				        if (t.countTokens() == 5){
				        	tmp.setId(Integer.parseInt(t.nextToken().trim()));
				        	tmp.setName(t.nextToken().trim());
				        	tmp.setName(tmp.getName().substring(1,tmp.getName().length()-1));
				        	tmp.setCookingtime(Integer.parseInt(t.nextToken().trim()));
				        	tmp.setTemperature(Integer.parseInt(t.nextToken().trim()));
				        	String ing=t.nextToken().trim();
				        	ing=ing.substring(1,ing.length()-1);
				        	
				        	StringTokenizer t2=new StringTokenizer(ing, ";");
							int N=t2.countTokens();
							
							for(int y=0;y<N;y++){
								tmp.addIngredient(createIngredientByString(t2.nextToken().trim()));
												}
							}
				     recipes.add(tmp);
				     this.tp.appendText(tmp.toString());
		            }
				return recipes;		
		        }	            
		         else {
		        	     return null;
		        }
		    } catch (Exception ex) {
		    	this.tp.appendText("readAllRecipes:lettura non effettuata con successo\nerrore:"+ex.toString());        
		    }
		
		return null;
	}

		private Ingredient createIngredientByString(String ing){
			Ingredient tmp=new Ingredient();
			StringTokenizer st=new StringTokenizer(ing,"-");
			if(st.countTokens()==2){
				tmp.setName(st.nextToken().trim());
				tmp.setQuantity(Integer.parseInt(st.nextToken().trim()));
				return tmp;		
			}
			else{return null;}
		}
		
		
		public boolean memorizeRecipe(Recipe recipe){
		    Value idrecipe= new Value(this.getRecipeId());
		    Value name_recipe=new Value(recipe.getName());
		    Value cooking_time=new Value(recipe.getCookingtime());
		    Value temperature=new Value(recipe.getTemperature());
		   
		    String list="";
			for(Ingredient temp:recipe.getIngredients())
				list=list+temp.getName()+"-"+temp.getQuantity()+";";
			list=list.substring(0, list.length()-1);
		    
					
		    Value ingreds  = new Value(list);
		    
		    
		    LogicTuple template = new LogicTuple("recipe",idrecipe, name_recipe, cooking_time, temperature, ingreds);
		    try {
		        ITucsonOperation op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
		        if(op_out.isResultSuccess()){
		            this.tp.appendText("Frigo: tupla recipe inserita:"+op_out.toString());
		        	return true;
		        			}
		    } catch (TucsonOperationNotPossibleException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (UnreachableNodeException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (OperationTimeOutException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    }
		
		
		
		return true;
		
		
	}
		
		public int getRecipeId(){
			
			
			try {
				LogicTuple template;
				ITucsonOperation op_rdp;
			for(int i=0;i<10000;i++){
			 	template = new LogicTuple("recipe",new Value(i), new Var("X"),new Var("Y"), new Var("W"), new Var("Z"));
			 	op_rdp = this.acc1.rdp(ovenfridge_tc, template, Long.MAX_VALUE);
			 	if(op_rdp.isResultFailure()){
			 		return i;
			 	}
			
			}
			 
			 
			 
			} catch (InvalidVarNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TucsonOperationNotPossibleException e) {
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
		
		public boolean insertTempRecipe(Recipe recipe){
		    Value idrecipe= new Value(recipe.getId());
		    Value name_recipe=new Value(recipe.getName());
		    Value cooking_time=new Value(recipe.getCookingtime());
		    Value temperature=new Value(recipe.getTemperature());
		   
		    String list="";
			for(Ingredient temp:recipe.getIngredients())
				list=list+temp.getName()+"-"+temp.getQuantity()+";";
			list=list.substring(0, list.length()-1);
		    
					
		    Value ingreds  = new Value(list);
		    
		    
		    LogicTuple template = new LogicTuple("recipe",idrecipe, name_recipe, cooking_time, temperature, ingreds);
		    try {
		        ITucsonOperation op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
		        if(op_out.isResultSuccess()){
		            this.tp.appendText("Frigo: tupla recipe inserita:"+op_out.toString());
		        	return true;
		        			}
		    } catch (TucsonOperationNotPossibleException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (UnreachableNodeException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (OperationTimeOutException ex) {
		        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
		    }
		
		
		
		return true;
		
		
	}
		
		
		
		
		/*
		 cookRecipe
		  1 se tutto è andato a buon fine
		  0 se non ci sono gli ingredienti
		 -1 errore eliminazione ingredienti dal frigo
		 -2 errore comunicazione con ovenfridge_tc
		 -3 conferma non ricevuta dal Frigo a scadenza timeout
		
		  */		
		/*
		 action=0:ricetta memorizzata
		 action=1:ricetta non memorizzata
		  */		
		public int cookRecipe(Recipe recipe,String fridgename, int action){
			
			Value fridge_name;
			Value idrecipe;
			Value action_tc;
			this.fridge_name=fridgename;
			if(action==1){
				if(!this.insertTempRecipe(recipe)){
				this.tp.appendText("Forno:errore comunicazione con ovenfridge_tc");	
				return -2;
				}
					
			}
			
			
			  fridge_name=new Value(fridgename); 
			  idrecipe=new Value(recipe.getId());  
			  action_tc=new Value(action);  
			    LogicTuple template = new LogicTuple("check_recipe",fridge_name,idrecipe,action_tc);
			    try {
			        ITucsonOperation op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
			        if(op_out.isResultSuccess()){
			            this.tp.appendText("Forno: tupla recipe inserita:"+op_out.toString());	
			            /*da continuare*/
			            int id_recipe,count;
						ITucsonOperation order;
						    
						    count=0;
						    id_recipe=this.isThereOrder(recipe); 
					    	 while((id_recipe<0)&&(count<20)){
					    		 this.tp.appendText("tupla non trovata");
					    		 try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
					    		 id_recipe=this.isThereOrder(recipe); 
					    		 count++;
					    	 }
					    	 
					    	 if(count!=20){
						    	 order=this.getOrder(id_recipe);
						    	 tp.appendText("Forno:ordine trovato\n");
						    	 if(order!=null){
						    	 int result=this.orderConfirm(order);
						    	 if(!this.deleteOrder(id_recipe))
						    		 return -2;
						    	 //conferma
						    	 return result;		    	 
						    	 }
						    	 else{
						    		 this.tp.appendText("Forno:errore comunicazione con ovenfridge_tc");	
							        	return -2; 
						    	 }
						    	 }
						    	 else{
						    		 this.tp.appendText("Forno:risposta dal frigo non ricevuto alla scadenza del timeout");
						    		 return -3;
						    	 }
					    	 
			            
			        			}
			        else{
			        	this.tp.appendText("Forno:errore comunicazione con ovenfridge_tc");	
			        	return -2;
			        }
			    } catch (TucsonOperationNotPossibleException ex) {
			        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
			        return -2;
			    } catch (UnreachableNodeException ex) {
			        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
			        return -2;
			    } catch (OperationTimeOutException ex) {
			        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
			        return -2;
			    }
			
			
			}
		
		public boolean orderBuyConfirm(Recipe recipe,boolean confirm){
			
			try{
			String name_template;
			LogicTuple template;
			 name_template="ok";
 		    String conf="";
 		    Value r_recipe= new Value(recipe.getId());
 		    Value fridgename=new Value(this.fridge_name);
 		    Value confirmed;
 		    if(confirm){
 		    	conf=conf+"ok";
 		    	}
 		    else{
 		    	conf=conf+"nok";
 		    	}
 		    
 		    
 		    ITucsonOperation op_out;
 		    confirmed=new Value(conf);
				template = new LogicTuple(name_template,fridgename, r_recipe, confirmed);
				op_out = this.acc1.out(ovenfridge_tc, template, Long.MAX_VALUE);
				if(op_out.isResultSuccess())					
					                         return true;        	   
									        
				
				
				
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
			
			
			
			return false;}
		
		public boolean deleteRecipe(Recipe recipe){
			LogicTuple template;
			String name_template="recipe";
			
			String ing="";
			for(Ingredient temp:recipe.getIngredients())
				ing=ing+temp.getName()+"-"+temp.getQuantity()+";";
			ing=ing.substring(0, ing.length()-1);
			
			
			template = new LogicTuple(name_template, new Value(recipe.getId()), new Value(recipe.getName()), new Value(recipe.getCookingtime()), new Value(recipe.getTemperature()), new Value(ing));
			try {
				ITucsonOperation op_inp = this.acc1.inp(ovenfridge_tc, template, Long.MAX_VALUE);
				if(op_inp.isResultSuccess())
				{return true;}
			} catch (TucsonOperationNotPossibleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnreachableNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OperationTimeOutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;	
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

