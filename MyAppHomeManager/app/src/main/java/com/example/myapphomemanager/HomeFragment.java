package com.example.myapphomemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.model.Utente;

/**
 * Created by Alessio on 23/05/2016.
 */
public class HomeFragment extends Fragment {
    private View myView;
    private Utente utente;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance (Utente utente){
            HomeFragment fragment =new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("utente",utente);
            fragment.setArguments(bundle);
            return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        utente = (Utente) getArguments().getSerializable("utente");
        TextView textViewWelcome = (TextView) myView.findViewById(R.id.textViewWelcome);
        String nomeUtente = utente.getFirstname();
        nomeUtente = nomeUtente.substring(1,nomeUtente.length()-1);
        textViewWelcome.setText("Welcome "+nomeUtente);


        return myView;
    }



}
