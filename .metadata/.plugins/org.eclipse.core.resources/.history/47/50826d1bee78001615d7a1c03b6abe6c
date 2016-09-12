package it.unibo.homemanager.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextArea;

import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.tablemap.Device;
import it.unibo.homemanager.tablemap.ServicesInterfaces.DeviceServiceInterface;

public class RefreshThread extends Thread {
	private ArrayList<ScheduledDevice> schDevs;
	private JTextArea taDev;
	private JTextArea taSch;
	private int refreshTime;
	
	private TucsonAgentId agent;
	private Vector tid;
	private DeviceServiceInterface deviceService;
	private Database database;
	
	public RefreshThread(Vector t, ServiceFactory sf, ArrayList<ScheduledDevice> schDevs, int refreshTime, JTextArea taDev, JTextArea taSch) {
		this.schDevs = schDevs;
		this.taDev = taDev;
		this.taSch = taSch;
		this.refreshTime = refreshTime;
		try {
			agent = new TucsonAgentId("refreshThreadAgent");
			this.database = sf.getDatabaseInterface();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tid = t;
		this.deviceService = sf.getDeviceServiceInterface();
		
	}

	public void run() {
		try {
			while(true) {
				readDevsAndSchedule();				//reading device and schedule state + writing in the TextArea
				RefreshThread.sleep(refreshTime);   //time of refresh
			}
		}
		catch (Exception e) {//someone has closed ViewStateFrame
							 //ignore the exception?
			e.printStackTrace();
		}
	}
	
	private void readDevsAndSchedule() throws InvalidLogicTupleException
	{
		readDevicesStatus();
		readScheduledDevices();
	}
	
	private void readDevicesStatus() throws InvalidLogicTupleException
	{
		taDev.setText("");
        
        List<LogicTuple> l = getDevStates();
        for(int i=0;i<l.size();i++) {
            for(int j=0;j<((List)l.get(i)).size();j++) {
            	LogicTuple lt = LogicTuple.parse(((List)l.get(i)).get(j).toString());
            	taDev.append(getDevString(lt)+(i==l.size()-1?"":"\n"));
            }
        }
	}
	
	private Vector getDevStates() {
        List<LogicTuple> l = null;
        Vector dev = new Vector();
        try {
            EnhancedSynchACC acc = TucsonMetaACC.getContext(agent);
            for(int i=0; i<tid.size();i++)
            {
                ITucsonOperation op_rdAll = acc.rdAll((TucsonTupleCentreId)tid.elementAt(i),
                        new LogicTuple("dev_curr_st",
                                new Var("Y"),
                                new Var("Z")),
                        null);
                l = op_rdAll.getLogicTupleListResult();
                dev.add(l);
            }
            acc.exit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return dev;
    }
	
	private String getDevString(LogicTuple lt) {
        String str = "";
        try {
            Device d = deviceService.getDeviceById(database.getDatabase(),lt.getArg(0).intValue());
            str += d.name+" \t currently "+lt.getArg(1).toString();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }
	
	private void readScheduledDevices()
	{
		String name;
		String schedule;
		String program;
		
		String format;//used for displaying in columns the output
		String line;
		
		taSch.setText("");
		
		for(int i=0; i<schDevs.size(); i++)
		{   
			name = schDevs.get(i).getDevice().name;
			schedule = schDevs.get(i).getScheduleState();
			program = schDevs.get(i).getProgram();
			
			if(schedule.equals("NOT SCHEDULED"))
			{
				format = "%1$-37s %2$-15s";
				line = String.format(format, name, schedule);
			}
			else
			{
				format = "%1$-37s %2$-15s %3$-7s";
				line = String.format(format, name, schedule, program);
			}
			
			taSch.append(line+"\n");
		}
	}

}
