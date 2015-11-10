package it.unibo.homemanager.humanpresence;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.dbmanagement.TucsonDatabase;
import it.unibo.homemanager.meteo.TucsonAgentInterface;
import it.unibo.homemanager.tablemap.User;
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
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuples.javatuples.api.IJTupleTemplate;
import alice.tuples.javatuples.exceptions.InvalidJValException;
import alice.tuples.javatuples.impl.JTupleTemplate;
import alice.tuples.javatuples.impl.JVal;

public class PresenceAgent extends AbstractTucsonAgent implements TucsonAgentInterface {
	private TucsonTupleCentreId myTid;
    private TracePanel tp;
    private EnhancedSynchACC acc;
    private ViewPresenceAgentPanel panel;
    private Database db;
    
    public PresenceAgent(String id,TracePanel tp, TucsonTupleCentreId tuple, Database database) throws TucsonInvalidAgentIdException {
        super(id);
        this.tp=tp;
        this.myTid=tuple;
        this.db=database;
    }

    public void setPanel(ViewManageAgentPanel maPanel)
    {
    	panel=new ViewPresenceAgentPanel(maPanel, this, tp);
    }
    private void inizializzaCampi() {
		//TODO
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
	

	@Override
	protected void main() {
		tp.appendText("-- PRESENCE AGENT STARTED.");
        this.acc = this.getContext();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        LogicTuple t1,tdate;
        inizializzaCampi();
        try{
            while(true) {
            	Date data=new Date();
            	Timestamp t=new Timestamp(data.getTime());
            	tdate=new LogicTuple("presence",new Value(t.toString()));
            	t1= new LogicTuple("presence",new Value("IN"),new Value("living_room"));        	
            	acc.out(this.myTid, tdate, null);
            	acc.out(myTid, t1, null);
            	       	
            	tp.appendText("-- PRESENCE AGENT UPDATED.");
            	Thread.sleep(100000);
            	
            	reset();
               }
            
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
              tp.appendText("PresenceAgent : error in activity.");
          }
	}
	
	private void reset() throws OperationTimeOutException,UnreachableNodeException,TucsonOperationNotPossibleException
	{
		LogicTuple template=new LogicTuple("presence", new Var(), new Var());
    	acc.inAll(myTid, template, Long.MAX_VALUE);
    	template=new LogicTuple("presence", new Var());
    	acc.inAll(myTid,template,Long.MAX_VALUE);
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
