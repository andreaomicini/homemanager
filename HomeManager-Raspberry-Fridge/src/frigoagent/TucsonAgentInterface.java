/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frigoagent;

import alice.respect.api.AbstractAgent;
import alice.tucson.api.TucsonTupleCentreId;
//import it.unibo.homemanager.tablemap.User;
import javax.swing.JPanel;

/**
 *
 * @author s00661367
 */
public interface TucsonAgentInterface {
    
   // public void show(User user);
    public void hide();
    public JPanel getInterface();
    public String getName();
}
