package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

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
		return "Presence Agent";
	}
	
	public boolean isHumanPresent()
	{
		return this.humanPresent;
	}

	private void init() throws Exception
	{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
    	int NUM = ServiceFactory.TUCSON;
        ServiceFactory sf = ServiceFactory.getServiceFactory(NUM);
        Sensor cameraSensor = sf.getSensorServiceInterface().getSensor(db, "CAMERASTANZA1", 2);
        if (cameraSensor==null)
        	cameraSensor = sf.getSensorServiceInterface().insertSensor(db, "CAMERASTANZA1",2);
		sensors.add(cameraSensor);
		//test
       	this.roomTupleCentreId= new TucsonTupleCentreId("camera_tc", "localhost", "20504");
       	humanDetector = HumanDetectorFactory.createDifferential();
       	humanDetector.init();
   	}
	@Override
	protected void main() {
		tp.appendText("-- PRESENCE AGENT STARTED.");
        this.acc = this.getContext();
        
    	BufferedImage result= null;
    	boolean updatePresent=false;
    	LogicTuple t1;     
        try{
        	reset();
        	Thread.sleep(5000);
        	init();
            while(true)
            {
            	/*
            	 * Per gli obiettivi di questa tesi, l'implementazione attuale si basa su queste scelte ulteriori:
            	 * 1)L'agente aggiorna il feed nella view (che su un raspberryPi non sarebbe presente)
            	 * 2)Il detector non considera la stanza vuota se l'ultimo rilevamento era nel lato destro della stanza
            	 */
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
                	//tuple nel formato: (1 yes/0 no human detected,id stanza analizzata,id sensore,timestamp dell'ultimo update)
                	t1= new LogicTuple("presence",new Value(roomState),new Value(idRoom),new Value(sensors.get(0).idSens),new Value(t.toString()));  
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
