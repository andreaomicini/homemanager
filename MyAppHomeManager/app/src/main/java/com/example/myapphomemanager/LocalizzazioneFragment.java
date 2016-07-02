package com.example.myapphomemanager;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

/**
 * Created by Alessio on 31/05/2016.
 */
public class LocalizzazioneFragment extends Fragment implements OnMapReadyCallback{

    View myView;
    MapView mapView;
    GoogleMap googleMap;
    String latitudine;
    String longitudine;
    ArrayList<String> listaLuoghi;
    ArrayList<Double> placeLongitude;
    ArrayList<Double> placeLatitude;

    public LocalizzazioneFragment(){
        placeLatitude = new ArrayList<>();
        placeLongitude = new ArrayList<>();

    }

    public static LocalizzazioneFragment newInstance(String[] ris, float lat, float lon) {
        LocalizzazioneFragment fragment= new LocalizzazioneFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("Servizi", ris);
        bundle.putSerializable("Latitudine", String.valueOf(lat));
        bundle.putSerializable("Longitudine", String.valueOf(lon));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.fragment_localizzazione, container, false);
        final TextView posizioneAttuale = (TextView)myView.findViewById(R.id.posizioneAttuale);
        final TextView latit = (TextView)myView.findViewById(R.id.latitude);
        final TextView longi = (TextView)myView.findViewById(R.id.longitude);

        String[] result= (String[]) getArguments().getSerializable("Servizi");

        if(result.equals(null))
        {
            posizioneAttuale.setText("Non e' stato possibile rilevare la tua posizione");
        }
        else
        {
            posizioneAttuale.setText(result[0] +", "+ result[1]);

            listaLuoghi = new ArrayList<String>();
            /*RECUPERA UNA LISTA VUOTA
            int l=2;
            int j=0;
            while(j<result.length-2)
            {
                listaLuoghi.add(result[l]);
                l++;
                j++;
            }*/
            //Log.i("RESULT3: ",result[2]);
            String[] luoghi = new String[]{"1. Osteria della capra","2. CàBorghi Bed&Breakfast",
                "3. Pizzaria l'angolo della pizza","4. Pasticceria Menozzi"};
            listaLuoghi.addAll(Arrays.asList(luoghi));

            ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaLuoghi){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.WHITE);
                textView.setTextSize(15.0f);

                return view;
            }
        };
            ListView listView = (ListView)myView.findViewById(R.id.preferitiListView);
            listView.setAdapter(adapter);
        }

        latitudine = (String)getArguments().getSerializable("Latitudine");
        latit.setText("LATITUDINE: "+latitudine);
        longitudine = (String) getArguments().getSerializable("Longitudine");
        longi.setText("LONGITUDINE: "+longitudine);

        mapView = (MapView) myView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment


        //googleMap.setMyLocationEnabled(true);
        //Stampare servizi vicini sulla mappa
        Geocoder geocoder = new Geocoder(this.getContext());
        List<Address> addresses = null;
        try {
            //osteria della capra
            addresses = geocoder.getFromLocationName("36,Via Andrea Rivasi, 34,42025 Cavriago RE",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            placeLatitude.add(addresses.get(0).getLatitude());
            placeLongitude.add(addresses.get(0).getLongitude());
        }

        try {
            //CàBorghi Bed&Breakfast
            addresses = geocoder.getFromLocationName("Via Guardanavona, 35,42025 Cavriago RE",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            placeLatitude.add(addresses.get(0).getLatitude());
            placeLongitude.add(addresses.get(0).getLongitude());
        }

        try {
            //pizzaria l'angolo della pizza
            addresses = geocoder.getFromLocationName("Via Govi, 1A,Cavriago RE",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            placeLatitude.add(addresses.get(0).getLatitude());
            placeLongitude.add(addresses.get(0).getLongitude());
        }

        try {
            //pasticceria menozzi
            addresses = geocoder.getFromLocationName("Via Rivasi, 3,Provincia di Reggio Emilia",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            placeLatitude.add(addresses.get(0).getLatitude());
            placeLongitude.add(addresses.get(0).getLongitude());
        }


        return myView;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //googleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(Double.parseDouble(latitudine),Double.parseDouble(longitudine));

        CameraUpdate center= CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);

        googleMap.addMarker(new MarkerOptions().position(latLng).title("You"));

        for(int i=0;i<placeLatitude.size();i++) {
            latLng = new LatLng(placeLatitude.get(i), placeLongitude.get(i));
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Service " + (i+1))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }
}
