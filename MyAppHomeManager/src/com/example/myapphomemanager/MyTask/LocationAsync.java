package com.example.myapphomemanager.MyTask;



import alice.logictuple.LogicTuple;
import alice.logictuple.Value;

import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.TucsonNodeService;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class LocationAsync extends AsyncTask<Double, Void, String> {

	EnhancedSynchACC acc; 
    TucsonAgentId agent;
    TucsonTupleCentreId tc;
    TucsonNodeService tcn;
    NotificationManager nm;
    Context c;
    
    public LocationAsync(NotificationManager manager, Context ct)
      {
    	  nm = manager;
    	  c = ct;
      }
    @Override
    protected void onPreExecute() {
            // TODO Auto-generated method stub
          super.onPreExecute();
          
          Log.i("Rilevo locazione= ","OK" );
          
    }
    
	@Override
	protected String doInBackground(Double... params) {
		
		String s = null;
	
		double lat=params[0] ;
		double lon= params[1];
		
		try {
            this.tc = new TucsonTupleCentreId("infoTc", "10.0.2.2", "20504");
            Log.i("Centro: ", this.tc.toString());
            Log.i("Indirizzo rete: ", this.tc.getNode());
            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
            this.agent = new TucsonAgentId("locAg");
            Log.i("Agente: ", this.agent.toString());
            this.acc = TucsonMetaACC.getContext(this.agent, "10.0.2.2",
                    20504);
            Log.i("Acc: ", this.acc.toString());
            //deposito la tupla per la localizzazione lat e lon
            final LogicTuple arr = LogicTuple.parse("geo_position("+ new Value(1)+"," + new Value(3) + "," 
                     + new Value(6)+ ")");
            Log.i("Arr = ", arr.toString());
            final ITucsonOperation op = this.acc.out(this.tc, arr,Long.MAX_VALUE);
           
            if (op.isResultSuccess()) {
                Log.i("Risulato = ", "Bene");
                     
            } else {
                Log.i("Risultato = ", "Male");
            }
            /*recupero il testo del suggerimento*/
            Thread.sleep(10000);
            LogicTuple arr1 = LogicTuple.parse("suggerimento("+ new Var("Z")+")");
             Log.i("Suggerimento = ", arr1.toString());
             final ITucsonOperation op1 = this.acc.in(this.tc, arr1,
            Long.MAX_VALUE);
         if (op1.isResultSuccess()) {
           Log.i("Risulato = ", "Bene");
           String letto =op1.getLogicTupleResult().toString();
           s = letto.substring(letto.indexOf('(')+1,letto.length()-1);
          } else {
        Log.i("Risultato = ", "Male");
         }
        
         
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            Log.i("Creazione errore", "Non è stato possibile continuare");
        }
        return s;
	}
	
	@Override
	protected void onPostExecute(String ris)
	   {
		 super.onPostExecute(ris);
		
		 if((ris!=null)&&(ris.equals("forno")))
		   {
			 NotificationCompat.Builder n  = new NotificationCompat.Builder(c)
			 .setContentTitle("Home manager")
			 .setContentText("Hi! I switch on oven at 50°.I saw that you are in your" +
			 		" favourite pizza's take away")
			 .setSmallIcon(android.R.drawable.ic_dialog_email);
			  
			         nm.notify(0, n.build());
				  
		   }
	   }
		 
}


