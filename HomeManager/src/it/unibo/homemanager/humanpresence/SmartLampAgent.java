package it.unibo.homemanager.humanpresence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;

import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.meteo.TucsonAgentInterface;
import it.unibo.homemanager.tablemap.Lamp;
import it.unibo.homemanager.tablemap.Room;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.tucson.api.*;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * @author 0000691363
 *
 */

public class SmartLampAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	private TucsonTupleCentreId myTid;
    private TracePanel tp;
    private EnhancedSynchACC acc;
    private Database db;
    private ViewSmartLampPanel panel;
    private List<LampMap> lamps;
    private LampSwitchInterface lampswitch;
    
    
    //SMARTLMAPAGENT
    public SmartLampAgent(String id,TracePanel tp, TucsonTupleCentreId tId, Database database)
    		throws TucsonInvalidAgentIdException
    {
    	super(id);
   		this.tp=tp;
  		this.myTid=tId;
  		this.db=database;
   		lamps = new ArrayList<LampMap>();
   		this.lampswitch = new LampSwitch();
   		
   		
   		int NUM = ServiceFactory.TUCSON;
        ServiceFactory sf = ServiceFactory.getServiceFactory(NUM);
        Vector<Lamp> lampade=null;
        Vector<Room> rooms=null;
		try {
			lampade = sf.getLampServiceInterface().getLamps(db);
			rooms = sf.getRoomServiceInterface().getRooms(db);
		} catch (Exception e) {
			tp.appendText(e.toString());
		}      
        for (int i=0; i< rooms.size(); i++){
        	lamps.add(new LampMap(lampade.elementAt(i).idL, rooms.elementAt(i).idRoom,lampswitch.getPin()));
        }                 
    }
    
    public List<LampMap> getLamps(){
    	return this.lamps;
    }
    
    public void setPanel(ViewManageAgentPanel maPanel)
    {
    	panel=new ViewSmartLampPanel(maPanel, this, tp);
    }

	@Override
	public void show() {
		panel.init();
	}

	@Override
	public void hide() {
		panel.hidePanel();
	}

	@Override
	public JPanel getInterface() {
		return panel;
	}

	@Override
	public String getName() {
		return "SmartLampAgent";
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected void main() {
		tp.appendText("-- SMART LAMP AGENT STARTED.");
    	this.acc = this.getContext();
		LogicTuple t1=null;
        try{      
        	Thread.sleep(50);
        	// TUPLA RESTITUITA DAP PRESENCE AGENT
//        	t1= new LogicTuple("presence",
//        			new Value(1), 				//intero, 1 se viene rilevato un umano, 0 altrimenti
//        			new Value(idRoom),					// id della stanza monitorata dall'agente
//        			new Value(sensors.get(0).idSens), 	//id del sensore che ha effettuato il rilevamento
//        			new Value(""));
            while(true)
            {
            	Thread.sleep(5000);            	
            	t1 = new LogicTuple("presence",new Var("S"),new Var("R"),new Var("X"),new Var("T"));           	
            	ITucsonOperation operation;  
            	operation = acc.in(myTid, t1, Long.MAX_VALUE);
            	while (operation.isResultSuccess()){  
            		Thread.sleep(1000);
                    if (operation.isResultSuccess()){
                    	LogicTuple lt = operation.getLogicTupleResult();
                    	//Qui posso inserire un algoritmo piu complesso di previsione di accensione!
		                int presenza =  lt.getArg(0).intValue();
		                int idRoom = lt.getArg(1).intValue();
		                tp.appendText("SMARTLAMPAGENT  presenza: " + presenza + " - room: " + idRoom);
		                this.panel.resetImage(idRoom,presenza);
		                if (presenza==1){
		                	lightOn(findPinByIdRoom(idRoom));
		                }else{
		                	lightOff(findPinByIdRoom(idRoom));
		                }
		                tp.appendText("-- SMART LAMP AGENT UPDATE");
                    }else{
                    	tp.appendText("-- SMART LAMP AGENT : error during operation.");
                    } 
                    operation = acc.in(myTid, t1, Long.MAX_VALUE);
            	}            	               
            	tp.appendText("--SMART LAMP AGENT: END");
            } 
            
          }catch(TucsonOperationNotPossibleException ex){
              ex.printStackTrace();
              tp.appendText("SMART LAMP AGENT : error in activity.");
          }
          catch(UnreachableNodeException ex){
              ex.printStackTrace();
              tp.appendText("SMART LAMP AGENT : error in activity.");
          }
          catch(OperationTimeOutException ex){
              ex.printStackTrace();
              tp.appendText("SMART LAMP AGENT : error in activity.");
          }
          catch(Exception ex)
          {
        	  ex.printStackTrace();
              tp.appendText("SMART LAMP AGENT : unknown error in activity.");
          }
	}
	
	public int findPinByIdRoom(int idRoom){
		for (LampMap temp : lamps){
			if (temp.getIdRoom()==idRoom){
				return temp.getPin();
			}
		}
		return -1;
	}
	
	//METODI PER LE LUCI
	public void lightOn(int number) {
		lampswitch.lightOn(number);		
	}

	public void lightOff(int number) {
		lampswitch.lightOff(number);		
	}

	public void lightSwitch(int number) {
		lampswitch.lightSwitch(number);	
	}
	
	//Classe di mapping fra le lamp e le relative rooms e pin del raspberry!
	public class LampMap{
		private int idLamp;
		private int idRoom;
		private int pin;
		
		public LampMap(int lamp, int room, int pin) {
			super();
			this.idLamp = lamp;
			this.idRoom = room;
			this.pin = pin;
		}
		
		public int getIdLamp() {
			return idLamp;
		}

		public void setLamp(int lamp) {
			this.idLamp = lamp;
		}

		public int getIdRoom() {
			return idRoom;
		}

		public void setRoom(int room) {
			this.idRoom = room;
		}

		public int getPin() {
			return pin;
		}

		public void setPin(int pin) {
			this.pin = pin;
		}		
	}
}
