package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pickupapp.R;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.SpaceAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSpacesFragment extends Fragment {


    public ListSpacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        //ListView lista = (ListView) getView().findViewById(R.id.lista_spaces_fragment);
        ///ArrayAdapter<Space> adapter = new SpaceAdapter(getActivity(), retornarSpaces());
        ///lista.setAdapter(adapter);


        return inflater.inflate(R.layout.fragment_list_spaces, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ListView lista = (ListView) getView().findViewById(R.id.lista_spaces_fragment);
        ArrayAdapter<Space> adapter = new SpaceAdapter(getActivity(), retornarSpaces());
        lista.setAdapter(adapter);
    }

    private ArrayList<Space> retornarSpaces(){
        ArrayList<Space> espacos = new ArrayList<Space>();
        Space espaco = new Space();
        espaco.setName("Espaco 1");
        espaco.setPhone("12345");
        espacos.add(espaco);
        espaco.setName("Espaco 2");
        espaco.setPhone("56789");
        espacos.add(espaco);

        return espacos;



    }

}
