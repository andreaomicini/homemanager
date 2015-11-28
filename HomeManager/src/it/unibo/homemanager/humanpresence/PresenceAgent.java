package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.meteo.TucsonAgentInterface;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
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
    private TracePanel tp;
    private EnhancedSynchACC acc;
    private ViewPresenceAgentPanel panel;
    private Database db;
    
    private String room;
    private HumanDetectorInterface humanDetector;
    private boolean humanPresent;
    
    public PresenceAgent(String id,TracePanel tp, TucsonTupleCentreId tuple, Database database,String room)
    		throws TucsonInvalidAgentIdException
    {
    	super(id);
   		this.tp=tp;
  		this.myTid=tuple;
  		this.db=database;
   		this.room=room;
   		this.humanPresent=false;
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

	@Override
	protected void main() {
		tp.appendText("-- PRESENCE AGENT STARTED.");
        this.acc = this.getContext();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
    	Mat frame= new Mat();
    	BufferedImage result= null;
    	boolean updatePresent=false;
    	LogicTuple t1;
    	  
    	Mat firstFrame=new Mat();
      
        try{
        	reset();
        	Thread.sleep(5000);
        	
        	VideoCapture camera = new VideoCapture(
    				"C:/Users/Ameli/Documents/homemanager/HomeManager/src/testvideo2b.mp4");
        	if (!camera.isOpened())
    			System.out.println("Error: camera not working");
        	camera.read(firstFrame);
        	humanDetector = HumanDetectorFactory.createDifferential(firstFrame);
        	
            while(camera.isOpened())
            {
            	/*
            	 * Per gli obiettivi di questa tesi, l'implementazione attuale si basa su queste scelte ulteriori:
            	 * 1)L'agente aggiorna il feed nella view (che su un raspberryPi non sarebbe presente)
            	 * 2)L'agente non considera la stanza vuota se l'ultimo rilevamento era nel lato destro della stanza
            	 * 3)La view aggiorna una label tramite l'agente, non tucson
            	 */
            	camera.read(frame);
            	if (frame.empty())
            		break;
            	humanDetector.processFrame(frame);
            	result=MovementDetectionUtilities.resize(humanDetector.getCurrentProcessedFrame(), 480, 360);
            	updatePresent=humanDetector.isHumanPresent();
            	this.panel.setImage(result);
            	if (updatePresent != humanPresent)
            	{
            		//devo aggiornare il valore nel tupleCenter
            		Date data=new Date();
                	Timestamp t=new Timestamp(data.getTime());
                	String roomState="NO";
                	if(updatePresent)
                		roomState="YES";
                	//tuple nel formato: (YES/NO human detected,stanza analizzata,timestamp dell'ultimo update)
                	t1= new LogicTuple("presence",new Value(roomState),new Value(room),new Value(t.toString()));        	
                	acc.out(myTid, t1, null);
                	tp.appendText("-- PRESENCE AGENT UPDATED.");
                	humanPresent=updatePresent;
            	}
            	
             }
            camera.release();
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
