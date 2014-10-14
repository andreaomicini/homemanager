package com.example.myapphomemanager.MyTask;

import java.util.StringTokenizer;

import com.example.myapphomemanager.Condizionatore;
import com.examples.model.CondizionatoreData;

import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.TucsonNodeService;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import android.widget.Toast;



public class SetCond extends AsyncTask <Void, Void, CondizionatoreData> {
	EnhancedSynchACC acc; 
    TucsonAgentId agent;
    TucsonTupleCentreId tc;
    TucsonNodeService tcn;
    Activity act;
    
    public SetCond(Activity a)
       {
    	   act = a;
       }
	
	
	
	 @Override
  protected void onPreExecute() {
          // TODO Auto-generated method stub
        super.onPreExecute();
        
        Log.i("Inizio Condizionatore = ","OK" );
        
  }
	 
	
	@Override
	protected CondizionatoreData doInBackground(Void... params) {
		
		
		CondizionatoreData c = null;
		
		try {
            this.tc = new TucsonTupleCentreId("infoTc", "10.0.2.2", "20504");
            Log.i("Centro: ", this.tc.toString());
            Log.i("Indirizzo rete: ", this.tc.getNode());
            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
            this.agent = new TucsonAgentId("condizAg");
            Log.i("Agente: ", this.agent.toString());
            this.acc = TucsonMetaACC.getContext(this.agent, "10.0.2.2",
                    20504);
            Log.i("Acc: ", this.acc.toString());
            final LogicTuple arr = LogicTuple.parse("device(1," + new Var("X") + "," + new Var("Y")
                    + ")");
            Log.i("Arr = ", arr.toString());
            final ITucsonOperation op = this.acc.inp(this.tc, arr,
                    Long.MAX_VALUE);
           
            if (op.isResultSuccess()) {
                Log.i("Risulato = ", "Bene");
                c = createConditionerByString(op.getLogicTupleResult().toString());
                this.acc.out(this.tc, arr,Long.MAX_VALUE);
            } else {
                Log.i("Risultato = ", "Male");
            }
            
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            Log.i("Creazione errore", "Non Ã¨ stato possibile continuare");
        }
        return c;
	}
	
	
	@Override
	protected void onPostExecute(CondizionatoreData ris)
	   {
		 super.onPostExecute(ris);
		
		 if(ris!=null)
		   {
			 Intent inte= new Intent(act,Condizionatore.class);
			 inte.putExtra("Alias", ris.getAlias());
			 inte.putExtra("Stato", ris.isStato_attuale());
			 if(ris.getTemp()!=0)
			    {
				   inte.putExtra("temp", ris.getTemp());
			    }
			act.startActivity(inte);
		   }
		else Toast.makeText(act, "Non è stato trovato nessun condizionatore", Toast.LENGTH_SHORT).show();
		 
	   }

	private CondizionatoreData createConditionerByString (String s)

	{    
	    CondizionatoreData u=null;
		String s1=s.substring(s.indexOf("(")+1,s.length()-2);
		StringTokenizer t= new StringTokenizer(s1,",");
		if(t.countTokens()==3)
		{
			int id= Integer.parseInt(t.nextToken());
			String alias=(t.nextToken()).trim();
			float consumo=Float.parseFloat(t.nextToken().trim());
            u= new CondizionatoreData (id,alias,consumo);
		}
       return u;

	}

}
