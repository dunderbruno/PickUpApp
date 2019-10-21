package com.pickupapp.gui.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import com.pickupapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {

    private static final int PERMISSAO_REQUERIDA = 1;



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

        Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());

        //Pega o mapa adicionada no arquivo activity_main.xml
        MapView mapa = (MapView) rootView.findViewById(R.id.mapaId);
        //Fonte de imagens
        ///mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        //Cria um ponto de referência com base na latitude e longitude
        GeoPoint pontoInicial = new GeoPoint(-8.016276, -34.950909);

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



}










