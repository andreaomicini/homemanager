package frigoagent;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.*;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import iotdevice.Sensor4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author s0000661367
 */
public class FrigoAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

private final TucsonTupleCentreId fridge_tc;
private final TucsonTupleCentreId dictionary_tc;
private EnhancedSynchACC acc;
private final String name="FridgeVincenzo";
private final String name2="FridgeMatteo";
private HashMap<String, String> dictionary;
private Sensor4 sensor;
	    

    public FrigoAgent(String id, TucsonTupleCentreId tuple,TucsonTupleCentreId tuple2) throws TucsonInvalidAgentIdException {
        super(id);
        this.fridge_tc=tuple;
        this.dictionary_tc=tuple2; 
    }  
    @Override
    public void operationCompleted(ITucsonOperation ito) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	    
    @Override
    public void operationCompleted(AbstractTupleCentreOperation atco) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	    
    @Override
    protected void main() {
	System.out.println("-- FRIGO AGENT STARTED.");
        TucsonAgentId agentPref;
        try{
        agentPref = new TucsonAgentId("5");
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(agentPref);
        this.acc = negAcc.playDefaultRole();
        this.dictionary=this.getDictionary();
        this.sensor=new Sensor4();
            while(true) {      	
	            	//Thread.sleep(1000);
	            	//ATTENDO DI LEGGERE UN RFID VALIDO
                        String tagrfid=this.sensor.readRfid();
                        // IF VALIDO 
                        //faccio un controllo di collegamento tra id e nome prodotto
                         if (!tagrfid.equalsIgnoreCase("NO-TAG")) 
                           {
                                String nameProduct= this.dictionary.get(tagrfid);
                                //this.sensor.Display(nameProduct);
                                // CONTROLLO SE E' GIA' NEL FRIGO
                                ArrayList<String> presenti=this.getRealList();
                                //RESTITUISCE QUALCOSA COME "KEY VALUE"
                                boolean trovato=false;
                                for(String s: presenti)
                                {
                                    if (s.contains(tagrfid))
                                    {
                                        trovato=true;
                                        break;
                                    }                        
                                }
                                User adminFrigo=new User(123, "AdminFrigo", "AdminFrigo", "admin", "admin", "AdminFrigo", 25, 22, "AdminFrigo");
                                if (trovato==true)
                                {
                                    System.out.println("TROVATO");
                                    this.sensor.Display(nameProduct,0);
                                //SE CONTIENE DEVO ELIMINARE UNA QUANTITA'
                                    this.deleteR(tagrfid,nameProduct);
                                    this.delete(nameProduct, 1, adminFrigo);
                                }
                                //ALTRIMENTI LO INSERISCO
                                else 
                                {
                                    System.out.println("NON TROVATO");
                                    this.sensor.Display(nameProduct,1);
                                    this.insertR(tagrfid,nameProduct);
                                    this.insert(nameProduct, 1, adminFrigo);
                                }

                                System.out.println("-- FRIGO AGENT UPDATED.");
                                Thread.sleep(2000);
                                ArrayList<String> now=this.getList();
                                now.stream().forEach((s) -> {System.out.println(s);}); 
                                ArrayList<String> now2=this.getRealList();
                                now2.stream().forEach((s) -> {System.out.println(s);});
                           }
                    }
	          }catch(Exception ex){
	              System.out.println("FrigoAgent : error in activity.");
	          }
		}
@Override
public String getName() {
    return "Agente Frigo";
}
 public HashMap<String, String> getDictionary() throws InvalidVarNameException, Exception {
        ArrayList<String> list = new ArrayList<>();
        List l = null;
        String name_template = "match";
        LogicTuple template = new LogicTuple(name_template,new Value("DictionaryMatteo"), new Var("X"), new Var("Y"));
        ITucsonOperation op_rdAll = this.acc.rdAll(dictionary_tc, template, Long.MAX_VALUE);
        if (op_rdAll.isResultSuccess()) {
            l = op_rdAll.getLogicTupleListResult();
            for (int i = 0; i < l.size(); i++) {
                String couple = l.get(i).toString();
                System.out.println(couple);
                list.add(createStringByTupleString(couple));
            }
        } else {
            System.err.println("[readCentreArray(TA)@TucsonDatabase] rdAll(" + template + ") failed!");
        }
        HashMap<String,String> res= new HashMap<String,String>();
        for(String s:list)
        {
            StringTokenizer st= new StringTokenizer(s);
            String k=st.nextToken();
            String v=st.nextToken();
            res.put(k,v);
        }
        return res;
    }
  public ArrayList<String> getRealList() throws InvalidVarNameException, Exception {
        ArrayList<String> list = new ArrayList<>();
        List l = null;
        Value fridge_name = new Value(name2);
        String name_template = "real_fridge";

        LogicTuple template = new LogicTuple(name_template, fridge_name, new Var("X"), new Var("Y"));
        ITucsonOperation op_rdAll = this.acc.rdAll(fridge_tc, template, Long.MAX_VALUE);
        if (op_rdAll.isResultSuccess()) {
            l = op_rdAll.getLogicTupleListResult();
            for (int i = 0; i < l.size(); i++) {
                String product = l.get(i).toString();
                System.out.println(product);
                list.add(createStringByTupleString(product));
            }
        } else {
            System.err.println("[readCentreArray(TA)@TucsonDatabase] rdAll(" + template + ") failed!");
        }
        return list;
    }
 public ArrayList<String> getList() throws InvalidVarNameException, Exception {
        ArrayList<String> list = new ArrayList<>();
        List l = null;
        Value fridge_name = new Value(name);
        String name_template = "fridge_content";

        LogicTuple template = new LogicTuple(name_template, fridge_name, new Var("X"), new Var("Y"));
        ITucsonOperation op_rdAll = this.acc.rdAll(fridge_tc, template, Long.MAX_VALUE);
        if (op_rdAll.isResultSuccess()) {
            l = op_rdAll.getLogicTupleListResult();
            for (int i = 0; i < l.size(); i++) {
                String product = l.get(i).toString();
                System.out.println(product);
                list.add(createElementByTupleString(product));
            }
        } else {
            System.err.println("[readCentreArray(TA)@TucsonDatabase] rdAll(" + template + ") failed!");
        }
        return list;
    }
  private String createStringByTupleString(String s) {
        String s1 = s.substring(s.indexOf("(") + 1, s.length() - 1);
        StringTokenizer t = new StringTokenizer(s1, ",");
        if (t.countTokens() == 3) {
            String nameDevice = (t.nextToken());
            String key = t.nextToken();
            String value = t.nextToken();
            return key.substring(1,key.length()-1) + " " + value;
        }
        return "";
    }
    private String createElementByTupleString(String s) {
        String s1 = s.substring(s.indexOf("(") + 1, s.length() - 1);
        StringTokenizer t = new StringTokenizer(s1, ",");
        if (t.countTokens() == 3) {
            String nameDevice = (t.nextToken());
            String nameProduct = (t.nextToken());
            int quantity = Integer.parseInt(t.nextToken());
            return nameProduct + " " + quantity;
        }
        return "";
    }
 public void insertR(String tagrfi,String nameProduct) {
        try {
            Value fridge_name = new Value(name2);
            Value name_product = new Value(nameProduct);
            Value tagRfid = new Value(tagrfi);
           // Value id_user = new Value(u.idUser);
            String name_template = "real_fridge";

           // LogicTuple template = new LogicTuple("insert", id_user, fridge_name, name_product, quantity_value);
             LogicTuple template = new LogicTuple(name_template, fridge_name,tagRfid, name_product);
            ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
        //    ITucsonOperation op_out = this.acc.out(fridge_tc, template, null);
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException ex) {
            Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void insert(String nameProduct, int quantity, User u) {
        try {
            Value fridge_name = new Value(name);
            Value name_product = new Value(nameProduct);
            Value quantity_value = new Value(quantity);
            //Value id_user = new Value(u.idUser);
            String name_template = "fridge_content";
            int oldQuantity=this.getQuantity(nameProduct);
            if (oldQuantity==0)
            {// LogicTuple template = new LogicTuple("insert", id_user, fridge_name, name_product, quantity_value);
                LogicTuple template = new LogicTuple(name_template, fridge_name, name_product, quantity_value);
                ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
            }
            else 
            {
                int newQuantity=oldQuantity+1;
                LogicTuple template = new LogicTuple(name_template, fridge_name, name_product, new Value(oldQuantity));
                ITucsonOperation op_in = this.acc.in(fridge_tc, template, Long.MAX_VALUE);
                LogicTuple template2 = new LogicTuple(name_template, fridge_name, name_product, new Value(newQuantity));
                ITucsonOperation op_out = this.acc.out(fridge_tc, template2, Long.MAX_VALUE);
            } 
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException ex) {
            Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
public void deleteR(String tagRfi, String name_produc) {
        try {
            Value fridge_name = new Value(name2);
            Value name_product = new Value(name_produc);
            Value tagRfid = new Value(tagRfi);
            //Value id_user = new Value(u.idUser);
            String name_template = "real_fridge";

            LogicTuple template = new LogicTuple(name_template, fridge_name,tagRfid, name_product);
            ITucsonOperation op_out = this.acc.in(fridge_tc, template, Long.MAX_VALUE);
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException ex) {
            Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getQuantity(String nameProduct) throws Exception
    {
        String present=null;
        for(String s :this.getList())
            {
                if (s.contains(nameProduct))present=s;
            }
        if (present==null)return 0;
        //A QUESTO PUNTO PRESENT NON PUò ESSERE NULL
        StringTokenizer st= new StringTokenizer(present);
        st.nextToken();
        return Integer.parseInt(st.nextToken());
    }
    public void delete(String nameProduct, int quantity, User u) {
        try {
            Value fridge_name = new Value(name);
            Value name_product = new Value(nameProduct);
            Value quantity_value = new Value(quantity);
           // Value id_user = new Value(u.idUser);
            String name_template = "fridge_content";
            int oldQuantity=this.getQuantity(nameProduct);
            if (oldQuantity==1)
            {//LogicTuple template = new LogicTuple("delete", id_user, fridge_name, name_product, quantity_value);
            LogicTuple template = new LogicTuple(name_template, fridge_name, name_product, quantity_value);
            ITucsonOperation op_in = this.acc.in(fridge_tc, template, Long.MAX_VALUE);
            }
            else 
            {
                int newQuantity=oldQuantity-1;
                LogicTuple template = new LogicTuple(name_template, fridge_name, name_product, new Value(oldQuantity));
                ITucsonOperation op_in = this.acc.in(fridge_tc, template, Long.MAX_VALUE);
                LogicTuple template2 = new LogicTuple(name_template, fridge_name, name_product, new Value(newQuantity));
                ITucsonOperation op_out = this.acc.out(fridge_tc, template2, Long.MAX_VALUE);
            }
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException | OperationTimeOutException ex) {
            Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        Logger.getLogger(FrigoAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JPanel getInterface() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
