package it.unibo.homemanager.agents;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;

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
import it.unibo.homemanager.detection.*;
import it.unibo.homemanager.userinterfaces.TracePanel;

public class FridgeSituatedAgent extends AbstractTucsonAgent {

	private Device device;
	private String name;
	private TracePanel tracePanel;
	
	private TucsonTupleCentreId fridgeTc;
	
    public FridgeSituatedAgent(Device device, TucsonTupleCentreId fridgeTc, TracePanel tracePanel) throws TucsonInvalidAgentIdException {
		
    	super(device.getDeviceName() + "_" + device.getDeviceId());
		
		this.device = device;
		this.name = device.getDeviceName() + "_" + device.getDeviceId();
		this.tracePanel = tracePanel;
		
		this.fridgeTc = fridgeTc;
	
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
    		
    		Thread.sleep(30000);
    		tracePanel.appendText("--******-- FRIDGE SITUATED AGENT FOR "+ name +" INITIATING...");
	        
	        EnhancedSynchACC acc = this.getContext();
	        
	        TucsonTupleCentreId configTc = new TucsonTupleCentreId("'$ENV'", "localhost", "20504");
	        
	        String config = Utils.fileToString("it/unibo/homemanager/situatedness/fridgeSpec.rsp");
	        
	        System.out.println("Config file: " + config);
	        
	        this.loadConfig(fridgeTc, config);
	        
	        tracePanel.appendText("--******-- FRIDGE SITUATED AGENT FOR "+ name +" CONFIG DONE...");
	        
	        final LogicTuple actuatorTuple = new LogicTuple(
	                "createTransducerActuator",
	                new TupleArgument(fridgeTc.toTerm()), // tuple centre id
	                new Value("it.unibo.homemanager.situatedness.FridgeTransducer"), // java class implementing the transducer
	                        new Value("fridgeTransducer_" + device.getDeviceId()), // transducer id
	                        new Value("it.unibo.homemanager.situatedness.FridgeProbe"), // java class implementing the probe
	                                new Value("fridge" + device.getDeviceId())); // probe id
	        
	        acc.out(configTc, actuatorTuple, null);
	        
	        tracePanel.appendText("--******-- FRIDGE SITUATED AGENT FOR "+ name +" STARTED.");
	       
	        	// TEST
	        	Thread.sleep(5000);
	        	tracePanel.appendText("--******-- FRIDGE SITUATED AGENT FOR "+ name +" try to insert new tupla");
	        	// Test: we send some tuple and after that we take off one
	        	LogicTuple template = LogicTuple.parse("act(" + name + ", banana , 1)");
	            ITucsonOperation op = acc.out(fridgeTc, template,null);
	            if (op.isResultSuccess()) {
	            	tracePanel.appendText("--******-- FRIDGE SITUATED AGENT "+ device.getDeviceName() +": banana tupla inserted correctly.");
	            }
	            Thread.sleep(5000);
	            template = LogicTuple.parse("act(" + name + ", kiwi , 3)");
	            op = acc.out(fridgeTc, template,null);
	            if (op.isResultSuccess()) {
	            	tracePanel.appendText("--******-- FRIDGE SITUATED AGENT "+ device.getDeviceName() +": kiwi tupla inserted correctly.");
	            }
	            Thread.sleep(5000);
	            template = LogicTuple.parse("del(" + name + ", banana , 1)");
	            op = acc.in(fridgeTc, template,Long.MAX_VALUE);
	            if (op.isResultSuccess()) {
	            	tracePanel.appendText("--******-- FRIDGE SITUATED AGENT "+ device.getDeviceName() +": banana tupla deleted correctly.");
	            }
	       
        }
        catch(Exception ex) {
            ex.printStackTrace();
            tracePanel.appendText("FRIDGE SITUATED AGENT "+ device.getDeviceName() +": tupla not inserted correctly.");
        }
    }
    
    protected boolean loadConfig(TucsonTupleCentreId tc, String s) {
    	
    	// Format the device reaction config file correctly 
    	// set device id
    	s = s.replaceAll("#", "" + device.getDeviceId());
    	// format correctly
        s = s.replaceAll("\n", "");
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\.", ".\n");
        
        System.out.println("File formattato:\n" + s + "\n");
        
    	// Recover the other reaction in the TC
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
	        
	        System.out.println("Nuovo file reaction:\n" + s + "\n");
	        
	        acc.setS(tc, s, null);
		} catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;   	

    }
	
}
