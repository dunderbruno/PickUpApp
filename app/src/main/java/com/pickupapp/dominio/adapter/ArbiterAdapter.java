package com.pickupapp.dominio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Referee;
import com.pickupapp.dominio.Space;

import java.util.ArrayList;

public class ArbiterAdapter extends ArrayAdapter<Referee> {

    private final Context context;
    private final ArrayList<Referee> arbitros;


    public ArbiterAdapter(Context context, ArrayList<Referee> arbitros){
        super(context, R.layout.linha_players, arbitros);
        this.context = context;
        this.arbitros = arbitros;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_players, parent, false);

        TextView nomeNick = rowView.findViewById(R.id.id_player_nic);
        TextView primeiroNome = rowView.findViewById(R.id.id_player_primeiro_nome);
        TextView email = rowView.findViewById(R.id.id_player_email);

        nomeNick.setText(arbitros.get(position).getUser().getUsername());
        String primeiroNomeSet = arbitros.get(position).getUser().getPerson().getName();
        String segundooNomeSet = arbitros.get(position).getUser().getPerson().getSurname();
        primeiroNome.setText(primeiroNomeSet + " " + segundooNomeSet);
        email.setText(arbitros.get(position).getUser().getContact().getEmail());
        return rowView;

    }


}
