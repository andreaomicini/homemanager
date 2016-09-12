package it.unibo.homemanager.scheduler;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTextField;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.tablemap.ServicesInterfaces.DeviceServiceInterface;
import it.unibo.homemanager.tablemap.ServicesInterfaces.RoomServiceInterface;

public abstract class AbstractScheduler {

	protected ServiceFactory sf;
	protected Database db;
	protected JTextField textF;
	protected Vector tid;
	protected String name; //name of the schedule
	protected RoomServiceInterface rs;
	protected User usr; //used user's authorization for sending commands
	protected ArrayList<ScheduledDevice> allSchDevs; //all devices with their schedule state

	public AbstractScheduler(ServiceFactory sf, String name,
							ArrayList<ScheduledDevice> schDevs, JTextField textF, Vector tuple) throws Exception {
		this.sf=sf;
        this.textF=textF;
        this.tid=tuple;
        this.name = name;
        this.allSchDevs = schDevs;
        this.db = sf.getDatabaseInterface();
        this.rs = sf.getRoomServiceInterface();
	}

	public void setUser(User u){
		usr = u;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract void launch() throws Exception;
	
}
