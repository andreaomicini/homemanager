package main;

import model.Device;
import model.agent.MixerAgent;
import model.configuration.Configuration;
import model.naming.NameManager;
import ui.MainFrame;
import alice.tucson.api.TucsonTupleCentreId;

public class Main {
	
	public static void main(String[] args) {
		try {
			NameManager nameManager = NameManager.getInstance();
			String name = nameManager.getName();
			
			Configuration configuration = Configuration.getInstance();
			
			Device device = new Device(name, "p", configuration.getDeviceWatt());
			
			TucsonTupleCentreId mixerTc = new TucsonTupleCentreId( "mixer_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			TucsonTupleCentreId mixerContainerTc = new TucsonTupleCentreId( "mixer_container_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			TucsonTupleCentreId ovenTc = new TucsonTupleCentreId( "oven_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			TucsonTupleCentreId usageManagerTc = new TucsonTupleCentreId( "usage_manager_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			
			MixerAgent mixerAgent = new MixerAgent(device, mixerTc, mixerContainerTc, ovenTc, usageManagerTc);
			
			MainFrame mainFrame = new MainFrame(mixerAgent);
			mainFrame.setVisible(true);
			
			mixerAgent.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
