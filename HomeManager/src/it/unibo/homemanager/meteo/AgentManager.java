/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.homemanager.meteo;

import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.dbmanagement.TucsonDatabase;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s0000590338
 */
public class AgentManager {
    
    private List<TucsonTupleCentreId> tcs=null;
    private ArrayList<TucsonAgentInterface> listAgentInterface;
    private Database database;
    //private List listAgent;
    private static AgentManager istanza = null;
    private ViewManageAgentPanel viewManagerAgent;
    private TracePanel tp;
    
    
    public AgentManager(){
    }
    
    public static AgentManager getInstance() {
        if (istanza == null) {
                istanza = new AgentManager();
        }
            return istanza;
    }
    
    public void inizializeComponet(ViewManageAgentPanel viewManageAgentPanel,ServiceFactory sf,TracePanel tp,List<TucsonTupleCentreId> tcs){
    	this.viewManagerAgent=viewManageAgentPanel;
        try {
            this.database = sf.getDatabaseInterface();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(this.tcs==null){
                this.tcs=tcs;
        }
       /* try {
            listAgent = getListAgentService();
        } catch (InvalidVarNameException ex) {
            Logger.getLogger(ViewManageAgentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        this.tp=tp;
        initListAgent();
    }
    
    private void initListAgent() {
        listAgentInterface=new ArrayList<TucsonAgentInterface>();
        MeteoAgent m = null;
        try {
           
        	m=new MeteoAgent("meteoAgent_1",tp,tcs.get(0),database);  
        	m.setPanel(viewManagerAgent);
        	
        } catch (TucsonInvalidAgentIdException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        listAgentInterface.add(m);
    }
     
    
    
   /* private List getListAgentService() throws InvalidVarNameException {
        List listAgent = null;
        TucsonDatabase db = (TucsonDatabase) database;
        String nome_template = "service_agent";
        Value template = new Value(nome_template, new Var("X"), new Var("Y"));
        try {
            listAgent = db.readCentre(template);
        } catch (Exception ex) {
            Logger.getLogger(ViewManageAgentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listAgent;  //ritorna null
    }*/
    
    public ArrayList<TucsonAgentInterface> getListAgent(){
        return this.listAgentInterface;
    }
    
    @SuppressWarnings("unused")
	private String getNameAgent(String s) {
        String s1 = s.substring(s.indexOf("(") + 1, s.length() - 1);
        StringTokenizer t = new StringTokenizer(s1, ",");
        String result = "";
        if (t.countTokens() == 2) {
            int idAgent = Integer.parseInt(t.nextToken());
            String name = t.nextToken();
            result = name.substring(1, name.length() - 1) + " " + idAgent;
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
    
    public ArrayList<String> getListStringAgent(){
        ArrayList<String> list=new ArrayList<String>();
        for (int i = 0; i < this.listAgentInterface.size(); i++) {
            //list.add(getNameAgent(listAgent.get(i).toString()));
        	list.add(listAgentInterface.get(i).getName());
        }
        return list;
    }
       
    public void goListAgent() {
        for(TucsonAgentInterface ti:listAgentInterface){
            AbstractTucsonAgent atA=(AbstractTucsonAgent)ti;
            atA.go();
        }
    }
    
    public ViewManageAgentPanel getPanel()
    {
    	return this.viewManagerAgent;
    }

	

}
