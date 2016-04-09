package it.unibo.homemanager.fridge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewFridgePanel;
import it.unibo.homemanager.userinterfaces.ViewManageDevicesPanel;
import it.unibo.homemanager.userinterfaces.ViewPrologFridgePanel;
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

public class PrologFridgeAgent extends AbstractTucsonAgent implements TucsonAgentInterface {
	
	
	
	
    private User user;
    private String name;
    private List<Ingredient> fridge_content;
    private EnhancedSynchACC acc;
    private TucsonTupleCentreId fridge_tc;
   // private TucsonTupleCentreId ovenfridge_tc;
    private ViewPrologFridgePanel viewFridge;
    private TracePanel tp;
    private int indexbuy=0;
    private int indexrecipe=0;
    private Recipe recipe1, recipe2;//ricette test
    private ViewManageDevicesPanel mdPanel;
    
	public PrologFridgeAgent(String id, String name, ViewManageDevicesPanel mdPanel,  TucsonTupleCentreId fridge_tc, TucsonTupleCentreId ovenfridge_tc, TracePanel tp) throws TucsonInvalidAgentIdException {
        super(id);
        this.name = name;
        this.mdPanel = mdPanel;
        this.tp = tp;
        this.fridge_tc = fridge_tc;
        //this.ovenfridge_tc=ovenfridge_tc;
        viewFridge = new ViewPrologFridgePanel(this.mdPanel, this, fridge_tc, ovenfridge_tc, tp);
        
		
    }
	
	public String getName() {
        return this.name;
    }
	
	protected void main() {
		//esecuzione fridge agent
		 this.acc = this.getContext();		 
	     tp.appendText("--" + this.getName().toUpperCase() + " AGENT" + "  STARTED.\n");
	     
	    
	    	 this.fridge_content=this.getContent();
	        
				
			
	 	/*
	 		     
	     //creazione ricette
	     //ricetta 1
	     recipe1=new Recipe(0,"Ciambellone");
	     recipe1.addIngredient(new Ingredient("farina",5));
	     recipe1.addIngredient(new Ingredient("zucchero",5));
	     recipe1.addIngredient(new Ingredient("uova",5));
	     recipe1.addIngredient(new Ingredient("acqua",3));
	     recipe1.addIngredient(new Ingredient("olio",3));
	     recipe1.addIngredient(new Ingredient("lievito",1));
	     recipe1.addIngredient(new Ingredient("buccialimone",1));
	     recipe1.addIngredient(new Ingredient("vanillina",1));
	     recipe1.addIngredient(new Ingredient("cacao",1));
	     //fine ricetta1
	     
	     //ricetta 2
	     recipe2=new Recipe(1,"Cantucci");
	     recipe2.addIngredient(new Ingredient("farina",10));
	     recipe2.addIngredient(new Ingredient("zucchero",6));
	     recipe2.addIngredient(new Ingredient("uova",3));
	     recipe2.addIngredient(new Ingredient("latte",3));
	     recipe2.addIngredient(new Ingredient("olio",2));
	     recipe2.addIngredient(new Ingredient("lievito",1));
	     recipe2.addIngredient(new Ingredient("buccialimone",1));
	     recipe2.addIngredient(new Ingredient("mandorle",6));
	     recipe2.addIngredient(new Ingredient("succolimone",1));
	     //fine ricetta 2
	      
	     try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	     //controllo ricetta 1
	     
	     if(recipe1.checkRecipe(this.fridge_content)){
	    	 tp.appendText("ricetta 1 ok");
	    	if(this.sendCookingOrder(recipe1)){
	    		
	    		while(!this.getConfirm(recipe1)){	    			
	    		}
				    if(this.deleteRecipeIngredientsFromFridge(recipe1))
					tp.appendText("ingredienti ricetta 1 eliminati");
	    	}
				     
	      
	    }
	     
	    	 
	     
	     //fine controllo ricetta 1
	     
	     try {
				this.fridge_content=this.getContent();
			} catch (Exception e) {
				    e.printStackTrace();
			}
	     
	     if(recipe2.checkRecipe(this.fridge_content)){
	    	 tp.appendText("ricetta 2 ok");
	    	 if(this.sendCookingOrder(recipe2)){
	    		  while(!this.getConfirm(recipe2)){	
	    			  tp.appendText("frigo:conferma non trovata");
	    			 try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		 }
	    		   	if(this.deleteRecipeIngredientsFromFridge(recipe2))
						tp.appendText("ingredienti ricetta 2 eliminati");
	    		   	else
	    		   		tp.appendText("ordine non confermato");
	    	 }
	     }
	     //fine controllo ricetta 2
	     
	     
		try {
			this.fridge_content=this.getContent();
		} catch (Exception e) {
			    e.printStackTrace();
		}
		
		//rimane in esecuzione	
	     */
	    	 
	    	
	 
	   
	    while(true){
			}
	     
	}
	
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
		
	}
	
	public void operationCompleted(ITucsonOperation arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
		
	}
	
	
	
	/*
	 cookRecipe
	  1 se tutto è andato a buon fine
	  0 se non ci sono gli ingredienti
	 -1 errore comunicazione con fridge_tc
	 -2 conferma non ricevuta dal Forno a scadenza timeout
	 -3 errore nell'eliminazione degli ingredienti utilizzati dal contenuto del frigo
	  */
	public int cookRecipe(Recipe recipe){
		    int count;
		    
		    this.fridge_content=this.getContent();
			if(recipe.checkRecipe(this.fridge_content)){
			if(this.sendCookingOrder(recipe)){
				tp.appendText("Frigo:ordine cottura inviato");
				count=0;
				while((!this.getConfirm(recipe))&&count<20){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
							}
				count++;
	    		}
				if(count!=20){
					tp.appendText("Frigo:conferma ricevuta dal Forno");
					if(this.deleteRecipeIngredientsFromFridge(recipe)){
						tp.appendText("Frigo:ingredienti ricetta eliminati da fridge_tc ");	
						this.fridge_content=this.getContent();
					return 1;	
					}				
					else{
						tp.appendText("Frigo:errore nell'eliminazione degli ingredienti utilizzati dal contenuto del frigo ");
						return -3;
					}
				}
				else{
					tp.appendText("Frigo:conferma non ricevuta dal Forno a scadenza timeout");	
					return-2;
				}					
			}
			else{
				tp.appendText("Frigo:errore comunicazione con fridge_tc");
				return -1;
			}
			}
			else{
				tp.appendText("Frigo:ricetta non cucinabile al momento per mancanza ingredienti");
				return 0;
			}
			
		}
	
	public ArrayList<Ingredient> getContent()  {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        List<LogicTuple> l = null;
        Value fridge_name = new Value(this.name);
        String name_template = "fridge_content";

       
        
		try {
			 LogicTuple template = new LogicTuple(name_template, fridge_name, new Var("X"), new Var("Y"));
			
			ITucsonOperation op_rdAll = this.acc.rdAll(this.fridge_tc, template, Long.MAX_VALUE);
			 if (op_rdAll.isResultSuccess()) {
		            l = op_rdAll.getLogicTupleListResult();
		            for (int i = 0; i < l.size(); i++) {
		                String content = l.get(i).toString();
		                tp.appendText(content);
		                list.add(createElementByTupleString(content));
		            }
		        } else {
		            System.err.println("[readCentreArray(TA)@TucsonDatabase] rdAll(" + template + ") failed!");
		        }
		        return list;
		} catch (TucsonOperationNotPossibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnreachableNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidVarNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
       
    }

    private Ingredient createElementByTupleString(String s) {
        String s1 = s.substring(s.indexOf("(") + 1, s.length() - 1);
        StringTokenizer t = new StringTokenizer(s1, ",");
        if (t.countTokens() == 3) {
            String nameDevice = (t.nextToken());
            String nameIngredient = (t.nextToken());
            int quantity = Integer.parseInt(t.nextToken());
            return new Ingredient(nameIngredient, quantity);
        }
        return null;
    }
	
	
public boolean updateFridgeContent(String ingredientName, int quantity){
	try{
	 String name_template = "fridge_content";
	 Value fridge_name = new Value(this.name);
	 Value ingredient_name = new Value(ingredientName);
     LogicTuple template = new LogicTuple(name_template, fridge_name, ingredient_name, new Var("Y"));
     ITucsonOperation op_rdp = this.acc.rdp(fridge_tc, template, Long.MAX_VALUE);
     if(op_rdp.isResultSuccess()){
    	 
    	 String content=op_rdp.getLogicTupleResult().toString();
    	 Ingredient temp=this.createElementByTupleString(content);
    	 String name_template1 = "fridge_content";
         Value fridge_name1 = new Value(this.name);
         Value ingredient_name1 = new Value(temp.getName());
         Value oldquantity1 = new Value(temp.getQuantity());
         Value newquantity=new Value(quantity);
         LogicTuple oldtemplate = new LogicTuple(name_template1, fridge_name1, ingredient_name1, oldquantity1);
         ITucsonOperation op_inp = this.acc.inp(fridge_tc, oldtemplate, Long.MAX_VALUE);
         if(op_inp.isResultFailure())
        	 return false;
         
         if(quantity>0){         
         LogicTuple newtemplate=new LogicTuple(name_template1, fridge_name1,ingredient_name1,newquantity);
         ITucsonOperation op_out=this.acc.out(fridge_tc,newtemplate,Long.MAX_VALUE);         
         if(op_out.isResultSuccess())
        	 return true;
     }
     else 
    	 return true;	 
     }
	}catch(TucsonOperationNotPossibleException ex) {
        Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnreachableNodeException ex) {
        Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (OperationTimeOutException ex) {
        Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidVarNameException ex) {
    	Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
	}
     return false;
}	

public boolean deleteRecipeIngredientsFromFridge(Recipe recipe){
	int quantity;
	boolean test=true;
	for(Ingredient temp:recipe.getIngredients()){
		quantity=temp.getIngredientQuantityFromList(fridge_content);
		quantity=quantity-temp.getQuantity();
		if(!this.updateFridgeContent(temp.getName(), quantity))
        test=false;
		}
return test;	
}	

public boolean getConfirm(Recipe recipe){

	 try {
     	String name_template="ok";
     	String fridge_name;
			LogicTuple template = new LogicTuple(name_template,new Value(this.name), new Var("X"), new Var("Y"));
			ITucsonOperation op_rdp = this.acc.rdp(fridge_tc, template, Long.MAX_VALUE);
			if(op_rdp.isResultSuccess()){
				String result=op_rdp.getTupleResult().toString();
				String s1 = result.substring(result.indexOf("(") + 1, result.length() - 1);
			        StringTokenizer t = new StringTokenizer(s1, ",");
			        if (t.countTokens() == 3){
			        	fridge_name=t.nextToken().trim();
			        	fridge_name=fridge_name.substring(1,fridge_name.length()-1);
			        	int id=Integer.parseInt(t.nextToken().trim());
			        	String ans=t.nextToken().trim();
			        	System.out.println(ans);			        	   	
			        	template = new LogicTuple(name_template,new Value(fridge_name),new Value(id), new Value(ans));
						ITucsonOperation op_inp = this.acc.inp(fridge_tc, template, Long.MAX_VALUE);
						/*controllo risposta forno*/
      
			        	if((recipe.getId()==id)&&(ans.equalsIgnoreCase("ok"))){
			        		tp.appendText("Frigo: conferma ricevuta");
			        		return true;
			        		}
			        	/*altrimenti l'operazione è stata annullata lato forno*/
		        	
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
		return false;
      
}

public boolean sendCookingOrder(Recipe recipe){
    

    
    	 /*tupla al forno*/
    	
         try {
        	 Value recipe_id = new Value(recipe.getId());
        	 Value cooking_time = new Value(recipe.getCookingtime());
        	 Value r_temperature = new Value(recipe.getTemperature());
             String name_template = "cooking_order";
             LogicTuple template = new LogicTuple(name_template,new Value(this.name), recipe_id,cooking_time,r_temperature);
        	 ITucsonOperation op_out = this.acc.out(this.fridge_tc, template, Long.MAX_VALUE);
        	 if(op_out.isResultFailure()){
        		 tp.appendText("frigo: richiesta non inviata");
        		 return false; 
        	         
        	 }
        	 else{        		 
        	 tp.appendText("frigo: richiesta inviata");
        	 return true;
        	 }
         } catch (TucsonOperationNotPossibleException ex) {
             Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
         } catch (UnreachableNodeException ex) {
             Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
         } catch (OperationTimeOutException ex) {
             Logger.getLogger(PrologFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    	 return false;
         
     }

public void insertOrder(List<Ingredient> ingredients){
	String list="";
	for(Ingredient temp:ingredients)
		list=list+temp.getName()+"-"+temp.getQuantity()+";";
	list=list.substring(0, list.length()-1);
			
	this.insertOrderTuple(list);		
}

//<tuple>buy(1,'milk-4;eggs-2;cheese-3','2014-12-15_15.30.40')</tuple>
private boolean insertOrderTuple(String list) {
    Date d = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd'_'hh.mm.ss");
    Value date = new Value(df.format(d));
    Value ingreds = new Value(list);
    Value numOrder = new Value(this.indexbuy);
    this.indexbuy++;
    LogicTuple template = new LogicTuple("buy", numOrder, ingreds, date);
    try {
        ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
        if(op_out.isResultSuccess()){
            this.tp.appendText("frigo: tupla ordine inserita:"+op_out.toString());
        	return true;
        			}
    } catch (TucsonOperationNotPossibleException ex) {
        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnreachableNodeException ex) {
        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (OperationTimeOutException ex) {
        Logger.getLogger(TucsonShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.tp.appendText("frigo: errore inserimento ordine");
    return false;
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
	        ITucsonOperation op_rdAll = this.acc.rdAll(fridge_tc, template, Long.MAX_VALUE);
	        
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

public int getRecipeId(){
	LogicTuple template;
	ITucsonOperation op_rdp;
	int i;
	try {
	for(i=0;i<10000;i++){
	 	template = new LogicTuple("recipe",new Value(i), new Var("X"),new Var("Y"), new Var("W"), new Var("Z"));
	 	op_rdp = this.acc.rdp(fridge_tc, template, Long.MAX_VALUE);
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
	        ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
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
public boolean deleteRecipe(Recipe recipe){
	LogicTuple template;
	String name_template="recipe";
	
	String ing="";
	for(Ingredient temp:recipe.getIngredients())
		ing=ing+temp.getName()+"-"+temp.getQuantity()+";";
	ing=ing.substring(0, ing.length()-1);
	
	
	template = new LogicTuple(name_template, new Value(recipe.getId()), new Value(recipe.getName()), new Value(recipe.getCookingtime()), new Value(recipe.getTemperature()), new Value(ing));
	try {
		ITucsonOperation op_inp = this.acc.inp(fridge_tc, template, Long.MAX_VALUE);
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

public void show(User user) {
	this.viewFridge.init(user);
	
}


public void hide() {
	this.viewFridge.hidePanel();
}


public JPanel getInterface() {
	return this.viewFridge;
}




}
	