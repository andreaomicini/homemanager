package wardrobeAgent.main;

import alice.tucson.api.TucsonTupleCentreId;
import wardrobeAgent.configuration.Configuration;
import wardrobeAgent.lpaasLibrary.LPaaSLibrary;
import wardrobeAgent.model.Device;
import wardrobeAgent.model.WardrobeSensorsAgent;
import wardrobeAgent.naming.NameManager;

public class Main {

	public static void main(String[] args) {
		try {
			LPaaSLibrary.init();

			NameManager nameManager = NameManager.getInstance();
			String name = nameManager.getName();

			Configuration configuration = Configuration.getInstance();

			Device device = new Device(name, "p", configuration.getDeviceWatt());

			TucsonTupleCentreId wardrobeTc = new TucsonTupleCentreId("wardrobe_tc", "localhost", "20504");
			TucsonTupleCentreId usageManagerTc = new TucsonTupleCentreId("usage_manager_tc", "localhost", "20504");

			WardrobeSensorsAgent wardrobeAgent = new WardrobeSensorsAgent(device, wardrobeTc, usageManagerTc);

			wardrobeAgent.go();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
