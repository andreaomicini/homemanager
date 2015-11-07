package com.example.myapphomemanager;


import com.example.myapphomemanager.MyTask.CondizionatoreTask;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;

import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Condizionatore extends Activity {
	
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
	        setContentView(R.layout.air_conditioner);
	        
	        
         final SeekBar rilevatore= (SeekBar) findViewById(R.id.BarraTemperatura);
         final TextView valoreAttuale= (TextView) findViewById(R.id.TemperaturaSelezionata);
         final ToggleButton statoAttuale= (ToggleButton)findViewById(R.id.statoCondizionatore);
         final TextView aliasCondizionatore= (TextView) findViewById(R.id.aliasCondizionatore);
         String nome= getIntent().getExtras().getString("Alias");
         aliasCondizionatore.setText(nome);
         
   
         
 	    DeviceData risData= (DeviceData) getIntent().getSerializableExtra("CondizionatoreObj");
 	    ut = (Utente) getIntent().getSerializableExtra("Utente"); 
        aliasCondizionatore.setText(risData.getAlias());
        
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
         
	    
	    final Activity currentActivity= this;
	    
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
				if(progress<=12) valoreAttuale.setTextColor(Color.BLUE);
				else if(progress>=27) valoreAttuale.setTextColor(Color.RED);
				   else  valoreAttuale.setTextColor(Color.GREEN);
				
			}
		});
          
          
         
         final TextView testo= (TextView) findViewById(R.id.TestoTemp);
          
          
          
          
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
          
          Button salva= (Button)findViewById(R.id.salvaCondizionatore);
          salva.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean statoAggiornato= statoAttuale.isChecked();
				AlertDialog.Builder messaggio = new AlertDialog.Builder(saved);
				 if((rilevatore.getProgress()<15) &&(statoAggiornato))
				    {
					  messaggio.setMessage("Isn't it a bit too cold?");
					  statoAttuale.setChecked(false);
				    }
				 else if((rilevatore.getProgress()>28) &&(statoAggiornato))
				    {
					   messaggio.setMessage("Do you really wish to switch on the air conditioner for this temperature?");
					   statoAttuale.setChecked(false);
				    }
				 else{
					 
					 
					 
						StringBuilder sb= new StringBuilder();
						sb.append("You changed air conditioner \n\n");
						String tradotto;
					    if(statoAggiornato==true) tradotto="On";
					    else tradotto="Off";
						sb.append("Current state: "+tradotto+"\n");
						if(statoAggiornato) sb.append("Set temperature: "+ rilevatore.getProgress()+"°C\n");
						String mess=sb.toString();
						messaggio.setMessage(mess);
						String stat;
						CondizionatoreTask risc = new CondizionatoreTask(centroTuple, agente, currentActivity,centroTuple_room,agente_room,ut);
						
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


