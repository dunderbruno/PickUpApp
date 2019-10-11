package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pickupapp.R;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.SpaceAdapter;
import com.pickupapp.persistencia.SpaceDAO;

import org.json.JSONException;

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
        View inflate = inflater.inflate(R.layout.fragment_list_spaces, container, false);
        return inflate;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ListView lista = (ListView) getView().findViewById(R.id.lista_spaces_fragment);
        ArrayAdapter<Space> adapter = new SpaceAdapter(getActivity(), retornarSpaces());
        lista.setAdapter(adapter);
    }

    private ArrayList<Space> retornarSpaces(){
        final SpaceDAO spaceDAO = new SpaceDAO(getContext());
        final ArrayList<Space>[] response = new ArrayList[]{new ArrayList<>()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = spaceDAO.getSpaces();
                    Log.d("resposta", "retornarSpaces: "+ response[0].toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        Log.d("resposta", "retornarSpaces: "+ response[0].toString());
        return response[0];



    }

}
