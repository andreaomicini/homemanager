package com.example.myapphomemanager.MyTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.myapphomemanager.Costanti;
import com.example.myapphomemanager.Localizzazione;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.TucsonNodeService;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

public class LocalizzazioneTask extends AsyncTask<Float, Void, String[]>{

	EnhancedSynchACC acc; 
    TucsonAgentId agent;
    TucsonTupleCentreId tc, listaTc;
    TucsonNodeService tcn;
    NotificationManager nm;
    Context c;
    Activity act;
    Geocoder geo = null;
    String nomeUtente;
    
    float lat, lon;
    
    public LocalizzazioneTask(Activity a, Context ct, NotificationManager manager, String nome)
    {
    	  nm = manager;
    	  this.act = a;
    	  this.c = ct;
    	  this.nomeUtente = nome;
    }
    @Override
    protected void onPreExecute() {
            // TODO Auto-generated method stub
          super.onPreExecute();
          geo= new Geocoder(c, Locale.getDefault());
      	
          Log.i("Rilevo locazione= ","OK" );
          
    }
    
	@Override
	protected String[] doInBackground(Float... params) {
		
		
		lat=params[0] ;
		lon= params[1];
		String[] listaResult=null;
		
		try {
            this.tc = new TucsonTupleCentreId("infoTc", Costanti.getIndirizzo(), "20504");
            Log.i("Centro: ", this.tc.toString());
            Log.i("Indirizzo rete: ", this.tc.getNode());
            Log.i("Porta: ", String.valueOf(this.tc.getPort()));
            this.agent = new TucsonAgentId("locAg");
            Log.i("Agente: ", this.agent.toString());
            this.acc = TucsonMetaACC.getContext(this.agent,Costanti.getIndirizzo(),20504);
            Log.i("Acc: ", this.acc.toString());
           
            //deposito la tupla per la localizzazione lat e lon
            Value longVal= new Value(lon);
            Log.i("Long=",longVal.toString());
            Value latVal= new Value(lat);
            final LogicTuple arr = LogicTuple.parse("geo_position("+ this.nomeUtente+",'" + latVal + "','"+ longVal+ "')");
            Log.i("Arr = ", arr.toString());
            final ITucsonOperation op = this.acc.out(this.tc, arr,Long.MAX_VALUE);
           
            if (op.isResultSuccess()) {
                Log.i("Risulato = ", "Bene");
                     
            } else {
                Log.i("Risultato = ", "Male");
            }
            
            /*recupero i servizi*/
            Thread.sleep(10000);
            listaTc = new TucsonTupleCentreId("serviziTc", Costanti.getIndirizzo(), "20504");
            LogicTuple arr1 = LogicTuple.parse("servizio("+ new Var("Z")+")");
            Log.i("Servizio= ", arr1.toString());
            final ITucsonOperation op1 = this.acc.rdAll(listaTc, arr1, Long.MAX_VALUE);
           
            
            if (op1.isResultSuccess()) 
            {
            	Log.i("Risulato = ", "Bene");
            	List<LogicTuple> lista=op1.getLogicTupleListResult();
            	//String[] listaResult=new String[lista.size()+2];
            	listaResult = new String[lista.size()+2];
            	
            	List<Address> addresses = null;
                
            	try
                {
                    addresses = geo.getFromLocation(lat, lon, 1);
                }
                catch(IOException e)
                {
     
                }
     
                if(addresses != null)
                {
                    if(addresses.isEmpty())
                    {
                    	return null;
                    }
                    else 
                    {
                    	if(addresses.size()> 0)
                        {
                            Address tmp = addresses.get(0);
                            int y = 0;
     
                            while(y<tmp.getMaxAddressLineIndex())
                            {
                                listaResult[y] = tmp.getAddressLine(y);
                                y++;
                            }
                        }
                    	
                        int i=0;
                        int l=2;
                        while(i<lista.size())
                        {
                            String letto = lista.get(i).toString();
                            listaResult[l] = letto.substring(letto.indexOf('(')+1,letto.length()-1);;
                            i++;
                            l++;
                        }
     
                       // return listaResult;
                    }
                   
                }
                
                this.acc.exit();
                
                if(listaResult.length>0)
                	return listaResult;
	        } 
            else 
	        {
	        	Log.i("Risultato = ", "Male");
	        	return null;
	        }
        
            
         
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            Log.i("Creazione errore", "Non è stato possibile continuare");
        }
		return listaResult;
		
	}
	
	@Override
	protected void onPostExecute(String[] ris)
	{
		super.onPostExecute(ris);
		
		//NotificationCompat.Builder n  = new NotificationCompat.Builder(c).setContentTitle("Home manager").setContentText("Hi! I switch on oven at 50°.I saw that you are in your" +" favourite pizza's take away").setSmallIcon(android.R.drawable.ic_dialog_email);
		//nm.notify(0, n.build());
			
		Intent inte= new Intent(act, Localizzazione.class);
		
		inte.putExtra("Servizi", ris);
		inte.putExtra("Latitudine", String.valueOf(lat));
		inte.putExtra("Longitudine", String.valueOf(lon));

		act.startActivity(inte);
	}

}
