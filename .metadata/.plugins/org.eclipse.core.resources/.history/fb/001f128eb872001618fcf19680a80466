package it.unibo.homemanager.scheduler;

import it.unibo.homemanager.tablemap.Device;

public class ScheduledDevice {

	private Device device;
	private String scheduleState;
	//può essere "SCHEDULED", "NOT SCHEDULED" o "RUNNING"
	private String program;
	//questa variabile ha significato solo quando lo stato è impostato a SCHEDULED o RUNNING
	
	
	public ScheduledDevice(Device d) {
		this(d, "NOT SCHEDULED");
	}
	
	public ScheduledDevice(Device d, String ss) {
		this(d, ss, "");
	}
	
	public ScheduledDevice(Device d, String ss, String prog) {
		this.device = d;
		setScheduleState(ss);
		this.program = prog;
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
			throw new IllegalArgumentException();//parametro errato
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
}
