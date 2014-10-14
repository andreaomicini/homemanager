package com.example.myapphomemanager;


import com.example.myapphomemanager.MyTask.LoginTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;



public class MainActivity extends Activity {
	
	Context saved =this;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  
        Button entra= (Button) findViewById(R.id.login);
        final ProgressBar pr= (ProgressBar) findViewById(R.id.progresso);
        pr.setVisibility(View.INVISIBLE);
        final Activity act= this;
        entra.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText u= (EditText)findViewById(R.id.username);
				String user= u.getText().toString();
				EditText p= (EditText)findViewById(R.id.password);
				String pass= p.getText().toString();
				LoginTask logTask= new LoginTask(pr, act);
				logTask.execute(user,pass);
				
			}
		});
    }
    
   
          
   
        
}   
    

