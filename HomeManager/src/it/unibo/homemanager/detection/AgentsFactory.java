package it.unibo.homemanager.detection;

import java.util.Map;

import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import it.unibo.homemanager.agents.FridgeAgent;
import it.unibo.homemanager.agents.MixerAgent;
import it.unibo.homemanager.agents.OvenAgent;
import it.unibo.homemanager.agents.PantryAgent;
import it.unibo.homemanager.agents.TucsonAgentInterface;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.humanpresence.PresenceAgent;
import it.unibo.homemanager.meteo.MeteoAgent;
import it.unibo.homemanager.predictionWEKA.SmartPredictorAgent;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

public abstract class AgentsFactory {

	public static TucsonAgentInterface createAgent(Device device, Map<String, TucsonTupleCentreId> tupleCentres, TracePanel tracePanel, Database database, ViewManageAgentPanel butlerPanel) {
		if ( device.getDeviceName().equals("fridge") ) {
			try {
				FridgeAgent agent = new FridgeAgent(device, tracePanel, tupleCentres.get("fridge_tc"), tupleCentres.get("mixer_container_tc"), tupleCentres.get("usage_manager_tc"), database);
				agent.setPanel(butlerPanel);
				return (agent);
			} catch (TucsonInvalidAgentIdException e) {
				e.printStackTrace();
			}
		} else if ( device.getDeviceName().equals("pantry") ) {
			try {
				PantryAgent agent = new PantryAgent(device, tracePanel, tupleCentres.get("pantry_tc"), tupleCentres.get("mixer_container_tc"), tupleCentres.get("usage_manager_tc"), database);
				agent.setPanel(butlerPanel);
				return (agent);
			} catch (TucsonInvalidAgentIdException e) {
				e.printStackTrace();
			}
		} else if ( device.getDeviceName().equals("mixer") ) {
			try {
				MixerAgent agent = new MixerAgent(device, tracePanel, tupleCentres.get("mixer_tc"), tupleCentres.get("mixer_container_tc"), tupleCentres.get("oven_tc"), tupleCentres.get("usage_manager_tc"), database);
				agent.setPanel(butlerPanel);
				return (agent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ( device.getDeviceName().equals("oven") ) {
			try {
				OvenAgent agent = new OvenAgent(device, tracePanel, tupleCentres.get("oven_tc"), tupleCentres.get("usage_manager_tc"), database);
				agent.setPanel(butlerPanel);
				return (agent);
			} catch (TucsonInvalidAgentIdException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return (null);
	}
	
	
	public static TucsonAgentInterface createAgent(String nome,Map<String, TucsonTupleCentreId> tupleCentres, TracePanel tracePanel, Database database, ViewManageAgentPanel butlerPanel)
	  {
		TucsonTupleCentreId centro = tupleCentres.get(nome);
		
	 if(nome.equalsIgnoreCase("presence_tc")) 
	    {
			 
			  
			try {
				TucsonTupleCentreId centro_presence = new TucsonTupleCentreId("presence","localhost","20504");
				PresenceAgent ma = new PresenceAgent("presenceid",tracePanel,centro_presence,database,1);
				ma.setPanel(butlerPanel);
				return ma;
			} catch (TucsonInvalidAgentIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TucsonInvalidTupleCentreIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		    }
	 if(centro!=null)
		   {
			 if(nome.equalsIgnoreCase("meteo_tc")) 
			    {
				 
				  
				try {
					MeteoAgent ma = new MeteoAgent("meteoid",tracePanel,centro,database);
					ma.setPanel(butlerPanel);
					return ma;
				} catch (TucsonInvalidAgentIdException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
			    } else if(nome.equalsIgnoreCase("predictor_tc"))
			       {
			    	try {
			    		TucsonTupleCentreId centro_policy  = tupleCentres.get("rbac_tc");
			    		 TucsonTupleCentreId centro_presence = new TucsonTupleCentreId("presence","localhost","20504");
			    		TucsonTupleCentreId centro_meteo  = tupleCentres.get("meteo_tc");
			    		TucsonTupleCentreId centro_sala  = tupleCentres.get("sala_tc");
			    		
						SmartPredictorAgent spa = new SmartPredictorAgent("smartpredictoragent",tracePanel,centro_policy,centro_meteo, centro_presence,database,centro_sala);
						spa.setPanel(butlerPanel);
						return spa;
					} catch (TucsonInvalidAgentIdException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       }
			    else if(nome.equalsIgnoreCase("predictorcamera_tc"))
			       {
			    	try {
			    		TucsonTupleCentreId centro_policy  = tupleCentres.get("rbac_tc");
			    		 TucsonTupleCentreId centro_presence = new TucsonTupleCentreId("presence","localhost","20504");
			    		TucsonTupleCentreId centro_meteo  = tupleCentres.get("meteo_tc");
			    		TucsonTupleCentreId centro_camera  = tupleCentres.get("camera_tc");
			    		
						SmartPredictorAgent spa = new SmartPredictorAgent("smartpredictoragentcamera",tracePanel,centro_policy,centro_meteo, centro_presence,database,centro_camera);
						spa.setPanel(butlerPanel);
						return spa;
					} catch (TucsonInvalidAgentIdException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       } 
		   }
		
		else return null;
		
		
		return null;
		
	  }
}
