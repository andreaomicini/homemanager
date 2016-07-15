//java -cp tucson.jar;2p.jar alice.tucson.service.TucsonNodeService
//cd C:\Users\Alessio\Desktop\TuCSoN
package com.example.myapphomemanager;




import com.example.myapphomemanager.MyTask.CondizionatoreTask;
import com.example.myapphomemanager.MyTask.FornoTask;
import com.example.myapphomemanager.MyTask.LocalizzazioneTask;
import com.example.myapphomemanager.MyTask.RiscaldaTask;
import com.examples.model.DeviceData;
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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MenuScelta extends AppCompatActivity implements
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{

	//Aggiunta da Alessio
	Toolbar toolbar;
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle actionBarDrawerToggle;
	NavigationView navigationView;
	FragmentTransaction fragmentTransaction;
	TextView usernameLoggatoText;
	TextView roleLoggatoText;

	int indiceItemSelezionato;
	
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
	        setContentView(R.layout.activity_main_modern);
	        final Activity att = this;


	        //TextView tv= (TextView) findViewById(R.id.alias);
	        risUtente= (Utente) getIntent().getSerializableExtra("Utente");
	        
	        //tv.setText("Hi, "+ risUtente.getUsername()+",what do you want to do?");*/

		 //Intent intent=getIntent();

		 toolbar=(Toolbar)findViewById(R.id.toolbar);
		 setSupportActionBar(toolbar);

		 drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
		 actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);

		 drawerLayout.addDrawerListener(actionBarDrawerToggle);

		 fragmentTransaction=getSupportFragmentManager().beginTransaction();
		 fragmentTransaction.add(R.id.main_container, HomeFragment.newInstance(risUtente));
		 fragmentTransaction.commit();
		 getSupportActionBar().setTitle("MyHomeManager");


		 //riconoscimento evento click su una voce del menu del navigation drawer
		 navigationView = (NavigationView) findViewById(R.id.navigation_view);

		 //Setto da codice l'header del navigation drawer
		 View headerView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
		 usernameLoggatoText=(TextView)headerView.findViewById(R.id.user_loggato);
		 //String usernameLoggato=intent.getStringExtra("Username");
		 //Log.i("USERNAME", usernameLoggato);
		 //if(usernameLoggato!=null)
		 String firstname=risUtente.getFirstname();
		 //tolgo gli apici
		 firstname=firstname.substring(1,firstname.length()-1);
		 usernameLoggatoText.setText(firstname);
		 roleLoggatoText=(TextView)headerView.findViewById(R.id.role_user_loggato);
		 if(risUtente.getRole().contains("A"))
		 	roleLoggatoText.setText("Admin");
		 else
			 roleLoggatoText.setText("Ordinary User");

	        
	       
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

		 navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			 @Override
			 public boolean onNavigationItemSelected(MenuItem item) {
				 //innanzitutto deseleziono la voce precedente deselezionando tutto

				 int size = navigationView.getMenu().size();
				 for (int i = 0; i < size; i++) {
					 navigationView.getMenu().getItem(i).setChecked(false);
				 }
				 switch (item.getItemId()) {
					 case R.id.home_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();
						 fragmentTransaction.add(R.id.main_container, HomeFragment.newInstance(risUtente));
						 fragmentTransaction.commit();
						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("MyHomeManager");
						 break;
					 case R.id.settings_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();
						 fragmentTransaction.add(R.id.main_container, HomeFragment.newInstance(risUtente));
						 fragmentTransaction.commit();
						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Impostazioni");
						 break;
					 case R.id.condizionatore_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();
						 try{
							 if(condiAgent==null){
								 condiAgent = new TucsonAgentId("condAg");
								 Log.i("Agente: ", condiAgent.toString());
							 }
						 }catch (Exception e){Log.i("Errore agente cond =","NO");}


						 try{
							 if(condiUpAgent==null){
								 condiUpAgent = new TucsonAgentId("conUpAg");
								 Log.i("Agente: ", condiUpAgent.toString());
							 }
						 }catch (Exception e){Log.i("ErrAgent condi sala_tc:","NO");}

						 CondizionatoreTask ct= new CondizionatoreTask(tc,condiAgent,sala_tc,condiUpAgent,risUtente,fragmentTransaction);
						 ct.execute("read");
						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Air Conditioner");
						 break;
					 case R.id.forno_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();

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

						 FornoTask rt= new FornoTask(tc,fornoAgent,cucina_tc,fornoUpAgent,risUtente,fragmentTransaction);
						 rt.execute("read");

						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Oven");
						 break;
					 case R.id.riscaldamento_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();

						 try{
							 if(riscaldamentoAgent==null){
								 riscaldamentoAgent = new TucsonAgentId("riscAg");
								 Log.i("Agente: ", riscaldamentoAgent.toString());
							 }
						 }catch (Exception e){Log.i("Errore agente risc=","NO");}


						 try{
							 if(riscaldaUpAgent==null){
								 riscaldaUpAgent = new TucsonAgentId("riscUpAg");
								 Log.i("Agente: ", riscaldaUpAgent.toString());
							 }
						 }catch (Exception e){Log.i("Err agent risc sala_tc","NO");}

						 RiscaldaTask riscaldaTaskt= new RiscaldaTask(tc,riscaldamentoAgent,sala_tc,riscaldaUpAgent,risUtente,fragmentTransaction);
						 riscaldaTaskt.execute("read");

						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Heating");
						 break;
					 case R.id.geoloc_id:
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();

						 getCurrentLocation();

						 /*PER PROVARE INSERISCO IO LE COORDIANTE
						 if(location==null)
						 {
							 Toast.makeText(saved, "Non ci sono coordinate", Toast.LENGTH_SHORT).show();
						 }
						 else
						 {*/
							 //coordinate da utilizzare(lat 44.697994, lon 10.522876)
							 /*String s=""+location.getLatitude();
							 String str= s.substring(0,s.length()-7);
							 float lat = Float.parseFloat(str);
							 2 prova lat=44.508854 long=11.344801 via domenico zampieri bologna*/
							 float lat = Float.parseFloat("44.697994");
							 //float lat = Float.parseFloat("44.508854");
							 /*s =""+location.getLongitude();
							 str= s.substring(0,s.length()-1);
							 float lon = Float.parseFloat(str);*/
							 float lon = Float.parseFloat("10.522876");
							 //float lon = Float.parseFloat("11.344801");
							 Toast.makeText(saved, "C: "+lat+" "+lon, Toast.LENGTH_LONG).show();

							 LocalizzazioneTask loc = new LocalizzazioneTask(saved, notificationManager, risUtente.getFirstname(),fragmentTransaction);
							 loc.execute(lat, lon);
							 //LocationAsync la= new LocationAsync(notificationManager,saved);
							 //la.execute(lat,lon);


						 //}
						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Research Services");
						 break;
					 case R.id.log_out:
						 //LOGOUT
						 fragmentTransaction = getSupportFragmentManager().beginTransaction();
						 fragmentTransaction.add(R.id.main_container, new LogoutFragment());
						 fragmentTransaction.commit();
						 item.setChecked(true);
						 drawerLayout.closeDrawers();
						 getSupportActionBar().setTitle("Logout");
						 break;
				 }

				 //Toast.makeText(getApplicationContext(),"Cliccato"+item.getItemId(),Toast.LENGTH_SHORT).show();
				 return false;
			 }
		 });

	        
	       
       }
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
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
