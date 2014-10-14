package com.example.myapphomemanager;

import com.example.myapphomemanager.MyTask.Change_Cond;
import com.examples.model.CondizionatoreData;


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
         
         CondizionatoreData d= new CondizionatoreData(1, nome, 20);
         
         Change_Cond condTask= new Change_Cond(statoAttuale, rilevatore, valoreAttuale);
        
         condTask.execute(d);
         
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
					sb.append("You changed air conditioner\n\n");
					String tradotto;
				    if(statoAggiornato==true) tradotto="On";
				    else tradotto="Off";
					sb.append("Current status: "+tradotto+"\n");
					if(statoAggiornato) sb.append("Set temperature: "+ rilevatore.getProgress()+"°C\n");
					String mess=sb.toString();
					messaggio.setMessage(mess);
									  
				   }
					AlertDialog alert = messaggio.create();
				 alert.show();
			}
				
			
		});
          
}

	

	        
	   }


