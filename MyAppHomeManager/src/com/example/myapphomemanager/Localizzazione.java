package com.example.myapphomemanager;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Localizzazione extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.localizzazione);
	    
	    final TextView where = (TextView)findViewById(R.id.where);
	    final TextView latit = (TextView)findViewById(R.id.latitude);
	    final TextView longi = (TextView)findViewById(R.id.longitude);
	    
	    String[] result= (String[]) getIntent().getSerializableExtra("Servizi");
	    
	    if(result.equals(null))
	    {
	    	where.setText("Non è stato possibile rilevare la tua posizione");
	    }	
	    else
	    {
		    where.setText(result[0] + "\r\n " + result[1]);
		    
		    ArrayList<String> listaLuoghi = new ArrayList<String>();
	        int l=2;
	        int j=0;
	        while(j<result.length-2)
	        {
	            listaLuoghi.add(result[l]);
	            l++;
	            j++;
	        }
	        
	        
	        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaLuoghi);
	        ListView listView = (ListView)findViewById(R.id.preferitiListView);
	        listView.setAdapter(adapter);
	    }
	    
        String latitudine = (String)getIntent().getSerializableExtra("Latitudine");
        latit.setText(latitudine);
        String longitudine = (String)getIntent().getSerializableExtra("Longitudine");
        longi.setText(longitudine);
        
	    
	}

}
