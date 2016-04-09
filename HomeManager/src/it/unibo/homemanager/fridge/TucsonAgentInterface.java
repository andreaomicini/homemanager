/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.homemanager.fridge;

import alice.respect.api.AbstractAgent;
import alice.tucson.api.TucsonTupleCentreId;
import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import javax.swing.JPanel;

/**
 *
 * @author s0000590338
 */
public interface TucsonAgentInterface {
    
    public void show(User user);
    public void hide();
    public JPanel getInterface();
    public String getName();
}
