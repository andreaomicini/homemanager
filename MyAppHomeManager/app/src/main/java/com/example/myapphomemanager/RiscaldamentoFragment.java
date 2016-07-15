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

import com.example.myapphomemanager.MyTask.RiscaldaTask;
import com.examples.model.DeviceData;
import com.examples.model.Utente;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

/**
 * Created by Alessio on 30/05/2016.
 */
public class RiscaldamentoFragment extends Fragment {

    /*connessione*/
    TucsonTupleCentreId centroTuple=null;
    TucsonAgentId agente=null;
    TucsonTupleCentreId centroTuple_room=null;
    TucsonAgentId agente_room=null;

    /*gestione politiche RBAC*/
    Utente utente;

    DeviceData riscaldamento;
    private View myView;

    public RiscaldamentoFragment(){}

    public static RiscaldamentoFragment newInstance(DeviceData risData,TucsonTupleCentreId tc,TucsonAgentId agent,TucsonTupleCentreId device_room_tc,TucsonAgentId riscaldaAgent,Utente utente){
        RiscaldamentoFragment fragment = new RiscaldamentoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Riscaldamento",risData);
        bundle.putSerializable("CentroTuple",tc);
        bundle.putSerializable("Agente",agent);
        bundle.putSerializable("CentroTuple_room",device_room_tc);
        bundle.putSerializable("Agente_room", riscaldaAgent);
        bundle.putSerializable("Utente",utente);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //RECUPERO I PARAMETRI
        riscaldamento = (DeviceData) getArguments().getSerializable("Riscaldamento");
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

        //RECUPERO TOGGLE BUTTON RISCALDAMENTO
        final ToggleButton toggleButton= (ToggleButton) myView.findViewById(R.id.toggleButton);

        //RECUPERO TEXT VIEW TEMPERATURA E SEEK BAR
        final TextView textViewTemperatura= (TextView)myView.findViewById(R.id.textViewTemepratura);

        final TextView textViewMinTemp= (TextView)myView.findViewById(R.id.tvLabel1);

        final TextView textViewMaxTemp= (TextView)myView.findViewById(R.id.tvLabel2);

        final TextView textViewIntestazioneTemp= (TextView)myView.findViewById(R.id.textView2);

        final SeekBar seekBarTemp= (SeekBar)myView.findViewById(R.id.seekBarTempBarTemp);
        seekBarTemp.setMax(30);
        textViewMaxTemp.setText("30");

        seekBarTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewTemperatura.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTemp.setProgress(10);
        //SETTO I COMPONENTI DELL'INTERFACCIA SECONDO LE INFORMAZIONI LETTE
        if(!riscaldamento.isStato_attuale()){
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
            textViewTemperatura.setText(riscaldamento.getTemp() + "");
            seekBarTemp.setEnabled(true);
            seekBarTemp.setProgress(riscaldamento.getTemp());
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

        String alias= riscaldamento.getAlias().substring(1,riscaldamento.getAlias().length()-1);
        String type= riscaldamento.getType().substring(1,riscaldamento.getType().length()-1);

        textViewAlias.setText("Alias: "+alias);
        textViewEnergy.setText("Energy: "+riscaldamento.getConsumo()+" kW");
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
                int temperaturaSettata = seekBarTemp.getProgress();

                String messageToUser;
                if (statoAttualeDispositivo)
                    messageToUser="Riscaldamento ON at temperature "+ temperaturaSettata;
                else
                    messageToUser="Riscaldamento OFF";

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                //QUI RISCALDAMENTO TASK
                RiscaldaTask risc = new RiscaldaTask(centroTuple, agente,centroTuple_room,agente_room,utente,fragmentTransaction);

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
