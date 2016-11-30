package it.unibo.homemanager.situatedness;

import java.util.ArrayList;
import java.util.List;

import it.unibo.homemanager.agents.Ingredient;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.core.TransducersManager;
import alice.respect.situatedness.AbstractProbeId;
import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

public class FridgeProbe implements ISimpleProbe {
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "20504";
    private EnhancedSynchACC acc;
    private final AbstractProbeId pid;
    private TucsonTupleCentreId probeFridgeTc;
    private TransducerId tid;
    private TransducerStandardInterface transducer;
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

    public FridgeProbe(final AbstractProbeId i) {
	    this.pid = i;
	    try {
	        final TucsonAgentId aid = new TucsonAgentId("actuator" + i);
	        this.acc = TucsonMetaACC.getContext(aid, FridgeProbe.DEFAULT_HOST, Integer.valueOf(FridgeProbe.DEFAULT_PORT));
	    } catch (final TucsonInvalidAgentIdException e) {
	        e.printStackTrace();
	    }
	    
	    try {
			this.probeFridgeTc = new TucsonTupleCentreId("probeFridgeTc", "localhost", "20504");
		} catch (TucsonInvalidTupleCentreIdException e) {
			e.printStackTrace();
		}
    
	    System.err.println("[" + this.pid + "]: I'm alive");
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
		
        if (this.tid == null) {
            System.err.println("["+this.pid+"]Don't have any transducer associated yet!");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("["+this.pid+"]Can't retrieve my transducer!");
                return false;
            }
        }
        boolean new_ingredient = true;
		for(Ingredient ingredient : ingredients){
	    	String correct_key = ingredient.getName();
	    	if (key.equals(correct_key)){
	    		// Tuple template to match
	    		try{
		    		final LogicTuple template = LogicTuple.parse("content("+ key +", Q)");
		            final ITucsonOperation op = this.acc.rd(this.probeFridgeTc, template, null);
		            if (op.isResultSuccess()) {
		                final String quantity = op.getLogicTupleResult().getArg(1).toString();
		                System.err.println("["+this.pid+"]My content is: "+"num."+quantity+" of "+key);
			        	// Notify the Transducer
		                this.transducer.notifyEnvEvent(key,Integer.parseInt(quantity),AbstractTransducer.GET_MODE);
		                return true;
		            }
	    		}catch(TucsonOperationNotPossibleException | UnreachableNodeException |
	            		OperationTimeOutException | InvalidLogicTupleException e) {
	    			e.printStackTrace();
	    		}   
	    	}
	    }
        
        System.err.println("["+this.pid+"]Unknown property "+key);
    	return false;
    	
	}
	
	@Override
	public boolean writeValue(String key, int state) {
	
        if (this.tid == null) {
            System.err.println("["+this.pid+"]Don't have any transducer associated yet!");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("["+this.pid+"]Can't retrieve my transducer!");
                return false;
            }
        }
        boolean new_ingredient = true;
        int index = -1;
        for(Ingredient ingredient : ingredients){
        	String correct_key = ingredient.getName();
        	if (key.equals(correct_key)){
        		index = ingredients.indexOf(ingredient);
        		new_ingredient = false;
        		break;
        	}
        }
        if(!new_ingredient){
        	// I've already had that ingredient in the fridge
        	// I have to check the state value
        	int ing_quantity = ingredients.get(index).getQuantity();
        	int new_quantity = ing_quantity + state;
        	if(new_quantity > 0){
        		// The value has to be updated
        		try {
    	            final LogicTuple template = LogicTuple.parse("content(" + key + " , Q)");
    	            final ITucsonOperation op = this.acc.in(this.probeFridgeTc, template,null);
    	            if (op.isResultSuccess()) {
    	            	ingredients.get(index).setQuantity(new_quantity);
    	                final LogicTuple tempTuple = LogicTuple.parse("content("+key+","+new_quantity+")");
    	                this.acc.out(this.probeFridgeTc, tempTuple, null);
    	                System.err.println("["+this.pid+"]Now we have" +new_quantity+" of "+key);
    	                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.SET_MODE);
    	                return true;
    	            }
        		}catch(TucsonOperationNotPossibleException | UnreachableNodeException |
                		OperationTimeOutException | InvalidLogicTupleException e) {
        			e.printStackTrace();
        		}
        	}else if(new_quantity == 0){
        		// The ingredient has to be deleted
        		try {
        			Ingredient ing_deleted = ingredients.remove(index);
    	            final LogicTuple template = LogicTuple.parse("content(" + key + " , Q)");
    	            final ITucsonOperation op = this.acc.in(this.probeFridgeTc, template,null);
    	            if (op.isResultSuccess()) {
    	                System.err.println("["+this.pid+"]Ingredient "+key+" deleted");
    	                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.SET_MODE);
    	                return true;
    	            }
        		}catch(TucsonOperationNotPossibleException | UnreachableNodeException |
                		OperationTimeOutException | InvalidLogicTupleException e) {
        			e.printStackTrace();
        		}
        	}
    	}else{
    		// A new ingredient
    		try{
    			ingredients.add(new Ingredient(key, state));
    			LogicTuple tempTuple;
				tempTuple = LogicTuple.parse("content(" + key + ", "+ state + ")");
				final ITucsonOperation op = this.acc.out(this.probeFridgeTc, tempTuple, null);
				if (op.isResultSuccess()) {
					System.err.println("["+this.pid+"]We have new ingredients: "+state+" of "+key);
	                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.SET_MODE);
	                return true;
	            }
			} catch (TucsonOperationNotPossibleException | UnreachableNodeException | 
					OperationTimeOutException | InvalidLogicTupleException e) {
				e.printStackTrace();
			}
    	}
        System.err.println("["+this.pid+"]Unknown property " + key);
        return false;
        
	}
}
