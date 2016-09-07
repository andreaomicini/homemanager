package it.unibo.homemanager.scheduler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JTextField;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.tablemap.Device;
import it.unibo.homemanager.tablemap.Room;
import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.tablemap.ServicesInterfaces.DeviceServiceInterface;
import it.unibo.homemanager.tablemap.ServicesInterfaces.RoomServiceInterface;
import it.unibo.homemanager.util.Utilities;

public class RapidScheduler implements ScheduleInterface {

	private User usr; //per inviare i comandi, verranno utilizzate le autorizzazioni di usr
	private String name; //nome rappresentativo dello schedule
	private ServiceFactory sf;
    private RoomServiceInterface rs;
    private DeviceServiceInterface ds;
	private Database db;
	private JTextField textF;
	private Vector tid;
	
	public RapidScheduler(ServiceFactory sf, Database db, JTextField textF, Vector tuple) {
		this.sf=sf;
		this.db=db;
        this.textF=textF;
        this.tid=tuple;
        name = "Rapid Scheduler";
		
		//ottengo le interfacce
		rs = sf.getRoomServiceInterface();
		ds = sf.getDeviceServiceInterface();
	}

	public void setUser(User u) {
		usr = u;
	}
	
	public String getName() {
		return name;
	}

	public void launch() throws Exception { //codice riempitivo. Esegue le stesse cose di RandomScheduler
		ArrayList<Device> devs;
		String[] functions;
		Room room;
		Device device;
		String function;
		
		Random randomGen;
		int devNumber, funcNumber;
		int runningTime = 15000;//quanto tempo tengo attiva una funzione prima di sospenderla (in ms)
		
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
	    System.err.println("***INVIO COMANDO RAPID: Stanza "+room.name+", Disp "+device.name+", Funz "+function);
			
	    //pausa tra avvio comando e sua terminazione
	    Thread.sleep(runningTime);
	        
		sendCmd(room, device, "off", textF); //invia STOP
		System.err.println("***INVIO COMANDO STOP RAPID: Stanza "+room.name+", Disp "+device.name+", Funz "+"off");	
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
}
