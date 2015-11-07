package com.example.myapphomemanager;

import com.example.myapphomemanager.MyTask.FornoTask;
import com.examples.model.DeviceData;
import com.examples.model.Utente;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Forno extends Activity {
	

		
		Context saved=this;
		/*connessione*/
		TucsonTupleCentreId centroTuple=null;
		TucsonAgentId agente=null;
		TucsonTupleCentreId centroTuple_room=null;
		TucsonAgentId agente_room=null;
		
		/*gestione politiche RBAC*/
		Utente ut;
		
		 @Override
		 protected void onCreate(Bundle savedInstanceState) 
		   {
			 super.onCreate(savedInstanceState);
		        setContentView(R.layout.forno);
		    final TextView aliasRisc = (TextView) findViewById(R.id.aliasForno);
		    
		    
		    DeviceData risData= (DeviceData) getIntent().getSerializableExtra("FornoObj");
		    ut = (Utente) getIntent().getSerializableExtra("Utente");
	        aliasRisc.setText(risData.getAlias());
	        
	        //passo da un capo all'altro il centro di tuple e l'agente
		    if(centroTuple==null && agente==null){
		    centroTuple= (TucsonTupleCentreId) getIntent().getSerializableExtra("CentroTuple");
		    agente= (TucsonAgentId) getIntent().getSerializableExtra("Agente");
		    }
		    
		    //passo da un capo all'altro il centro di tuple e l'agente
		    if(centroTuple_room==null && agente_room==null){
		    centroTuple_room= (TucsonTupleCentreId) getIntent().getSerializableExtra("CentroTuple_room");
		    agente_room= (TucsonAgentId) getIntent().getSerializableExtra("Agente_room");
		    }
		    
	        final SeekBar rilevatore= (SeekBar) findViewById(R.id.BarraTemperaturaForno);
	        final Activity currentActivity= this;
	        
	        final TextView valoreAttuale= (TextView) findViewById(R.id.TemperaturaSelezionataForno);
	         rilevatore.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					
					valoreAttuale.setText(Integer.toString(progress)+"°C");
					if(progress<=50) valoreAttuale.setTextColor(Color.BLUE);
					else if(progress>=255) valoreAttuale.setTextColor(Color.RED);
					   else  valoreAttuale.setTextColor(Color.GREEN);
					
				}
			});
	         
	         
	         final ToggleButton statoAttuale= (ToggleButton)findViewById(R.id.statoForno);
	         final TextView testo= (TextView) findViewById(R.id.TestoTempForno);
	         
	         //setto i dati del dispositivo in base a quello che è stato letto dal sistema
	       
	         if(!risData.isStato_attuale())
	             {
	        	     statoAttuale.setChecked(false);
	        	     rilevatore.setVisibility(View.INVISIBLE);
	                 valoreAttuale.setVisibility(View.INVISIBLE);
	                 testo.setVisibility(View.INVISIBLE);
	             }
	         
	         
	         else{
	        	 
	        	 statoAttuale.setChecked(true);
	        	 
	             testo.setVisibility(View.VISIBLE);
	             valoreAttuale.setText(risData.getTemp() +" °C");
	             rilevatore.setProgress(risData.getTemp());
	    	     rilevatore.setVisibility(View.VISIBLE);
	             valoreAttuale.setVisibility(View.VISIBLE);
	             
	         }
	         
	         
	         
	         statoAttuale.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if(isChecked)
					  {rilevatore.setVisibility(View.VISIBLE);
					   valoreAttuale.setVisibility(View.VISIBLE);
					   testo.setVisibility(View.VISIBLE);
					   }
					else {rilevatore.setVisibility(View.INVISIBLE);
					      valoreAttuale.setVisibility(View.INVISIBLE);
					      testo.setVisibility(View.INVISIBLE);
					}
					
				}
			});
	         
	         
	         /**SALVATAGGIO DI NUOVI DATI*/
	         
	         Button salva= (Button)findViewById(R.id.salvaForno);
	         salva.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean statoAggiornato= statoAttuale.isChecked();
					AlertDialog.Builder messaggio = new AlertDialog.Builder(saved);
					 if((rilevatore.getProgress()<50) &&(statoAggiornato))
					    {
						 messaggio.setMessage("Do you really wish to switch on the oven for this temperature?");
						  statoAttuale.setChecked(false);
					    }
					 else if((rilevatore.getProgress()>250) &&(statoAggiornato))
					    {
						   messaggio.setMessage("Isn't too hot?");
						   statoAttuale.setChecked(false);
					    }
					 else{
						 
						 
						 
						StringBuilder sb= new StringBuilder();
						sb.append("You changed oven \n\n");
						String tradotto;
					    if(statoAggiornato==true) tradotto="On";
					    else tradotto="Off";
						sb.append("Current state: "+tradotto+"\n");
						if(statoAggiornato) sb.append("Set temperature: "+ rilevatore.getProgress()+"°C\n");
						String mess=sb.toString();
						messaggio.setMessage(mess);
						String stat;
						FornoTask risc = new FornoTask(centroTuple, agente, currentActivity,centroTuple_room, agente_room,ut);
						
						if(statoAttuale.isChecked()) stat="on";
						  else stat= "off";
						
						String temp= ""+rilevatore.getProgress();
						
						risc.execute("save",stat,temp);
						
						
					   }
					 AlertDialog alert = messaggio.create();
					 alert.show();
				}
					
				
			});
	         
	}

		

	}
//	Context saved=this;
//	 @Override
//	 protected void onCreate(Bundle savedInstanceState) 
//	   {
//		 super.onCreate(savedInstanceState);
//	        setContentView(R.layout.forno);
//       final SeekBar rilevatore= (SeekBar) findViewById(R.id.BarraTemperaturaForno);
//       final TextView valoreAttuale= (TextView) findViewById(R.id.TemperaturaSelezionataForno);
//        rilevatore.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				
//				
//				valoreAttuale.setText(Integer.toString(progress)+"°C");
//				if(progress<=50) valoreAttuale.setTextColor(Color.BLUE);
//				else if(progress>=200) valoreAttuale.setTextColor(Color.RED);
//				   else  valoreAttuale.setTextColor(Color.GREEN);
//				
//			}
//		});
//        
//        
//        final ToggleButton statoAttuale= (ToggleButton)findViewById(R.id.statoForno);
//        final TextView testo= (TextView) findViewById(R.id.TestoTempForno);
//        statoAttuale.setChecked(false);
//        rilevatore.setVisibility(View.INVISIBLE);
//        valoreAttuale.setVisibility(View.INVISIBLE);
//        testo.setVisibility(View.INVISIBLE);
//        
//        statoAttuale.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				
//				if(isChecked)
//				  {rilevatore.setVisibility(View.VISIBLE);
//				   valoreAttuale.setVisibility(View.VISIBLE);
//				   testo.setVisibility(View.VISIBLE);
//				   }
//				else {rilevatore.setVisibility(View.INVISIBLE);
//				      valoreAttuale.setVisibility(View.INVISIBLE);
//				      testo.setVisibility(View.INVISIBLE);
//				}
//				
//			}
//		});
//        
//        Button salva= (Button)findViewById(R.id.salvaForno);
//        salva.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				boolean statoAggiornato= statoAttuale.isChecked();
//				AlertDialog.Builder messaggio = new AlertDialog.Builder(saved);
//				 if((rilevatore.getProgress()<50) &&(statoAggiornato))
//				    {
//					  messaggio.setMessage("Sei sicuro che serva accendere il forno ad una temperatura così bassa?");
//					  statoAttuale.setChecked(false);
//				    }
//				
//				 else{
//					StringBuilder sb= new StringBuilder();
//					sb.append("Hai modificato il tuo forno\n\n");
//					String tradotto;
//				    if(statoAggiornato==true) tradotto="On";
//				    else tradotto="Off";
//					sb.append("Stato attuale: "+tradotto+"\n");
//					if(statoAggiornato) sb.append("Temperatura impostata: "+ rilevatore.getProgress()+"°C\n");
//					String mess=sb.toString();
//					messaggio.setMessage(mess);
//									  
//				   }
//					AlertDialog alert = messaggio.create();
//				 alert.show();
//			}
//				
//			
//		});
        


