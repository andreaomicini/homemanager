/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.homemanager.tablemap.ServicesInterfaces;

import alice.tucson.api.TucsonTupleCentreId;
import it.unibo.homemanager.ServiceFactory;
import it.unibo.homemanager.fridge.TucsonAgentInterface;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageDevicesPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author s0000590338
 */
public interface ManageDeviceAgentServiceInterface {
    
    public void initializeComponent(JPanel viewManageDevice,ServiceFactory sf,TracePanel tp,TucsonTupleCentreId tc,TucsonTupleCentreId tc2);
    public ArrayList<String> getListStringDevice();
    public ArrayList<TucsonAgentInterface> getListAgent();
     public TucsonAgentInterface getAgentbyName(String name);
    public void goListAgent();
   
}
