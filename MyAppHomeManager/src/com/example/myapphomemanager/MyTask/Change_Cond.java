package com.example.myapphomemanager.MyTask;

import java.util.StringTokenizer;


import com.examples.model.CondizionatoreData;

import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.TucsonNodeService;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Change_Cond extends AsyncTask<CondizionatoreData, Void, CondizionatoreData> {

	
	
	EnhancedSynchACC acc; 
    TucsonAgentId agent;
    TucsonTupleCentreId tc;
    TucsonNodeService tcn;
    ToggleButton stato;
    SeekBar sb;
    TextView temp;
    
    public Change_Cond(ToggleButton state, SeekBar sb, TextView temp)
       {
    	  this.stato= state;
    	  this.sb= sb; 
    	  this.temp = temp;
    	  
       }
    
    @Override
    protected void onPreExecute() {
            // TODO Auto-generated method stub
          super.onPreExecute();
          sb.setVisibility(View.INVISIBLE);
          temp.setVisibility(View.INVISIBLE);
          Log.i("Inizio Condizionatore = ","OK" );
          
    }

    @Override
	protected CondizionatoreData doInBackground(CondizionatoreData... params) {
		
          CondizionatoreData c = params[0];
		
		try {
            this.tc = new TucsonTupleCentreId("infoTc", "10.0.2.2", "20504");
            Log.i("Centro: ", this.tc.toString());
            Log.i("Indirizzo rete: ", this.tc.getNode());
            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
            this.agent = new TucsonAgentId("changeAg");
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
                 String s= op.getLogicTupleResult().toString();
                 c.setStato_attuale(getStatoByString(s));
                 c.setTemp(getTemp(s));
            } else {
                Log.i("Risultato = ", "Male");
            }
            
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            Log.i("Creazione errore", "Non è stato possibile continuare");
        }
        return c;
	}
	
	
	@Override
	protected void onPostExecute(CondizionatoreData ris)
	   {
		 super.onPostExecute(ris);
		
		 if(ris!=null)
		   {
			 stato.setChecked(ris.isStato_attuale());
			 if(ris.isStato_attuale())
			    {
				  sb.setVisibility(View.VISIBLE);
				  sb.setProgress(ris.getTemp());
				  temp.setVisibility(View.VISIBLE);
				  temp.setText(ris.getTemp()+ "°C");
			    }
		   }
		 
	   }
	
private boolean getStatoByString(String s)
  {
	String s1 = s.substring(s.indexOf('(')+1, s.length()-2);
	StringTokenizer t= new StringTokenizer(s1,",");
	t.nextToken();
	String st= t.nextToken();
	if(st.equals("on")) return true;
	   else return false;
	
  }


private int getTemp(String s)
 {
	String s1 = s.substring(s.indexOf('(')+1, s.length()-1);
	StringTokenizer t= new StringTokenizer(s1,",");
	t.nextToken();
	t.nextToken();
	int  st= Integer.parseInt(t.nextToken());
	return st;
	
 }
	

}
