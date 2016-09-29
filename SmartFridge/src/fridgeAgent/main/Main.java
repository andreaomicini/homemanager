package fridgeAgent.main;

import fridgeAgent.model.Device;
import alice.tucson.api.TucsonTupleCentreId;
import fridgeAgent.configuration.Configuration;
import fridgeAgent.model.FridgeAgent;
import fridgeAgent.naming.NameManager;

public class Main {

    public static void main(String[] args) {
        try {
            NameManager nameManager = NameManager.getInstance();
            String name = nameManager.getName();

            Configuration configuration = Configuration.getInstance();

            Device device = new Device(name, "p", configuration.getDeviceWatt());

            TucsonTupleCentreId fridgeTc = new TucsonTupleCentreId("fridge_tc", "localhost", "20504");
            TucsonTupleCentreId rfidMappingTc = new TucsonTupleCentreId("dictionary", "localhost", "20504");
            TucsonTupleCentreId mixerContainerTc = new TucsonTupleCentreId("mixer_container_tc", "localhost", "20504");
            TucsonTupleCentreId usageManagerTc = new TucsonTupleCentreId("usage_manager_tc", "localhost", "20504");

            FridgeAgent fridgeAgent = new FridgeAgent(device, fridgeTc, rfidMappingTc, mixerContainerTc, usageManagerTc);
            fridgeAgent.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
