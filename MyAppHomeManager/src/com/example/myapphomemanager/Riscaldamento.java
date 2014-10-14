package com.example.myapphomemanager;

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

public class Riscaldamento extends Activity {
	
	Context saved=this;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	   {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.riscaldamento);
        final SeekBar rilevatore= (SeekBar) findViewById(R.id.BarraTemperaturaRisc);
        final TextView valoreAttuale= (TextView) findViewById(R.id.TemperaturaSelezionataRisc);
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
         
         
         final ToggleButton statoAttuale= (ToggleButton)findViewById(R.id.statoRiscaldamento);
         final TextView testo= (TextView) findViewById(R.id.TestoTempRisc);
         statoAttuale.setChecked(false);
         rilevatore.setVisibility(View.INVISIBLE);
         valoreAttuale.setVisibility(View.INVISIBLE);
         testo.setVisibility(View.INVISIBLE);
         
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
         
         Button salva= (Button)findViewById(R.id.salvaRiscaldamento);
         salva.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean statoAggiornato= statoAttuale.isChecked();
				AlertDialog.Builder messaggio = new AlertDialog.Builder(saved);
				 if((rilevatore.getProgress()<18) &&(statoAggiornato))
				    {
					  messaggio.setMessage("Sei sicuro che serva accendere il riscaldamento per una temperatura così bassa?");
					  statoAttuale.setChecked(false);
				    }
				 else if((rilevatore.getProgress()>30) &&(statoAggiornato))
				    {
					   messaggio.setMessage("Non c'è abbastanza caldo?");
					   statoAttuale.setChecked(false);
				    }
				 else{
					StringBuilder sb= new StringBuilder();
					sb.append("Hai modificato il tuo riscaldamento\n\n");
					String tradotto;
				    if(statoAggiornato==true) tradotto="On";
				    else tradotto="Off";
					sb.append("Stato attuale: "+tradotto+"\n");
					if(statoAggiornato) sb.append("Temperatura impostata: "+ rilevatore.getProgress()+"°C\n");
					String mess=sb.toString();
					messaggio.setMessage(mess);
									  
				   }
					AlertDialog alert = messaggio.create();
				 alert.show();
			}
				
			
		});
         
}

	

}
