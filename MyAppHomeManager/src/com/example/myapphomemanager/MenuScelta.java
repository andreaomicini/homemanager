package com.example.myapphomemanager;




import com.example.myapphomemanager.MyTask.CondizionatoreTask;
import com.example.myapphomemanager.MyTask.FornoTask;
import com.example.myapphomemanager.MyTask.LocalizzazioneTask;
import com.example.myapphomemanager.MyTask.RiscaldaTask;
import com.examples.model.Utente;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MenuScelta extends Activity implements
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{
	
	/*per la gestione delle notifiche*/
     private NotificationManager notificationManager;
     /*per la gestione della posizione*/
      private Location location;
      private LocationManager lManager;
      private LocationListener lListener;
      Context saved = this; 
      String nome;
      
      /*per la gestione del centro di tuple*/
     TucsonTupleCentreId tc=null;
     TucsonTupleCentreId sala_tc = null;
     TucsonTupleCentreId cucina_tc = null;
     
     
     /*per la gestione degli agenti*/
    TucsonAgentId riscaldamentoAgent=null;
    TucsonAgentId fornoAgent=null;
    TucsonAgentId fornoUpAgent = null;
    TucsonAgentId riscaldaUpAgent = null;
    TucsonAgentId condiAgent = null;
    TucsonAgentId condiUpAgent = null;
    
     /*per la gestione delle politiche RBAC*/
    Utente risUtente;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.menu);
	        final Activity att = this;
	        TextView tv= (TextView) findViewById(R.id.alias);
	        risUtente= (Utente) getIntent().getSerializableExtra("Utente");
	        
	        tv.setText("Hi, "+ risUtente.getUsername()+",what do you want to do?");
	        
	       
	        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	        
	        lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	        
	        location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		
	        try {
	        	if(tc==null){
				this.tc = new TucsonTupleCentreId("db",Costanti.indirizzoIp, "20504");
				Log.i("Centro: ", this.tc.toString());
		        Log.i("Indirizzo rete: ", this.tc.getNode());
		        Log.i("Porta: ", String.valueOf(this.tc.getPort()));
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        try {
	        	if(sala_tc==null){
				this.sala_tc = new TucsonTupleCentreId("sala_tc",Costanti.indirizzoIp, "20504");
				Log.i("Centro: ", this.sala_tc.toString());
		        Log.i("Indirizzo rete: ", this.sala_tc.getNode());
		        Log.i("Porta: ", String.valueOf(this.sala_tc.getPort()));
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	        
	        try {
	        	if(cucina_tc==null){
				this.cucina_tc = new TucsonTupleCentreId("cucina_tc",Costanti.indirizzoIp, "20504");
				Log.i("Centro: ", this.cucina_tc.toString());
		        Log.i("Indirizzo rete: ", this.cucina_tc.getNode());
		        Log.i("Porta: ", String.valueOf(this.cucina_tc.getPort()));
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	        /**CLICK LOCALIZZAMI*/
	        ImageButton bottLoc= (ImageButton) findViewById(R.id.localizza);
	        bottLoc.setOnClickListener(new OnClickListener() {
				
	        	@Override
	        	public void onClick(View v) {
					getCurrentLocation();
					
	        		
					if(location==null) 
					{
						Toast.makeText(saved, "non ci sono coordinate", Toast.LENGTH_SHORT).show();
					}
					else
					{
						String s=""+location.getLatitude();
						String str= s.substring(0,s.length()-7);
						float lat = Float.parseFloat(str);
						s =""+location.getLongitude();
						str= s.substring(0,s.length()-1);
						float lon = Float.parseFloat(str);
					
						LocalizzazioneTask loc = new LocalizzazioneTask(att, saved, notificationManager, nome);
						loc.execute(lat, lon);
						//LocationAsync la= new LocationAsync(notificationManager,saved);
						//la.execute(lat,lon);
					
					
					}
	        	}
			});
	       
	        /**CLICK CONDIZIONATORE*/
	        
	        ImageButton b = (ImageButton) findViewById(R.id.condizionatoreBotton);
	        b.setOnClickListener(new View.OnClickListener() {
	        	
	        
	        	//inizializzo l'agente e il centro di tuple


				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				
				try{
				if(condiAgent==null){
				Toast.makeText(saved, "condizionatore nullo", Toast.LENGTH_LONG).show();
	            condiAgent = new TucsonAgentId("condAg");
	            Log.i("Agente: ", condiAgent.toString());
				}
				}catch (Exception e){Log.i("Errore agente cond =","NO");}
				
				
				try{
					if(condiUpAgent==null){
					Toast.makeText(saved, "condizionatore nullo", Toast.LENGTH_LONG).show();
		            condiUpAgent = new TucsonAgentId("conUpAg");
		            Log.i("Agente: ", condiUpAgent.toString());
					}
					}catch (Exception e){Log.i("Errore agente condi sala_tc =","NO");}
				
				CondizionatoreTask ct= new CondizionatoreTask(tc,condiAgent,att,sala_tc,condiUpAgent,risUtente);
				ct.execute("read");
				}	
	        });
	 
	        
		 
	
	        
	       
	        /**CLICK RISCALDAMENTO*/
	        
	        ImageButton bottRiscaldamento= (ImageButton) findViewById(R.id.riscaldamentoBotton);
	        bottRiscaldamento.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//inizializzo l'agente e il centro di tuple
					
					try{
					if(riscaldamentoAgent==null){
					Toast.makeText(saved, "riscaldamento nullo", Toast.LENGTH_LONG).show();
		            riscaldamentoAgent = new TucsonAgentId("riscAg");
		            Log.i("Agente: ", riscaldamentoAgent.toString());
					}
					}catch (Exception e){Log.i("Errore agente risc=","NO");}
					
					
					try{
						if(riscaldaUpAgent==null){
						Toast.makeText(saved, "riscaldamento nullo", Toast.LENGTH_LONG).show();
			            riscaldaUpAgent = new TucsonAgentId("riscUpAg");
			            Log.i("Agente: ", riscaldaUpAgent.toString());
						}
						}catch (Exception e){Log.i("Errore agente risc sala_tc =","NO");}
					
					RiscaldaTask rt= new RiscaldaTask(tc,riscaldamentoAgent,att,sala_tc,riscaldaUpAgent,risUtente);
					rt.execute("read");
					
					
				}
			});
	        
	        /**CLICK FORNO*/
	       
	        ImageButton bottForno= (ImageButton) findViewById(R.id.fornoBottone);
	        bottForno.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//startActivity(new Intent(MenuScelta.this,Forno.class));
					
					try{
						if(fornoAgent==null){
						
			            fornoAgent = new TucsonAgentId("fornoAg");
			            Log.i("Agente: ", fornoAgent.toString());
						}
						if(fornoUpAgent==null){
							
				            fornoUpAgent = new TucsonAgentId("fornoUpAg");
				            Log.i("Agente: ", fornoUpAgent.toString());
							}
						}catch (Exception e){Log.i("Errore agente forno=","NO");}
						
						FornoTask rt= new FornoTask(tc,fornoAgent,att,cucina_tc,fornoUpAgent,risUtente);
						rt.execute("read");
						
						
				}
			});
	        
	       
       }

	 


	
		 private void getCurrentLocation() {
	         
	         
	         lListener = new LocationListener() {
	              
	             @Override
	             public void onStatusChanged(String provider, int status, Bundle extras) {
	                 // TODO Auto-generated method stub
	             }
	              
	             @Override
	             public void onProviderEnabled(String provider) {
	                 // TODO Auto-generated method stub
	             }
	              
	             @Override
	             public void onProviderDisabled(String provider) {
	                 // TODO Auto-generated method stub
	             }
	              
	             @Override
	             public void onLocationChanged(Location loc) {
	                 // TODO Auto-generated method stub
	                 location = loc;
	             }
	         };
	         lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);
	     }
	

	
	
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onConnected(Bundle connectionHint)
		{
			
		}
	
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onConnectionSuspended(int arg0) {
			// TODO Auto-generated method stub
			
		}
	 }
