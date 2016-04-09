/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.homemanager.fridge;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.userinterfaces.ViewManageDevicesPanel;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewFridgePanel;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author s0000590338
 */
public class TucsonFridgeAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

    private ViewFridgePanel viewFridge;
    private User user;
    private String name;
    private EnhancedSynchACC acc;
    private TucsonTupleCentreId fridge_tc;
    private ViewManageDevicesPanel mdPanel;
    private TracePanel tp;

    public TucsonFridgeAgent(String id, String name, ViewManageDevicesPanel mdPanel, TucsonTupleCentreId fridge_tc, TracePanel tp) throws TucsonInvalidAgentIdException {
        super(id);
        this.name = name;
        mdPanel = mdPanel;
        this.tp = tp;
        this.fridge_tc = fridge_tc;
        viewFridge = new ViewFridgePanel(mdPanel, this, fridge_tc, tp);
    }

    @Override
    public void show(User user) {
        viewFridge.init(user);
    }

    @Override
    public JPanel getInterface() {
        return viewFridge;
    }

    @Override
    public void hide() {
        this.viewFridge.hidePanel();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void operationCompleted(AbstractTupleCentreOperation atco) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void operationCompleted(ITucsonOperation ito) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void main() {
    	
        this.acc = this.getContext();
        tp.appendText("--" + this.getName().toUpperCase() + " AGENT" + "  STARTED.");
        
	/*	try {
			Var fridge_name = new Var("fname");
			Var name_product = new Var("pnam");
		    Var quantity_value = new Var("");
		    Var id_user = new Var("");
		    LogicTuple template,result;
		    ITucsonOperation op_in;
       
        while (true) {
        	

            template = new LogicTuple("insert", new Var(),new Var(),new Var(), new Var());
            op_in = this.acc.inp(fridge_tc, template, Long.MAX_VALUE); 
            if(op_in.isOperationCompleted()){
            	result=op_in.getLogicTupleResult();
            	this.insertHandler(result);
            }
           template = new LogicTuple("delete", id_user, fridge_name, name_product, quantity_value);
            op_in = this.acc.inp(fridge_tc, template, Long.MAX_VALUE);
            if(op_in.isResultSuccess()){
            	this.deleteHandler(id_user, fridge_name, name_product, quantity_value);
            }
            
        }
		} catch (TucsonOperationNotPossibleException e) {
			
			e.printStackTrace();
		} catch (UnreachableNodeException e) {
			
			e.printStackTrace();
		} catch (OperationTimeOutException e) {
			
			e.printStackTrace();
		}*/
    }

    private void deleteHandler(Var id_user, Var fridge_name, Var name_product,Var quantity_value) {
		
		
		
	}

	private void insertHandler(LogicTuple result) {
	
	String tmp="";
	tmp=result.toString();
	int user = 0,quantity=0;
	String fname ="",pname="";
	String s1 = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);
    StringTokenizer t = new StringTokenizer(s1, ",");
    if (t.countTokens() == 4) {
        user = Integer.parseInt(t.nextToken());
        fname=(t.nextToken());
        pname = (t.nextToken());
        quantity = Integer.parseInt(t.nextToken());
    }
    LogicTuple template=new LogicTuple("fridge_content",new Value(fname),new Value(pname),new Value(quantity));
    ITucsonOperation op_out;
    try {
		op_out=this.acc.out(fridge_tc,template,Long.MAX_VALUE);
	} catch (TucsonOperationNotPossibleException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnreachableNodeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (OperationTimeOutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	}

	public ArrayList<String> getList() throws InvalidVarNameException, Exception {
        ArrayList<String> list = new ArrayList<String>();
        List l = null;
        Value fridge_name = new Value(name);
        String name_template = "fridge_content";

        LogicTuple template = new LogicTuple(name_template, fridge_name, new Var("X"), new Var("Y"));
        ITucsonOperation op_rdAll = this.acc.rdAll(fridge_tc, template, Long.MAX_VALUE);
        if (op_rdAll.isResultSuccess()) {
            l = op_rdAll.getLogicTupleListResult();
            for (int i = 0; i < l.size(); i++) {
                String product = l.get(i).toString();
                tp.appendText(product);
                list.add(createElementByTupleString(product));
            }
        } else {
            System.err.println("[readCentreArray(TA)@TucsonDatabase] rdAll(" + template + ") failed!");
        }
        return list;
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

    public void insert(String nameProduct, int quantity, User u) {
        try {
            Value fridge_name = new Value(name);
            Value name_product = new Value(nameProduct);
            Value quantity_value = new Value(quantity);
            Value id_user = new Value(u.idUser);

            LogicTuple template = new LogicTuple("insert", id_user, fridge_name, name_product, quantity_value);
            ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
        } catch (TucsonOperationNotPossibleException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnreachableNodeException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OperationTimeOutException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String nameProduct, int quantity, User u) {
        try {
            Value fridge_name = new Value(name);
            Value name_product = new Value(nameProduct);
            Value quantity_value = new Value(quantity);
            Value id_user = new Value(u.getId());

            LogicTuple template = new LogicTuple("delete", id_user, fridge_name, name_product, quantity_value);
            ITucsonOperation op_out = this.acc.out(fridge_tc, template, Long.MAX_VALUE);
        } catch (TucsonOperationNotPossibleException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnreachableNodeException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OperationTimeOutException ex) {
            Logger.getLogger(TucsonFridgeAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
