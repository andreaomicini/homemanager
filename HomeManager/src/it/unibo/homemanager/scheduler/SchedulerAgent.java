package it.unibo.homemanager.scheduler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JTextField;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.meteo.TucsonAgentInterface;
import it.unibo.homemanager.tablemap.*;
import it.unibo.homemanager.tablemap.ServicesInterfaces.DeviceServiceInterface;
import it.unibo.homemanager.tablemap.ServicesInterfaces.RoomServiceInterface;
import it.unibo.homemanager.tablemap.TucsonService.TucsonRoomService;
import it.unibo.homemanager.userinterfaces.IssueCmdPanel;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

import it.unibo.homemanager.util.Utilities;

public class SchedulerAgent extends AbstractTucsonAgent {

	public final Object lock = new Object();
	public volatile boolean paused = true;
	
	private AbstractScheduler absScheduler;
	
	public SchedulerAgent(String id) throws TucsonInvalidAgentIdException {
        super(id);
    }
	
	@Override
	protected void main() {
		try{   
			while(true){	
				synchronized(lock){
					while (paused)
						lock.wait();
				}
				absScheduler.launch();
				Thread.sleep(20000);//sleep between a schedule and the next one
			}
		}
		catch(InterruptedException e){
			System.err.println("Error");
		}
        catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public void setSchedule(AbstractScheduler s)
	{
		absScheduler = s;
	}
	
	public AbstractScheduler getSchedule()
	{
		return absScheduler;
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation atco) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void operationCompleted(ITucsonOperation ito) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
