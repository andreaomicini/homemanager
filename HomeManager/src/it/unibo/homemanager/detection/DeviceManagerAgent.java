package it.unibo.homemanager.detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.communication.AgentCommunicationLanguage;

public class DeviceManagerAgent extends AbstractTucsonAgent {
	private static DeviceManagerAgent deviceManagerAgent;
	
	private TucsonTupleCentreId deviceManagerTupleCentre;
	private EnhancedSynchACC acc;
	private List<Device> devices;
	
	private List<DeviceListener> listeners;
	
	private DeviceManagerAgent() throws Exception {
		super("deviceManagerAgent");
		
		deviceManagerTupleCentre = new TucsonTupleCentreId("device_manager_tc", "localhost", "20504");
		devices = new ArrayList<Device>();
		
		listeners = new ArrayList<DeviceListener>();
		
		init();
	}
	
	private void init() throws Exception {
		File file = new File("devices.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = "";
		
		while ( (line = reader.readLine()) != null) {
			LogicTuple logicTuple = LogicTuple.parse(line);
			Device device = createDeviceByLogicTuple(logicTuple);
			getDevices().add(device);
			onNewDevice(device);
		}
		reader.close();
	}
	
	public TucsonTupleCentreId getDeviceManagerTupleCentre() {
		return (deviceManagerTupleCentre);
	}

	public EnhancedSynchACC getAcc() {
		return (acc);
	}
	
	public List<Device> getDevices() {
		return (devices);
	}
	
	public List<DeviceListener> getListeners() {
		return (listeners);
	}
	
	public void addListener(DeviceListener deviceListener) {
		getListeners().add(deviceListener);
	}
	
	public void removeListener(DeviceListener deviceListener) {
		getListeners().remove(deviceListener);
	}

	public static DeviceManagerAgent getInstance() throws Exception {
		if ( deviceManagerAgent == null )
			deviceManagerAgent = new DeviceManagerAgent();
		return (deviceManagerAgent);
	}

	@Override
	protected void main() {
		acc = getContext();
		
		int first = 0;
		int second = 1;
		int third = 2;
		
		try {
			if ( initTupleCentre() ) {
				while (true) {
					LogicTuple deviceRequest = deviceRequest();
					if ( deviceRequest != null ) {
						String deviceType = deviceRequest.getArg(second).toString();
						int watt = deviceRequest.getArg(third).getArg(first).intValue();
						managerAnswer(deviceType, watt);
					}
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean initTupleCentre() throws Exception {
		for (Device device : getDevices()) {
			LogicTuple logicTuple = createLogicTupleByDevice(device);
			ITucsonOperation operation = getAcc().out(getDeviceManagerTupleCentre(), logicTuple, Long.MAX_VALUE);
			if ( operation.isResultFailure() )
				return (false);
		}
		return (true);
	}
	
	private LogicTuple createLogicTupleByDevice(Device device) throws Exception {
		String deviceName = device.getDeviceName() + "(" + device.getDeviceId() + ")";
		String deviceInfo = AgentCommunicationLanguage.getInfoDevice() + "(" + device.getDeviceEnergy() + "," + device.getDeviceParameters() + "," + device.getDeviceRoomId() + ")";
		String deviceType = device.getDeviceType();
		String tuple = AgentCommunicationLanguage.getDevice() + "(" + deviceName + "," + deviceInfo + "," + deviceType + ")";
		LogicTuple logicTuple = LogicTuple.parse(tuple);
		return (logicTuple);
	}
	
	private Device createDeviceByLogicTuple(LogicTuple logicTuple) {
		int first = 0;
		int second = 1;
		int third = 2;
		
		TupleArgument argumentName = logicTuple.getArg(first);
		TupleArgument argumentInfo = logicTuple.getArg(second);
		TupleArgument argumentType = logicTuple.getArg(third);
		
		int deviceId = argumentName.getArg(first).intValue();
		String deviceName = argumentName.getName();
		float deviceEnergy = argumentInfo.getArg(first).floatValue();
		String deviceType = argumentType.getName();
		String deviceParameters = argumentInfo.getArg(second).toString();
		int deviceRoomId = argumentInfo.getArg(third).intValue();
		
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceName(deviceName);
		device.setDeviceEnergy(deviceEnergy);
		device.setDeviceType(deviceType);
		device.setDeviceParameters(deviceParameters);
		device.setDeviceRoomId(deviceRoomId);
		return (device);
	}

	private LogicTuple deviceRequest() throws Exception {
		String tuple = AgentCommunicationLanguage.getNewName() + "(unknown,A,B)";
		LogicTuple logicTuple = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getDeviceManagerTupleCentre(), logicTuple, Long.MAX_VALUE);
		if ( operation.isResultFailure() )
			return (null);
		return (operation.getLogicTupleResult());
	}
	
	private void managerAnswer(String deviceType, int watt) throws Exception {
		int index = 0;
		for (Device device : getDevices())
			if ( (device.getDeviceName().equals(deviceType)) && (device.getDeviceId() > index) )
				index = device.getDeviceId();
		index = index + 1;
		
		String tuple = AgentCommunicationLanguage.getNewName() + "(" + deviceType + "(" + index + ")" + ")";
		LogicTuple logicTuple = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getDeviceManagerTupleCentre(), logicTuple, Long.MAX_VALUE);
		if (operation.isResultSuccess()) {
			
			// device(deviceType(index), info_device(watt,null,-1),s)
			tuple = AgentCommunicationLanguage.getDevice() + "(" + deviceType + "(" + index + ")" + "," + AgentCommunicationLanguage.getInfoDevice() + "(" + watt + "," + "null,-1" + ")" + "," + "s" + ")";
			logicTuple = LogicTuple.parse(tuple);
			getAcc().out(getDeviceManagerTupleCentre(), logicTuple, Long.MAX_VALUE);
			Device first = createDeviceByLogicTuple(logicTuple);
			getDevices().add(first);
			persistDevice(tuple);
			// ---
			
			// device(deviceType(index), info_device(watt,null,-1),p)
			tuple = AgentCommunicationLanguage.getDevice() + "(" + deviceType + "(" + index + ")" + "," + AgentCommunicationLanguage.getInfoDevice() + "(" + watt + "," + "null,-1" + ")" + "," + "p" + ")";
			logicTuple = LogicTuple.parse(tuple);
			getAcc().out(getDeviceManagerTupleCentre(), logicTuple, Long.MAX_VALUE);
			Device second = createDeviceByLogicTuple(logicTuple);
			getDevices().add(second);
			persistDevice(tuple);
			// ---
			
			onNewDevice(first);
			onNewDevice(second);
		}
	}
	
	private void persistDevice(String tuple) throws Exception {
		File file = new File("devices.txt");
		FileWriter writer = new FileWriter(file, true);
		writer.write(tuple + System.lineSeparator());
		writer.close();
	}
	
	private void onNewDevice(Device device) {
		for ( DeviceListener deviceListener : getListeners() )
			deviceListener.onNewDevice(device);
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg) { }

	@Override
	public void operationCompleted(ITucsonOperation arg) { }
}
