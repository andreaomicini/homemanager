/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frigoagent;

import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.network.exceptions.DialogCloseException;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tucson.service.TucsonNodeService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s0000661367
 */
public class Main {
    
    public static void main(String[] args) {
          //  int NUM = ServiceFactory.TUCSON;
            //ServiceFactory sf = ServiceFactory.getServiceFactory(NUM);
        try {
            //TucsonNodeService tns = null;
            
//           if(TucsonNodeService.isInstalled(20504))
//               System.out.println("Service already active");
//            else {
//                System.out.println("Service not running! :(");
//                System.exit(-1);
//                //tns = new TucsonNodeService(); 
//                //tns.install();
//                //Thread.sleep(30000);
//            }
            System.out.println("Initializing Connection to Home Manager!");
            TucsonTupleCentreId fridge_tc=new TucsonTupleCentreId("fridge", "localhost", "20504");
            TucsonTupleCentreId dictionary_tc=new TucsonTupleCentreId("dictionary", "localhost", "20504");
//          Versione Client/Server             
//           TucsonTupleCentreId fridge_tc=new TucsonTupleCentreId("fridge", "192.168.1.132", "20504");
//           TucsonTupleCentreId dictionary_tc=new TucsonTupleCentreId("dictionary", "192.168.1.132", "20504");
            FrigoAgent fAgent= new FrigoAgent("5", fridge_tc,dictionary_tc);
            fAgent.main();
          } catch (TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}