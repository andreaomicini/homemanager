package com.example.myapphomemanager;


import com.example.myapphomemanager.MyTask.LoginTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {

	private Button buttonLogin;
	//Context saved =this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
  
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progresso);//recupero la progress bar
        progressBar.setVisibility(View.INVISIBLE);//rendo la progress bar invisibile
        final Activity activity = this;
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editTextUsername = (EditText) findViewById(R.id.username);
                String username = editTextUsername.getText().toString();
                EditText editTextPassword = (EditText) findViewById(R.id.password);
                String password = editTextPassword.getText().toString();
                TextView textViewMessageToUser = (TextView) findViewById(R.id.messageToUser);
                LoginTask logTask = new LoginTask(progressBar, activity, textViewMessageToUser);
                //creo un nuovo login task con pr progress bar e act activity attuale
                if( (!username.isEmpty()) && (!password.isEmpty()) )
                    logTask.execute(username, password);
                else
                    textViewMessageToUser.setText("Insert username and password");
                //il cambio di activity avviene in LoginTask nel metodo onPostExecute

            }
        });

    }
    
   
          
   
        
}   
    

