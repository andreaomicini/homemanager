package it.unibo.homemanager.detection;

import java.util.Map;

import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import it.unibo.homemanager.agents.FridgeAgent;
import it.unibo.homemanager.agents.FridgeSituatedAgent;
import it.unibo.homemanager.agents.MixerAgent;
import it.unibo.homemanager.agents.OvenAgent;
import it.unibo.homemanager.agents.PantryAgent;
import it.unibo.homemanager.agents.TucsonAgentInterface;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

public abstract class AgentsFactory {

	public static TucsonAgentInterface createAgent(Device device, Map<String, TucsonTupleCentreId> tupleCentres, TracePanel tracePanel, Database database, ViewManageAgentPanel butlerPanel) {
		if ( device.getDeviceName().equals("fridge") ) {
			try {
				FridgeSituatedAgent s_agent = new FridgeSituatedAgent(device, tupleCentres.get("fridge_tc"),tracePanel);
				s_agent.go();
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
}
