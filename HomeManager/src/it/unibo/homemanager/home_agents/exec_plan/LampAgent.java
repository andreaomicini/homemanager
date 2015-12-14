/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.homemanager.home_agents.exec_plan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidTupleArgumentException;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.utilities.Utils;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import cli.System.IO.IOException;
import it.unibo.homemanager.tablemap.Lamp;
import it.unibo.homemanager.userinterfaces.TracePanel;

/**
 *
 * @author sik
 */
public class LampAgent extends AbstractTucsonAgent {

    private TucsonTupleCentreId room_tc;
    private TracePanel tp;
    private Lamp l;
    private String state;
    
    public LampAgent(String id, TracePanel tp, TucsonTupleCentreId tid, Lamp lamp) throws TucsonInvalidAgentIdException {
        super(id);
        this.room_tc = tid;
        this.tp = tp;
        this.l = lamp;
        this.state = "off";
    }

    public LampAgent(String id, TracePanel tp, TucsonTupleCentreId tid, Lamp lamp, String state) throws TucsonInvalidAgentIdException {
        super(id);
        this.room_tc = tid;
        this.tp = tp;
        this.l = lamp;
        this.state = state;
    }
    
    @Override
    public void operationCompleted(ITucsonOperation ito) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void operationCompleted(AbstractTupleCentreOperation atco) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    protected void main() {
    	try {
    		
	    	//Inizializzazione
	        LogicTuple template_in = null;
	        try {
	            template_in = new LogicTuple("light_mode",new Value(l.sensId),new Var("X"));
	        } catch (InvalidVarNameException e) {
	            // cannot happen
	        }
	        
	        EnhancedSynchACC acc = this.getContext();
	        
	        TucsonTupleCentreId configTc = new TucsonTupleCentreId("'$ENV'", "localhost", "20504");
	        
	        String config = Utils.fileToString("it/unibo/homemanager/situatedness/lightSpec.rsp");
	        
	        config = config.replaceAll("#", "" + l.idL);
	        
	        this.outS(room_tc, config);
	        
	        
	        final LogicTuple actuatorTuple = new LogicTuple(
	                "createTransducerActuator",
	                new TupleArgument(room_tc.toTerm()), // tuple centre id
	                new Value(
	                        "it.unibo.homemanager.situatedness.LightTransducer"), // java class implementing the transducer
	                        new Value("lightTransducer_" + l.idL), // transducer id
	                        new Value(
	                                "it.unibo.homemanager.situatedness.ActualLight"), // java class implementing the probe
	                                new Value("lamp_" + l.idL)); // probe id
	        
	        acc.out(configTc, actuatorTuple, null);
	        
	        tp.appendText("-- LAMP AGENT FOR "+l.name+" STARTED.");
	            
	        while(true) {
	            // wait for command
	            ITucsonOperation op_in = acc.in(room_tc, template_in, Long.MAX_VALUE);
	            
	            LogicTuple result = op_in.getLogicTupleResult();
	            
	            System.out.println("LAMPAGENT "+l.name+": " + op_in.getLogicTupleResult());
	            
	            LogicTuple action = null;
	            
	            action = LogicTuple.parse("act(" + result +")");
	            
	            tp.appendText("LampAgent "+l.name+": cambio stato: " + action);
	            
	            acc.out(room_tc, action, null);	            
	        }   
        }
        catch(Exception ex) {
            ex.printStackTrace();
            tp.appendText("LampAgent "+l.name+": error in lights activity.");
        }
    }
    
    //Questa funzione realizza una outS uguale alla setS, che prende in ingresso una stringa anziché le singole tuple che costituiscono la reaction
    protected boolean outS(TucsonTupleCentreId tc, String s) {
    	
    	EnhancedSynchACC acc = this.getContext();
    	
        ITucsonOperation attuale;
		try {
			attuale = acc.getS(tc, null);

	        List<LogicTuple> tupleattuali =  attuale.getLogicTupleListResult();
	        for(int i=0; i < tupleattuali.size(); i++) {
	        	System.out.println( tupleattuali.get(i).toString());
	        	String newconfig = tupleattuali.get(i).toString();
	        	newconfig.replaceAll("[\\[\\]]","");
	        	s = s + newconfig + ".\n";
	        }
	        
	        acc.setS(tc, s, null);
		} catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;   	

    }
    
}
