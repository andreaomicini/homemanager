import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.TucsonNodeService;

public class Main {
    /**
     * @param args
     */
    public static void main(final String[] args) {
        TucsonTupleCentreId tc;
        TucsonNodeService tns = null;
        TucsonAgentId agent, agent2;
        EnhancedSynchACC acc,acc2;
        final String porta = "20504";
        final String rete = "localhost";
        try {
            if (TucsonNodeService.isInstalled(rete, 20504, 5000)) {
                System.out.println("Nodo già  attivo");
            } else {
                tns = new TucsonNodeService(20504);
                tns.install();
                Thread.sleep(5000);
            }
            tc = new TucsonTupleCentreId("infoTc", rete, porta);
            System.out.println("tc.getNode = " + tc.getNode()
                    + " tc.getPort = " + tc.getPort());
            agent = new TucsonAgentId("dbAg");
            acc = TucsonMetaACC.getContext(agent, tc.getNode(), tc.getPort());
            LogicTuple lt = LogicTuple.parse("utente(1,ila,ila)");
            ITucsonOperation op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            lt = LogicTuple.parse("utente(2,grace,grace)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            
            lt = LogicTuple.parse("device(1,frizzyair,20)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            lt = LogicTuple.parse("device(2,hotfire,20)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            lt = LogicTuple.parse("device(3,captainoven,20)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            
            lt = LogicTuple.parse("device_state(3,on,100)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            lt = LogicTuple.parse("device_state(2,off,0)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
            lt = LogicTuple.parse("device_state(1,on,21)");
            op = acc.out(tc, lt, null);
            if (op.isResultSuccess()) {
                System.out.println("Ho inserito");
            } else {
                System.out.println("Ho fallito");
            }
            
          
            
            System.out.println("FINE INSERIMENTI**********");
            
            
            
            agent2 = new TucsonAgentId("suggAg");
            acc2= TucsonMetaACC.getContext(agent2);
            lt = LogicTuple.parse("geo_position("+new Var("Z")+","+ new Var("X")+","+ new Var("Y")+")");
            op = acc2.in(tc, lt, null);
            if (op.isResultSuccess()) {
            	System.out.println("Pizza take away position received");
            	lt = LogicTuple.parse("suggerimento(forno)");
            	ITucsonOperation op2 = acc2.out(tc, lt, null);

            	if (op2.isResultSuccess()) {System.out.println("Sugg written");}
               
               
               /*cambio stato della device forno*/
               lt = LogicTuple.parse("device_state(3"+new Var("Z")+","+ new Var("X")+")");
               ITucsonOperation op3 = acc.inp(tc, lt, null);
               if (op3.isResultSuccess()) {
                   
            	   lt = LogicTuple.parse("device_state(3,on,50)");
            	   ITucsonOperation op4 = acc.out(tc, lt, null);
            	   if (op4.isResultSuccess())
            	      {
            		   System.out.println("State oven changed");
            	      }
            	   
               } else {
                   System.out.println("Ho fallito");
               }
               
               
               
            } else {
                System.out.println("Ho fallito la posizione");
            }
          
//            acc.exit();
//            acc2.exit();
        } catch (final Exception e) {
            System.err.println(e.toString());
        }
    }
}
