package fridgeAgent.naming;

import fridgeAgent.configuration.Configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import alice.logictuple.LogicTuple;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

public class NameManager {

    private static NameManager nameManager;

    private NameManager() { }

    public static NameManager getInstance() {
        if (nameManager == null) {
            nameManager = new NameManager();
        }
        return (nameManager);
    }

    public String getName() throws Exception {
        File file = new File("name.txt");
        if (!file.exists()) {
            Configuration configuration = Configuration.getInstance();

            TucsonAgentId agentId = new TucsonAgentId("naming");
            EnhancedSynchACC acc = TucsonMetaACC.getContext(agentId);

            TucsonTupleCentreId managerTc = new TucsonTupleCentreId("device_manager_tc", configuration.getIp(), String.valueOf(configuration.getPort()));

            String tuple = "new_name(unknown" + "," + configuration.getDeviceType() + "," + "info_device" + "(" + configuration.getDeviceWatt() + ")" + ")";
            LogicTuple template = LogicTuple.parse(tuple);

            ITucsonOperation operation = acc.out(managerTc, template, Long.MAX_VALUE);

            if (!operation.isResultSuccess()) {
                return (null);
            }

            tuple = "new_name" + "(" + configuration.getDeviceType() + "(" + "A" + ")" + ")";
            template = LogicTuple.parse(tuple);

            Long timeout = new Long(5000);
            try {
                operation = acc.in(managerTc, template, timeout);
            } catch (OperationTimeOutException e) {
                return (null);
            }

            if (!operation.isResultSuccess()) {
                return (null);
            }

            LogicTuple result = operation.getLogicTupleResult();

            int first = 0;
            int index = result.getArg(first).getArg(first).intValue();

            String name = configuration.getDeviceType() + "_" + index;

            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println(name);
            writer.close();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String name = reader.readLine();
        reader.close();

        return (name);
    }

}
