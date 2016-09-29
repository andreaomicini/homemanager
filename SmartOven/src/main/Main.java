package main;

import model.Device;
import model.agent.OvenAgent;
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
			
			TucsonTupleCentreId ovenTc = new TucsonTupleCentreId( "oven_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			TucsonTupleCentreId usageManagerTc = new TucsonTupleCentreId( "usage_manager_tc", configuration.getIp(), String.valueOf(configuration.getPort()) );
			
			OvenAgent ovenAgent = new OvenAgent(device, ovenTc, usageManagerTc);
			
			MainFrame mainFrame = new MainFrame(ovenAgent);
			mainFrame.setVisible(true);
			
			ovenAgent.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
