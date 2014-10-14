package com.example.myapphomemanager;




import com.example.myapphomemanager.MyTask.LocationAsync;
import com.example.myapphomemanager.MyTask.SetCond;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MenuScelta extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	  private LocationManager lManager;
      private LocationListener lListener;
     private NotificationManager notificationManager;
   //   private LocationClient lc = new LocationClient(this,this,this);
      private Location location;
      Context saved = this; 
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	   {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.menu);
	        final Activity att = this;
	        TextView tv= (TextView) findViewById(R.id.alias);
	        String nome= getIntent().getExtras().getString("Utente");
	        tv.setText("Hi, "+nome+",what do you want to do?");
	       
	        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	        
	        lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	      		
	        
	        Button bottLoc= (Button) findViewById(R.id.localizza);
	        bottLoc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getCurrentLocation();
					
					if(location==null) 
						{
						 Toast.makeText(saved, "non ci sono coordinate", Toast.LENGTH_SHORT).show();
						}
					else {String s=""+location.getLatitude();
					      String str= s.substring(0,s.length()-10);
					      double lat = Double.parseDouble(str);
					      s =""+location.getLongitude();
					      str= s.substring(0,s.length()-10);
					      double lon = Double.parseDouble(str);
	 					
						Toast.makeText(saved, String.valueOf(lat), Toast.LENGTH_SHORT).show();
						Toast.makeText(saved, String.valueOf(lon), Toast.LENGTH_SHORT).show();
							LocationAsync la= new LocationAsync(notificationManager,saved);
							la.execute(lat,lon);
						
					
				}
				}
			});
	       
	        ImageButton b = (ImageButton) findViewById(R.id.condizionatoreBotton);
	        b.setOnClickListener(new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
					
	        		SetCond setCondTask= new SetCond(att);
					setCondTask.execute();	
	        		
					  
				}
			});
	        
	       
	        
	        ImageButton bottRiscaldamento= (ImageButton) findViewById(R.id.riscaldamentoBotton);
	        bottRiscaldamento.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuScelta.this,Riscaldamento.class));
					
				}
			});
	       
	        ImageButton bottForno= (ImageButton) findViewById(R.id.fornoBottone);
	        bottForno.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MenuScelta.this,Forno.class));
					
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
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	 }
