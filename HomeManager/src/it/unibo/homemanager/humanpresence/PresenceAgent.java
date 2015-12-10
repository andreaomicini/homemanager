package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.opencv.core.Core;

import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.meteo.TucsonAgentInterface;
import it.unibo.homemanager.tablemap.Sensor;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
/**
 * @author 0000628533
 *
 */
public class PresenceAgent extends AbstractTucsonAgent implements TucsonAgentInterface {
	private TucsonTupleCentreId myTid;
	private TucsonTupleCentreId roomTupleCentreId;
    private TracePanel tp;
    private EnhancedSynchACC acc;
    private ViewPresenceAgentPanel panel;
    private Database db;
    
    private int idRoom;
    private HumanDetectorVideoInterface humanDetector;
    private boolean humanPresent;
    private List<Sensor> sensors;
    
    private int sleepAmount=5000;
    
    public PresenceAgent(String id,TracePanel tp, TucsonTupleCentreId tId, Database database,int idRoom)
    		throws TucsonInvalidAgentIdException
    {
    	super(id);
   		this.tp=tp;
  		this.myTid=tId;
  		this.db=database;
   		this.idRoom=idRoom;
   		this.humanPresent=false;
   		this.sensors=new ArrayList<Sensor>();
    }

    public void setPanel(ViewManageAgentPanel maPanel)
    {
    	panel=new ViewPresenceAgentPanel(maPanel, this, tp);
    }
    
	@Override
	public void show() {
		panel.init();	
	}

	@Override
	public void hide()
	{
		panel.hidePanel();
	}

	@Override
	public JPanel getInterface() {
		return panel;
	}

	@Override
	public String getName() {
		return "Presence Agent "+idRoom;
	}
	
	public boolean isHumanPresent()
	{
		return this.humanPresent;
	}
	//Inizializza i sensori e gli humanDetector
	private void init() throws Exception
	{
    	int NUM = ServiceFactory.TUCSON;
        ServiceFactory sf = ServiceFactory.getServiceFactory(NUM);
        String RoomName= sf.getRoomServiceInterface().getRoomById(db, idRoom).name;
        Sensor cameraSensor = sf.getSensorServiceInterface().getSensor(db, "CAMERA_"+RoomName+"_1", idRoom);
        if (cameraSensor==null)
        	cameraSensor = sf.getSensorServiceInterface().insertSensor(db, "CAMERA_"+RoomName+"_1",idRoom);
        if (idRoom==3)
        	sleepAmount+=60000;
		sensors.add(cameraSensor);
		humanDetector = HumanDetectorFactory.createDifferential();
       	humanDetector.init();
		//test
       	this.roomTupleCentreId= new TucsonTupleCentreId("camera_tc", "localhost", "20504");
       	
   	}
	/*
	 * In questa implementazione, l'agente si limita a:
	 * 1) Processare frame tramite human detector
	 * 2) Ottenere il frame processato per aggiornare la view
	 * 3) Aggiornare una tupla in caso cambi lo stato della stanza
	 * 
	 */
	@Override
	protected void main() {
		tp.appendText("-- PRESENCE AGENT STARTED.");
        this.acc = this.getContext();
        
    	BufferedImage result= null;
    	boolean updatePresent=false;
    	LogicTuple t1;     
        try{
        	reset();
        	init();
        	Thread.sleep(sleepAmount);
        	
            while(true)
            {
            	humanDetector.processFrame();
            	if(humanDetector.getCurrentProcessedFrame()==null)
            		break;
            	result=MovementDetectionUtilities.resize(humanDetector.getCurrentProcessedFrame(), 480, 360);
            	updatePresent=humanDetector.isHumanPresent();
            	this.panel.setImage(result);
            	if (updatePresent != humanPresent)
            	{
            		//devo aggiornare il valore nel tupleCenter
            		Date data=new Date();
                	Timestamp t=new Timestamp(data.getTime());
                	int roomState=0;
                	if(updatePresent)
                		roomState=1;
                	//tuple nel formato: 
                	t1= new LogicTuple("presence",
                			new Value(roomState), //intero, 1 se viene rilevato un umano, 0 altrimenti
                			new Value(idRoom),	// id della stanza monitorata dall'agente
                			new Value(sensors.get(0).idSens), //id del sensore che ha effettuato il rilevamento
                			new Value(t.toString()));  //timestamp
                	//test 
                	/*
                	LogicTuple detection = new LogicTuple("detection",
                            new Value("entry"),
                            new Value(idSensor), // sensor
                            new Value(-10), // person
                            new Value(idRoom)// room
                    );
                	acc.out(this.roomTupleCentreId,detection,null);
                	*/
                	//end test
                	acc.out(myTid, t1, null);
                	
                	tp.appendText("-- PRESENCE AGENT UPDATED.");
                	humanPresent=updatePresent;
            	}
            	
             }
            
            tp.appendText("--PRESENCE AGENT: STREAM ENDED.");
            this.panel.resetImage();
            
          }catch(TucsonOperationNotPossibleException ex){
              ex.printStackTrace();
              tp.appendText("PresenceAgent : error in activity.");
          }
          catch(UnreachableNodeException ex){
              ex.printStackTrace();
              tp.appendText("PresenceAgent : error in activity.");
          }
          catch(OperationTimeOutException ex){
              ex.printStackTrace();
              tp.appendText("PresenceAgent : error in activity.");
          }
          catch(Exception ex)
          {
        	  ex.printStackTrace();
              tp.appendText("PresenceAgent : unknown error in activity.");
          }
	}
	
	private void reset()
			throws OperationTimeOutException,UnreachableNodeException,TucsonOperationNotPossibleException
	{
		LogicTuple template=new LogicTuple("presence", new Var(), new Var(),new Var());
    	acc.inAll(myTid, template, Long.MAX_VALUE);
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		 throw new UnsupportedOperationException("Not supported yet.");
		
	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {
		 throw new UnsupportedOperationException("Not supported yet.");
		
	}
}
