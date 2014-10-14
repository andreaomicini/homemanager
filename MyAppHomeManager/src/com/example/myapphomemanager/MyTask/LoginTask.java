package com.example.myapphomemanager.MyTask;

import java.util.StringTokenizer;

import com.example.myapphomemanager.MenuScelta;
import com.examples.model.Utente;

import alice.logictuple.LogicTuple;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, Utente> {

	
	
	ProgressBar p;
	   Activity act;
	   EnhancedSynchACC acc; 
	    TucsonAgentId agent;
	    TucsonTupleCentreId tc;
	    TucsonNodeService tcn;
	  
	   
		public LoginTask(ProgressBar b,Activity a)
		   {
			 p = b;
			 act = a;
		   }
		
		
		 @Override
	     protected void onPreExecute() {
	             // TODO Auto-generated method stub
	           super.onPreExecute();
	           p.setVisibility(View.VISIBLE); 
	           Log.i("Inizio = ","OK" );
	           
	     }
		 
		 @Override
			protected Utente doInBackground(String... params) {
				
			 Utente ut= null;
			 
			 
			 final String u = params[0];
		     final String p = params[1];
		        
		        try {
		            this.tc = new TucsonTupleCentreId("infoTc", "10.0.2.2", "20504");
		            Log.i("Centro: ", this.tc.toString());
		            Log.i("Indirizzo rete: ", this.tc.getNode());
		            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
		            this.agent = new TucsonAgentId("mobileAg");
		            Log.i("Agente: ", this.agent.toString());
		            this.acc = TucsonMetaACC.getContext(this.agent, "10.0.2.2",
		                    20504);
		            Log.i("Acc: ", this.acc.toString());
		            final LogicTuple arr = LogicTuple.parse("utente(X," + u + "," + p
		                    + ")");
		            Log.i("Arr = ", arr.toString());
		            final ITucsonOperation op = this.acc.rdp(this.tc, arr,
		                    Long.MAX_VALUE);
		           
		            if (op.isResultSuccess()) {
		                Log.i("Risulato = ", "Bene");
		                ut = createUserByString(op.getLogicTupleResult().toString());
		            } else {
		                Log.i("Risultato = ", "Male");
		            }
		           
		        } catch (final Exception e) {
		            // TODO Auto-generated catch block
		            Log.i("Creazione errore", "Non è stato possibile continuare");
		        }
		        return ut;
			}



		
		@Override
		protected void onPostExecute(Utente ris)
		   {
			 super.onPostExecute(ris);
			 p.setVisibility(View.INVISIBLE);
			 if(ris!=null)
			   {
				 Intent inte= new Intent(act,MenuScelta.class);
				 inte.putExtra("Utente", ris.getUsername());
				 
				act.startActivity(inte);
			   }
			else Toast.makeText(act, "Credenziali non corrette", Toast.LENGTH_SHORT).show();
			 
		   }

		private Utente createUserByString (String s)

		{    
		    Utente u=null;
			String s1=s.substring(s.indexOf("(")+1,s.length()-2);
			StringTokenizer t= new StringTokenizer(s1,",");
			if(t.countTokens()==3)
			{
				int idUser= Integer.parseInt(t.nextToken());
				String username=(t.nextToken()).trim();
				String pwd=(t.nextToken()).trim();
                u= new Utente(idUser,username,pwd);
			}
           return u;

		}
		
}
