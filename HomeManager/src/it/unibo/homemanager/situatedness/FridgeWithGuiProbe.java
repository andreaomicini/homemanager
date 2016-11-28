package it.unibo.homemanager.situatedness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alice.respect.core.TransducersManager;
import alice.respect.situatedness.AbstractProbeId;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import it.unibo.homemanager.agents.Ingredient;
import it.unibo.homemanager.userinterfaces.agents.FridgeProbeFrame;

public class FridgeWithGuiProbe implements ISimpleProbe {
    
	private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "20504";
    private EnhancedSynchACC acc;
    private final AbstractProbeId pid;
    private TransducerId tid;
    private TransducerStandardInterface transducer;
    private FridgeProbeFrame f_frame;
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	
    public FridgeWithGuiProbe(final AbstractProbeId i) throws IOException {
	    this.pid = i;
	    try {
	        final TucsonAgentId aid = new TucsonAgentId("actuator" + i);
	        this.acc = TucsonMetaACC.getContext(aid, FridgeWithGuiProbe.DEFAULT_HOST, Integer.valueOf(FridgeWithGuiProbe.DEFAULT_PORT));
	    } catch (final TucsonInvalidAgentIdException e) {
	        e.printStackTrace();
	    }
    
	    System.err.println("[" + this.pid + "]: I'm alive");
	    
	    f_frame = new FridgeProbeFrame(pid.toString());
    }
    
    @Override
	public AbstractProbeId getIdentifier() {
		return this.pid;
	}

	@Override
	public TransducerId getTransducer() {
		return this.tid;
	}
	
	@Override
	public void setTransducer(TransducerId arg0) {
		this.tid = arg0;
	}
	
	@Override
	public boolean readValue(String key) {
		System.err.println("[" + this.pid + "]: readValue method");
		
		if (this.tid == null) {
            System.err.println("[" + this.pid + "]: Don't have any transducer associated yet!");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid + "]: Can't retrieve my transducer!");
                return false;
            }
        }
        
        boolean new_ingredient = true;
        int index = 0;
        for(;index<ingredients.size();index++){
        	String correct_key = ingredients.get(index).getName();
        	if (key.equals(correct_key)){
        		new_ingredient = false;
        		break;
        	}
        }
        
        if(!new_ingredient){
    		// Remove the ingredient from the list
    		Ingredient ingredient_removed = ingredients.remove(index);
    		for(Ingredient i : ingredients) {
                System.out.println("Ingredient in the list: " + i.getName() +" qt." + i.getQuantity() +"\n");
            }
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ " removed from list");
    		f_frame.deleteIngredient(key);
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ " removed from table");
        	return true;
	    }else{
	    	System.err.println("[" + this.pid + "]: Can't found the ingredient!");
			return false;
        }
	}
	
	@Override
	public boolean writeValue(String key, int state) {
		System.err.println("[" + this.pid + "]: writeValue method");
		
		if (this.tid == null) {
            System.err.println("[" + this.pid + "]: Don't have any transducer associated yet!");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid + "]: Can't retrieve my transducer!");
                return false;
            }
        }
        
        boolean new_ingredient = true;
        int index = 0;
        for(;index<ingredients.size();index++){
        	String correct_key = ingredients.get(index).getName();
        	System.out.println("[" + this.pid + "] - Ingredient to be checked: " +correct_key);
        	if (key.equals(correct_key)){
        		new_ingredient = false;
        		break;
        	}
        }
        
        if(!new_ingredient){
        	// This ingredient is already been in the fridge
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ " found");
    		Ingredient ing = ingredients.get(index);
    		int quantity = ing.getQuantity();
    		for(Ingredient i : ingredients) {
                System.out.println("Ingredient in the list: " + i.getName() +" qt." + i.getQuantity() +"\n");
            }
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ "old quantity: " + quantity);
    		int new_quantity = quantity + state;
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ "new quantity: " + new_quantity);
    		ingredients.get(index).setQuantity(new_quantity);
    		for(Ingredient i : ingredients) {
                System.out.println("Ingredient in the list: " + i.getName() +" qt." + i.getQuantity() +"\n");
            }
    		System.out.println("[" + this.pid + "] - Ingredient: " +key+ "set new quantity");
    		f_frame.addIngredient(key,new_quantity);
    		System.out.println("[" + this.pid + "] - Ingredient upgraded");
    		return true;
        }else{
//        	// New ingredient
        	System.out.println("[" + this.pid + "] - New ingredient: " +key+ " found");
        	ingredients.add(new Ingredient(key, state));
            f_frame.addIngredient(key,state);
            for(Ingredient i : ingredients) {
                System.out.println("Ingredient in the list: " + i.getName() +" qt." + i.getQuantity() +"\n");
            }
            System.out.println("[" + this.pid + "] - New ingredient: " +key+ " added to table");
            return true;
        }
        
	}

}
