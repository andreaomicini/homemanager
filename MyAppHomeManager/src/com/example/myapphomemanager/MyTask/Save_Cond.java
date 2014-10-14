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
import android.os.AsyncTask;
import android.util.Log;


import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Save_Cond extends AsyncTask<String, Void, Integer> {

	
	
	EnhancedSynchACC acc; 
    TucsonAgentId agent;
    TucsonTupleCentreId tc;
    TucsonNodeService tcn;
    ToggleButton stato;
    SeekBar sb;
    TextView temp;
    
    public Save_Cond(ToggleButton state, SeekBar sb, TextView temp)
       {
    	  this.stato= state;
    	  this.sb= sb; 
    	  this.temp = temp;
    	  
       }
    
    @Override
    protected void onPreExecute() {
            // TODO Auto-generated method stub
          super.onPreExecute();
          
          Log.i("Inizio Salvataggio Condizionatore = ","OK" );
          
    }


	@Override
	protected Integer doInBackground(String... params) {
		
		int ris= -1;
		 boolean stato_nuovo = Boolean.valueOf(params[0]);
		 int temp_nuova = 0;
		
		 if(stato_nuovo)
		    {
			   temp_nuova = Integer.parseInt(params[1]);
		    }
			
			try {
	            this.tc = new TucsonTupleCentreId("infoTc", "10.0.2.2", "20504");
	            Log.i("Centro: ", this.tc.toString());
	            Log.i("Indirizzo rete: ", this.tc.getNode());
	            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
	            this.agent = new TucsonAgentId("saveAg");
	            Log.i("Agente: ", this.agent.toString());
	            this.acc = TucsonMetaACC.getContext(this.agent, "10.0.2.2",
	                    20504);
	            Log.i("Acc: ", this.acc.toString());
	           
	            final LogicTuple arr = LogicTuple.parse("device_state(1," + new Var("X") + "," + new Var("Y")
	                    + ")");
	            Log.i("Arr = ", arr.toString());
	            final ITucsonOperation op = this.acc.inp(this.tc, arr,
	                    Long.MAX_VALUE);
	           
	            if (op.isResultSuccess()) {
	                Log.i("Risulato = ", "Bene");
	                 
	                 String strstato;
	                 if(stato_nuovo)
	                   {
	                	  strstato="on";
	                   }
	                 else strstato="off";
	                 String t= ""+temp_nuova;
	                 
	                 final LogicTuple arr2 = LogicTuple.parse("device_state(1," + new Value(strstato) + "," + new Value(t)
	                    + ")"); 
	                 final ITucsonOperation op2 = this.acc.out(this.tc, arr2,
	 	                    null);
	 	           
	 	            if (op2.isResultSuccess()) {
	 	                Log.i("Risulato = ", "Bene");
	 	                ris= 1;
	 	            }else { Log.i("Risultato = ", "Male");}
	                 
//	                 c.setStato_attuale(getStatoByString(s));
//	                 c.setTemp(getTemp(s));
	            } else {
	                Log.i("Risultato = ", "Male");
	            }
	            acc.exit();
	           		
	            		
	            		
	            
	        } catch (final Exception e) {
	            // TODO Auto-generated catch block
	            Log.i("Creazione errore", "Non è stato possibile continuare");
	        }
	        return ris;
	}
	
	@Override
	protected void onPostExecute(Integer ris)
	   {
		 super.onPostExecute(ris);
		
		 if(ris>0)
		   {
			  
			  Log.i("Fine", "Ok");
		   }
		 
	   }

}
