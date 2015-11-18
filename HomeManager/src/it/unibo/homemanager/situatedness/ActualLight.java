package it.unibo.homemanager.situatedness;

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

public class ActualLight implements ISimpleProbe{
	
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "20504";
    private EnhancedSynchACC acc;
    private final AbstractProbeId pid;
    private TucsonTupleCentreId illumination_tc;
    private TransducerId tid;
    private TransducerStandardInterface transducer;
    private int lamp_id;
    
    public ActualLight(final AbstractProbeId i) {
    	this.pid = i;
    	this.lamp_id = Integer.parseInt(pid.toString().replace("lamp_", ""));
        try {
            final TucsonAgentId aid = new TucsonAgentId("actuator" + i);
            this.acc = TucsonMetaACC.getContext(aid, ActualLight.DEFAULT_HOST, Integer.valueOf(ActualLight.DEFAULT_PORT));
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        
        try {
			this.illumination_tc = new TucsonTupleCentreId("illumination_tc", "localhost", "20504");
		} catch (TucsonInvalidTupleCentreIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
            System.err.println("[" + this.pid
                    + "]: Non è stato ancora associato nessun traduttore a questo PID.");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Non posso collegarmi al transducer.");
                return false;
            }
        }
        
        
        try {
        	switch (key) {
	        case "get_light_state": final LogicTuple template = LogicTuple.parse("light_curr_st(" + this.lamp_id + ",_)");
						            final ITucsonOperation op = this.acc.rd(this.illumination_tc, template, null);
						            if (op.isResultSuccess()) {
						                final String stato = op.getLogicTupleResult().getArg(1).toString();
						                System.out.println("[" + this.pid + "]: lo stato della lampadina è: " + stato);
						                final int state;
						                if (stato == "off") {
						                	state = 0;
						                } else if (stato == "on") {
						                	state = 1;
						                } else state = 0;
						                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.GET_MODE);
						            }
						            return true;
	        default: System.err.println("[" + this.pid + "]: Proprietà sconosciuta " + key);
            		 return false;
        	}
        	
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        } catch (InvalidLogicTupleException e) {
			e.printStackTrace();
		}
        return false;
		
	}



	@Override
	public boolean writeValue(String key, int state) {
		
		if (this.tid == null) {
            System.err.println("[" + this.pid
                    + "]: Non è stato ancora associato nessun traduttore a questo PID.");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Non posso collegarmi al transducer.");
                return false;
            }
        }
        
        try {
        
	        switch (key) {
		        case "light_mode":  String stato;
		        						if (state == 0) {
								        	stato = "off";
								        } else if (state == 1) {
								        	stato = "on";
								        } else {
								        	System.err.println("[" + this.pid + "]: stato non valido: " + state);
								        	return false;
								        }
		        						System.out.println("[" + this.pid + "]: devo impostare il valore della luce a " + stato);
		        						final LogicTuple template = LogicTuple.parse("light_curr_st(" + lamp_id + ",_)");
							            final ITucsonOperation op = this.acc.inp(this.illumination_tc, template, null); //TODO: verificare se è meglio con la in
							            //TODO: dovrebbe verificare se la luce è già accesa o meno
							            if (op.isResultSuccess()) {
							                final LogicTuple nuovaTupla = LogicTuple.parse("light_curr_st(" + lamp_id + "," + stato  + ")");
							                this.acc.out(this.illumination_tc, nuovaTupla, null);
							                System.out.println("[" + this.pid + "]: stato della luce impostato a " + stato);
							                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.SET_MODE);
							                return true;
							            }
							            System.err.println("[" + this.pid + "]: Errore generico.");
							            return false;
		        default: System.err.println("[" + this.pid + "]: Proprietà sconosciuta " + key);
	            		 return false;
	        }
                	
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return false;
	}

}
