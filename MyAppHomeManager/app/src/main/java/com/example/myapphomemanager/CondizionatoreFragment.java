package com.example.myapphomemanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myapphomemanager.MyTask.CondizionatoreTask;
import com.examples.model.DeviceData;
import com.examples.model.Utente;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

/**
 * Created by Alessio on 30/05/2016.
 */
public class CondizionatoreFragment extends Fragment {



    /*connessione*/
    TucsonTupleCentreId centroTuple=null;
    TucsonAgentId agente=null;
    TucsonTupleCentreId centroTuple_room=null;
    TucsonAgentId agente_room=null;

    private Utente utente;

    private View myView;
    private DeviceData condizionatore;


    public CondizionatoreFragment(){}

    public static CondizionatoreFragment newInstance(DeviceData condizionatore,TucsonTupleCentreId tc,TucsonAgentId agent,TucsonTupleCentreId device_room_tc,TucsonAgentId condiAgent,Utente utente){
        CondizionatoreFragment fragment = new CondizionatoreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CentroTuple", tc);
        bundle.putSerializable("Agente", agent);
        bundle.putSerializable("CentroTuple_room", device_room_tc);
        bundle.putSerializable("Agente_room", condiAgent);
        bundle.putSerializable("Utente", utente);
        bundle.putSerializable("Condizionatore",condizionatore);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DeviceData condizionatore= (DeviceData) getArguments().getSerializable("Condizionatore");
        utente = (Utente) getArguments().getSerializable("Utente");

        //passo da un capo all'altro il centro di tuple e l'agente
        if(centroTuple==null && agente==null){
            centroTuple= (TucsonTupleCentreId) getArguments().getSerializable("CentroTuple");
            agente= (TucsonAgentId) getArguments().getSerializable("Agente");
        }

        //passo da un capo all'altro il centro di tuple e l'agente
        if(centroTuple_room==null && agente_room==null){
            centroTuple_room= (TucsonTupleCentreId) getArguments().getSerializable("CentroTuple_room");
            agente_room= (TucsonAgentId) getArguments().getSerializable("Agente_room");
        }

        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_device, container, false);
        TextView controllo = (TextView) myView.findViewById(R.id.textViewMex);
        //controllo.setText(forno.toString());

        //RECUPERO TOGGLE BUTTON CONDIZIONATORE
        final ToggleButton toggleButton= (ToggleButton) myView.findViewById(R.id.toggleButton);

        //RECUPERO TEXT VIEW TEMPERATURA E SEEK BAR
        final TextView textViewTemperatura= (TextView)myView.findViewById(R.id.textViewTemepratura);

        final TextView textViewMinTemp= (TextView)myView.findViewById(R.id.tvLabel1);

        final TextView textViewMaxTemp= (TextView)myView.findViewById(R.id.tvLabel2);

        final TextView textViewIntestazioneTemp= (TextView)myView.findViewById(R.id.textView2);

        final SeekBar seekBarTemp= (SeekBar)myView.findViewById(R.id.seekBarTempBarTemp);
        //seekbar non permette di cambiare il minimo che resta sempre 0
        //perciò ipotizzando un range per il condizionatore tra 18-30 gradi
        //imposto il max a 12 ed aggiungo 18 ad ogni valore letto dalla seek bar
        seekBarTemp.setMax(12);
        textViewMaxTemp.setText("30");
        textViewMinTemp.setText("18");
        seekBarTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progresso = progress + 18;
                textViewTemperatura.setText(progresso+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarTemp.setProgress(4); //che in realtà è 22

        //SETTO I COMPONENTI DELL'INTERFACCIA SECONDO LE INFORMAZIONI LETTE
        if(!condizionatore.isStato_attuale()){
            toggleButton.setChecked(false);
            seekBarTemp.setEnabled(false);
            //textViewTemperatura.setVisibility(View.INVISIBLE);
            //textViewIntestazioneTemp.setVisibility(View.INVISIBLE);
            textViewTemperatura.setTextColor(Color.parseColor("#616161"));
            textViewIntestazioneTemp.setTextColor(Color.parseColor("#616161"));
            textViewMaxTemp.setTextColor(Color.parseColor("#616161"));
            textViewMinTemp.setTextColor(Color.parseColor("#616161"));
        }else{
            toggleButton.setChecked(true);
            textViewTemperatura.setText(condizionatore.getTemp() + "");
            seekBarTemp.setEnabled(true);
            seekBarTemp.setProgress(condizionatore.getTemp()-18);
            textViewTemperatura.setVisibility(View.VISIBLE);
            textViewIntestazioneTemp.setVisibility(View.VISIBLE);
            textViewMaxTemp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewMinTemp.setTextColor(Color.parseColor("#FFFFFF"));
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    seekBarTemp.setEnabled(true);
                    //textViewTemperatura.setVisibility(View.VISIBLE);
                    //textViewIntestazioneTemp.setVisibility(View.VISIBLE);
                    textViewTemperatura.setTextColor(Color.parseColor("#FFFFFF"));
                    textViewIntestazioneTemp.setTextColor(Color.parseColor("#FFFFFF"));
                    textViewMaxTemp.setTextColor(Color.parseColor("#FFFFFF"));
                    textViewMinTemp.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else{
                    seekBarTemp.setEnabled(false);
                    //textViewTemperatura.setVisibility(View.INVISIBLE);
                    //textViewIntestazioneTemp.setVisibility(View.INVISIBLE);
                    textViewTemperatura.setTextColor(Color.parseColor("#616161"));
                    textViewIntestazioneTemp.setTextColor(Color.parseColor("#616161"));
                    textViewMaxTemp.setTextColor(Color.parseColor("#616161"));
                    textViewMinTemp.setTextColor(Color.parseColor("#616161"));
                }
            }
        });

        final TextView textViewAlias= (TextView)myView.findViewById(R.id.tvAlias);
        final TextView textViewEnergy= (TextView)myView.findViewById(R.id.tvEnergy);
        final TextView textViewType= (TextView)myView.findViewById(R.id.tvType);

        String alias= condizionatore.getAlias().substring(1,condizionatore.getAlias().length()-1);
        String type= condizionatore.getType().substring(1,condizionatore.getType().length()-1);

        textViewAlias.setText("Alias: "+alias);
        textViewEnergy.setText("Energy: " + condizionatore.getConsumo()+" kW");
        textViewType.setText("Type: "+type);

        final ImageView imageViewMoreInfo = (ImageView)myView.findViewById(R.id.imageViewMoreInfo);
        final TextView textViewMoreInfo= (TextView)myView.findViewById(R.id.textViewMoreInfo);

        textViewMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewAlias.getVisibility()==View.INVISIBLE) {
                    imageViewMoreInfo.setImageResource(R.drawable.ic_arrow_up);
                    textViewAlias.setVisibility(View.VISIBLE);
                    textViewEnergy.setVisibility(View.VISIBLE);
                    textViewType.setVisibility(View.VISIBLE);
                }else{
                    imageViewMoreInfo.setImageResource(R.drawable.ic_arrow_down);
                    textViewAlias.setVisibility(View.INVISIBLE);
                    textViewEnergy.setVisibility(View.INVISIBLE);
                    textViewType.setVisibility(View.INVISIBLE);
                }

            }
        });
        imageViewMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewAlias.getVisibility()==View.INVISIBLE) {
                    imageViewMoreInfo.setImageResource(R.drawable.ic_arrow_up);
                    textViewAlias.setVisibility(View.VISIBLE);
                    textViewEnergy.setVisibility(View.VISIBLE);
                    textViewType.setVisibility(View.VISIBLE);
                }else{
                    // imageViewMoreInfo.setImageDrawable(Drawable.createFromPath("@android:drawable/arrow_down_float"));
                    imageViewMoreInfo.setImageResource(R.drawable.ic_arrow_down);
                    textViewAlias.setVisibility(View.INVISIBLE);
                    textViewEnergy.setVisibility(View.INVISIBLE);
                    textViewType.setVisibility(View.INVISIBLE);
                }
            }
        });

        //SALVATAGGIO NUOVI DATI

        Button saveButton = (Button)myView.findViewById(R.id.buttonSalva);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recupero i valori da salvare
                boolean statoAttualeDispositivo = toggleButton.isChecked();
                int temperaturaSettata = seekBarTemp.getProgress()+18;

                String messageToUser;
                if (statoAttualeDispositivo)
                    messageToUser="Air conditioner ON at temperature "+ temperaturaSettata;
                else
                    messageToUser="Air conditioner OFF";

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //QUI CONDIZIONATORE TASK

                CondizionatoreTask risc = new CondizionatoreTask(centroTuple, agente, centroTuple_room, agente_room, utente, fragmentTransaction);

                String statoAttuale;
                if(statoAttualeDispositivo)
                    statoAttuale = "on";
                else statoAttuale = "off";

                risc.execute("save", statoAttuale, temperaturaSettata+"");

                Toast.makeText(getContext(), messageToUser, Toast.LENGTH_SHORT).show();
            }
        });




        return myView;
    }


}
