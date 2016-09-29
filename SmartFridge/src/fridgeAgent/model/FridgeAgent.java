package fridgeAgent.model;

import alice.logictuple.LogicTuple;
import alice.tucson.api.*;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import iotdevice.Sensor4;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FridgeAgent extends AbstractTucsonAgent {
    private Device device;
    private String name;

    private boolean state;

    private EnhancedSynchACC acc;

    private TucsonTupleCentreId fridgeTc;
    private TucsonTupleCentreId rfidMappingTc;
    private TucsonTupleCentreId mixerContainerTc;
    private TucsonTupleCentreId usageManagerTc;

    private Map<String, String> rfidMapping;
    private Sensor4 sensor;

    public FridgeAgent(Device device, TucsonTupleCentreId fridgeTc, TucsonTupleCentreId rfidMappingTc, TucsonTupleCentreId mixerContainerTc, TucsonTupleCentreId usageManagerTc) throws TucsonInvalidAgentIdException {
        super(device.getName() + "_" + device.getId());

        this.device = device;
        this.name = device.getName() + "_" + device.getId();
        state = false;

        this.fridgeTc = fridgeTc;
        this.rfidMappingTc = rfidMappingTc;
        this.mixerContainerTc = mixerContainerTc;
        this.usageManagerTc = usageManagerTc;

        rfidMapping = new HashMap<>();
    }

    @Override
    public void operationCompleted(ITucsonOperation arg) { }

    @Override
    public void operationCompleted(AbstractTupleCentreOperation arg) { }

    @Override
    protected void main() {
        System.out.println("--- " + getName() + " STARTED!");

        acc = getContext();

        try {
            rfidMapping = getRfidMapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            writePresenceInformation();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // sensor = new Sensor4();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("______________________________");
                System.out.println("Fridge ingredients");
                for ( Ingredient ingredient : getContent() )
                    System.out.println(" - " + ingredient.getName() + "(" + ingredient.getQuantity() + ")");
                
                // String rfid = sensor.readRfid();
                String rfid = randomRfid();
                
                if (!rfid.equalsIgnoreCase("NO-TAG")) {
                    System.out.println("______________________________");
                    System.out.println("Found: " + rfid);
                    String productName = rfidMapping.get(rfid);

                    String operation = "";
                    do {
                        System.out.println("Insert or Remove?");
                        operation = reader.readLine().trim();
                    } while (!(operation.equalsIgnoreCase("I") || operation.equalsIgnoreCase("R")));

                    int productQuantity = -1;

                    do {
                        System.out.println("Quantity?");
                        try {
                            productQuantity = Integer.parseInt(reader.readLine().trim());
                        } catch (NumberFormatException e) {
                            productQuantity = -1;
                        }
                    } while (productQuantity == -1);

                    if (operation.equalsIgnoreCase("I")) {
                        // sensor.display(productName, 1);

                        Ingredient ingredient = new Ingredient(productName, productQuantity);
                        addIngredient(ingredient);
                    } else if (operation.equalsIgnoreCase("R")) {
                        // sensor.display(productName, 0);

                        Ingredient ingredient = new Ingredient(productName, productQuantity);
                        removeIngredient(ingredient);
                    }

                    System.out.println("______________________________");
                    System.out.println("--- " + getName() + " UPDATED!");

                    for ( Ingredient ingredient : getContent() )
                        System.out.println(" - " + ingredient.getName() + "(" + ingredient.getQuantity() + ")");
                    
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean writePresenceInformation() throws Exception {
        String nameTemplate = "presence_information";
        String tuple = nameTemplate + "(" + device.getName() + "(" + device.getId() + ")" + "," + "info_device"
                + "(" + "off" + "," + device.getWatt() + ")" + "," + device.getType() + ")";
        
        LogicTuple template = LogicTuple.parse(tuple);
        ITucsonOperation operation = acc.out(usageManagerTc, template, Long.MAX_VALUE);
        
        if ( !operation.isResultSuccess() )
            return (false);
        
        nameTemplate = "state_change";
        tuple = nameTemplate + "(" + "order" + "," + device.getName() + "(" + device.getId() + ")" + ","
                 + "A" + "," + device.getType() + ")";

        template = LogicTuple.parse(tuple);
        operation = acc.in(usageManagerTc, template, Long.MAX_VALUE);

        if ( !operation.isResultSuccess() )
            return (false);
        
        LogicTuple result = operation.getLogicTupleResult();
        int third = 2;
        String deviceState = result.getArg(third).toString();
        state = deviceState.equals("on");
        return (true);
    }

    private String randomRfid() {
        String[] rfidArray = new String[]{"eab5e4a4", "aa8be4a4", "baf7e3a4", "caf0e1a4", "NO-TAG"};
        Random random = new Random();
        int index = random.nextInt(rfidArray.length);
        return (rfidArray[index]);
    }

    public String getName() {
        return (name);
    }

    public HashMap<String, String> getRfidMapping() throws Exception {
        String nameTemplate = "match";
        String tuple = nameTemplate + "(" + "mapping" + "," + "A,B" + ")";
        LogicTuple template = LogicTuple.parse(tuple);
        ITucsonOperation operation = acc.rdAll(rfidMappingTc, template, Long.MAX_VALUE);

        if ( !operation.isResultSuccess() )
            return (new HashMap<>());

        List<LogicTuple> allMatches = operation.getLogicTupleListResult();
        HashMap<String, String> result = new HashMap<>();

        int second = 1;
        int third = 2;

        for (LogicTuple match : allMatches) {
            String productRfid = match.getArg(second).toString();
            String productName = match.getArg(third).toString();
            result.put(productRfid, productName);
        }

        return (result);
    }

    public List<Ingredient> getContent() throws Exception {
        String nameTemplate = "content";
        String fridgeName = getName();

        String tuple = nameTemplate + "(" + fridgeName + "," + "A,B" + ")";
        LogicTuple template = LogicTuple.parse(tuple);
        ITucsonOperation operation = acc.rdAll(fridgeTc, template, Long.MAX_VALUE);

        if (!operation.isResultSuccess()) {
            return (new ArrayList<>());
        }

        List<LogicTuple> logicTuples = operation.getLogicTupleListResult();
        List<Ingredient> ingredients = new ArrayList<>();

        int second = 1;
        int third = 2;

        for (LogicTuple logicTuple : logicTuples) {
            String productName = logicTuple.getArg(second).toString();
            int productQuantity = logicTuple.getArg(third).intValue();
            Ingredient ingredient = new Ingredient(productName, productQuantity);
            ingredients.add(ingredient);
        }
        return (ingredients);
    }

    public boolean addIngredient(Ingredient ingredient) throws Exception {
        String nameTemplate = "content";
        String fridgeName = getName();

        String tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + "A" + ")";
        LogicTuple template = LogicTuple.parse(tuple);
        ITucsonOperation operation = acc.inp(fridgeTc, template, Long.MAX_VALUE);

        int third = 2;

        if (operation.isResultSuccess()) {
            LogicTuple result = operation.getLogicTupleResult();
            int oldQuantity = result.getArg(third).intValue();
            int newQuantity = oldQuantity + ingredient.getQuantity();
            tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + newQuantity + ")";
            template = LogicTuple.parse(tuple);
            operation = acc.out(fridgeTc, template, Long.MAX_VALUE);
            return (operation.isResultSuccess());
        } else { // ( if operation.isResultFailure() )
            tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + ingredient.getQuantity() + ")";
            template = LogicTuple.parse(tuple);
            operation = acc.out(fridgeTc, template, Long.MAX_VALUE);
            return (operation.isResultSuccess());
        }
    }

    public boolean removeIngredient(Ingredient ingredient) throws Exception {
        int oldQuantity = getQuantity(ingredient);
        int newQuantity = oldQuantity - ingredient.getQuantity();

        Ingredient newIngredient = new Ingredient(ingredient.getName(), newQuantity);
        return (updateContent(newIngredient));
    }

    public int getQuantity(Ingredient ingredient) throws Exception {
        List<Ingredient> content = getContent();

        if (!content.contains(ingredient)) {
            return (0);
        }

        Ingredient fridgeIngredient = content.get(content.indexOf(ingredient));
        return (fridgeIngredient.getQuantity());
    }

    private boolean updateContent(Ingredient ingredient) throws Exception {
        String nameTemplate = "content";
        String fridgeName = getName();
        
        String tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + "A" + ")";
        LogicTuple template = LogicTuple.parse(tuple);
        ITucsonOperation operation = acc.inp(fridgeTc, template, Long.MAX_VALUE);

        if (operation.isResultSuccess()) {
            int newQuantity = ingredient.getQuantity();
            if (newQuantity > 0) {
                tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + newQuantity + ")";
                template = LogicTuple.parse(tuple);
                operation = acc.out(fridgeTc, template, Long.MAX_VALUE);
                return (operation.isResultSuccess());
            }
            return (true);
        }
        return (false);
    }
}
