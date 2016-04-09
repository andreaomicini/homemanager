/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.homemanager.fridge;

import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.dbmanagement.TucsonDatabase;
import it.unibo.homemanager.oven.PrologOvenAgent;
import it.unibo.homemanager.tablemap.ServicesInterfaces.ManageDeviceAgentServiceInterface;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageDevicesPanel;
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
public class TucsonManageDevice implements ManageDeviceAgentServiceInterface{

    private ArrayList<TucsonAgentInterface> listAgentInterface;
    private Database database;
    private List listDevice;
    private TracePanel tp;
    private TucsonTupleCentreId fridge_tc;
    private TucsonTupleCentreId ovenfridge_tc;
    private ViewManageDevicesPanel viewManageDevice;
    
    public TucsonManageDevice(){}
    
    public void initializeComponent(JPanel viewManageDevice,ServiceFactory sf,TracePanel tp,TucsonTupleCentreId fridge_tc,TucsonTupleCentreId ovenfridge_tc) {
        this.tp=tp;
        this.viewManageDevice=(ViewManageDevicesPanel)viewManageDevice;
        this.fridge_tc=fridge_tc;
        this.ovenfridge_tc=ovenfridge_tc;
        
        try {
            this.database = sf.getDatabaseInterface();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initListAgent();

        try {
            listDevice = getListDevice();
        } catch (InvalidVarNameException ex) {
            Logger.getLogger(ViewManageDevicesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

     private void initListAgent() {
        listAgentInterface = new ArrayList<TucsonAgentInterface>();
       TucsonFridgeAgent tucsonFridgeAgent = null;
       PrologFridgeAgent prologFridgeAgent=null;
       PrologOvenAgent prologOvenAgent=null;
        try {
        	
            tucsonFridgeAgent = new TucsonFridgeAgent("5", "FridgeVincenzo", viewManageDevice, fridge_tc, tp);
            prologFridgeAgent= new PrologFridgeAgent("6","FridgeArmando",viewManageDevice,fridge_tc,ovenfridge_tc,tp);
            prologOvenAgent= new PrologOvenAgent("7","OvenArmando",viewManageDevice, fridge_tc,tp);
        } catch (TucsonInvalidAgentIdException ex) {
            Logger.getLogger(TucsonManageDevice.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
        listAgentInterface.add(tucsonFridgeAgent);
		listAgentInterface.add(prologFridgeAgent);
	    listAgentInterface.add(prologOvenAgent);
		
        
    }

    public ArrayList<TucsonAgentInterface> getListAgent() {
        return this.listAgentInterface;
    }

    private List getListDevice() throws InvalidVarNameException {
        List listDevices = null;
        TucsonDatabase db = (TucsonDatabase) database;
        String nome_template = "device";
        Value template = new Value(nome_template, new Var("X"), new Var("Y"), new Var("Z"), new Var("A"), new Var("B"), new Var("C"));
        try {
            listDevices = db.readCentre(template);
        } catch (Exception ex) {
            Logger.getLogger(ViewManageDevicesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listDevices;
    }
    
    private String getNameDevice(String s) {
        String s1 = s.substring(s.indexOf("(") + 1, s.length() - 1);
        StringTokenizer t = new StringTokenizer(s1, ",");
        String result = "";
        if (t.countTokens() == 6) {
            int idDev = Integer.parseInt(t.nextToken());
            String name = t.nextToken();
            result = name.substring(1, name.length() - 1) + " " + idDev;
        }
        return result;
    }
    
    public TucsonAgentInterface getAgentbyName(String name) {
        for (TucsonAgentInterface tAI : listAgentInterface) {
            if (tAI.getName().equalsIgnoreCase(name)) {
                return tAI;
            }
        }
        return null;
    }
    
    public ArrayList<String> getListStringDevice(){
        ArrayList<String> list=new ArrayList<String>();
        for (int i = 0; i < this.listDevice.size(); i++) {
            list.add(getNameDevice(listDevice.get(i).toString()));
        }
        return list;
    }

    public void goListAgent() {
        for(TucsonAgentInterface ti:listAgentInterface){
            AbstractTucsonAgent atA=(AbstractTucsonAgent)ti;
            atA.go();
        }
    }
}
