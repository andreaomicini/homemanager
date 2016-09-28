package pantryAgent.main;

import pantryAgent.model.Device;
import alice.tucson.api.TucsonTupleCentreId;
import pantryAgent.configuration.Configuration;
import pantryAgent.model.PantryAgent;
import pantryAgent.naming.NameManager;

public class Main {

    public static void main(String[] args) {
        try {
            NameManager nameManager = NameManager.getInstance();
            String name = nameManager.getName();

            Configuration configuration = Configuration.getInstance();

            Device device = new Device(name, "p", configuration.getDeviceWatt());

            TucsonTupleCentreId pantryTc = new TucsonTupleCentreId("pantry_tc", "localhost", "20504");
            TucsonTupleCentreId rfidMappingTc = new TucsonTupleCentreId("dictionary", "localhost", "20504");
            TucsonTupleCentreId mixerContainerTc = new TucsonTupleCentreId("mixer_container_tc", "localhost", "20504");
            TucsonTupleCentreId usageManagerTc = new TucsonTupleCentreId("usage_manager_tc", "localhost", "20504");

            PantryAgent pantryAgent = new PantryAgent(device, pantryTc, rfidMappingTc, mixerContainerTc, usageManagerTc);
            pantryAgent.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
