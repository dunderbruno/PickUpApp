package com.pickupapp.gui.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import com.pickupapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {

    private static final int PERMISSAO_REQUERIDA = 1;
    private LocationManager locationManager;
    private Double latitude,longitude;
    private GeoPoint pontoInicial;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.maps_fragment, container, false);



        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissoes, PERMISSAO_REQUERIDA);
        }

        ActivityCompat.requestPermissions(getActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSAO_REQUERIDA);

        Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());

        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Check gps is enable or not

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

            OnGPS();
        }
        else
        {
            //GPS is already On then

            getLocation();
        }

        //Pega o mapa adicionada no arquivo activity_main.xml
        MapView mapa = (MapView) rootView.findViewById(R.id.mapaId);
        //Fonte de imagens
        ///mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        //Cria um ponto de referência com base na latitude e longitude
        //setarLocalizacaoPadrao();

        if(latitude!= null){
            pontoInicial = new GeoPoint(latitude, longitude);

        }else{
            pontoInicial = new GeoPoint(-8.016276, -34.950909);
            Toast.makeText(getActivity(),"Local não disponível",Toast.LENGTH_LONG).show();
        }





        IMapController mapController = mapa.getController();
        //Faz zoom no mapa
        mapController.setZoom(15);
        //Centraliza o mapa no ponto de referência
        mapController.setCenter(pontoInicial);

        //Cria um marcador no mapa
        Marker startMarker = new Marker(mapa);
        startMarker.setPosition(pontoInicial);
        startMarker.setTitle("Ponto Inicial");
        //Posição do ícone
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapa.getOverlays().add(startMarker);





        return rootView;


    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSAO_REQUERIDA);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=LocationGps.getLatitude();
                longitude=LocationGps.getLongitude();

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=LocationNetwork.getLatitude();
                longitude=LocationNetwork.getLongitude();

            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=LocationPassive.getLatitude();
                longitude=LocationPassive.getLongitude();

            }
            else
            {
                Toast.makeText(getContext(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }



    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }




}










