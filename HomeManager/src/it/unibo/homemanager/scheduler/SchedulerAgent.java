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

	private User usr; //per inviare i comandi, verranno utilizzate le autorizzazioni di usr
	private Vector tid;
	private ServiceFactory sf;
    private RoomServiceInterface rs;
    private DeviceServiceInterface ds;
	private Database db;
	private JTextField textF;
	
	public final Object lock = new Object();
	public volatile boolean paused = true;
	
	public SchedulerAgent(String id, Vector tuple, ServiceFactory sf, Database db, JTextField textF) throws TucsonInvalidAgentIdException {
        super(id);
        this.tid=tuple;
        this.sf=sf;
        this.db=db;
        this.textF=textF;
    }
	
	
	@Override
	protected void main() {
		int runningTime=15000;//quanto tempo tengo attiva una funzione prima di sospenderla (in ms)
		//ottengo le interfacce
		rs = sf.getRoomServiceInterface();
        ds = sf.getDeviceServiceInterface();
		
		try
        {   
			while(true)
			{	
				synchronized(lock)
				{
					while (paused)
					{
						lock.wait();
					}
				}
				
				//codice di prova: avvio random di un comando per 'runningTime' millisecondi e terminazione
				randomCmd(runningTime);
				Thread.sleep(20000);
			}
		}
		catch(InterruptedException e)
		{
			System.err.println("Errore");
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	private void randomCmd(int runningTime) throws Exception
	{
		ArrayList<Device> devs;
		String[] functions;
		Room room;
		Device device;
		String function;
		
		Random randomGen;
		int devNumber, funcNumber;
		
		devs = ds.getDevices(db);
		//rimuovo TV Plasma Panasonic. Utente grace non riesce ad accenderlo
		devs.remove(2);

		randomGen = new Random();
		
		devNumber = randomGen.nextInt(devs.size());
	    device = devs.get(devNumber);
	    	
	    room = rs.getRoomById(db, device.roomId);
	    	
	    //funzione random del dispositivo. (se trovata OFF, ripeto la ricerca)
	    while(true)
	    {
	       	functions = Utilities.getDevCmds(device.param);
	       	funcNumber = randomGen.nextInt(functions.length);
	       	function = functions[funcNumber];
	        	
	       	if(!function.equalsIgnoreCase("off"))
	       		break;
	       }
			
	    sendCmd(room, device, function, textF); //invia AVVIO
	    System.err.println("***INVIO COMANDO: Stanza "+room.name+", Disp "+device.name+", Funz "+function);
			
	    //pausa tra avvio comando e sua terminazione
	    Thread.sleep(runningTime);
	        
		sendCmd(room, device, "off", textF); //invia STOP
		System.err.println("***INVIO COMANDO STOP: Stanza "+room.name+", Disp "+device.name+", Funz "+"off");	
	}
	
	public void setUser(User u)
	{
		usr = u;
	}
	
	private void sendCmd(Room r, Device d, String function, JTextField textF) {
        try {
            //TucsonContext ctx = Tucson.enterDefaultContext();
            TucsonAgentId agent = new TucsonAgentId("sendCmdAgent");
            EnhancedSynchACC acc = TucsonMetaACC.getContext(agent);
            
            LogicTuple cmd = new LogicTuple("send_cmd",new Value(this.usr.idUser),new Value(r.idRoom),
                new Value(d.idDev),new Value(function));
            
            acc.out((TucsonTupleCentreId)tid.elementAt(r.idRoom-1), cmd, null);
           // ctx.out((TupleCentreId)tid.elementAt(r.idRoom-1),cmd);
            Utilities.insertTupleInto((TucsonTupleCentreId)tid.elementAt(r.idRoom-1), cmd);
            System.err.println("CMD SENT: "+textF.getText()+" / "+cmd);
            acc.exit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
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
