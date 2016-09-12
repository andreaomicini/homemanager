package it.unibo.homemanager.scheduler;

import java.util.Vector;

import it.unibo.homemanager.tablemap.Device;
import it.unibo.homemanager.tablemap.Room;

public class ScheduledDevice {
	private Room room;
	private Device device;
	private String scheduleState;  //it can be: "SCHEDULED", "NOT SCHEDULED" or "RUNNING"
	private String program;        //it's used only when the schedule state is SCHEDULED or RUNNING
	private RunningThread rThread; //it keeps track the execution of a scheduled program
	
	public ScheduledDevice(Room r, Device d, Vector tid) {
		this(r, d, "NOT SCHEDULED", tid);
	}
	
	public ScheduledDevice(Room r, Device d, String ss, Vector tid) {
		this(r, d, ss, "", tid);
	}
	
	public ScheduledDevice(Room r, Device d, String ss, String prog, Vector tid) {
		this.room = r;
		this.device = d;
		setScheduleState(ss);
		this.program = prog;
		rThread = new RunningThread(r, this, tid);
	}
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getScheduleState() {
		return scheduleState;
	}

	public void setScheduleState(String scheduleState) {
		if (scheduleState.equals("SCHEDULED") || scheduleState.equals("NOT SCHEDULED")
				 || scheduleState.equals("RUNNING"))
			this.scheduleState = scheduleState;
		else
			throw new IllegalArgumentException();
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
	public RunningThread getrThread() {
		return rThread;
	}

	public void setrThread(RunningThread rThread) {
		this.rThread = rThread;
	}
}
