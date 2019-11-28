package com.pickupapp.dominio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Invite;
import com.pickupapp.dominio.Player;

import java.util.ArrayList;

public class InviteAdapter extends ArrayAdapter<Invite> {

    private final Context context;
    private final ArrayList<Invite> jogadores;


    public InviteAdapter(Context context, ArrayList<Invite> jogadores) {
        super(context, R.layout.linha_spaces, jogadores);
        this.context = context;
        this.jogadores = jogadores;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_players, parent, false);

        TextView nomeNick = rowView.findViewById(R.id.primeiro_nome);
        TextView primeiroNome = rowView.findViewById(R.id.nome_pessoa);
        TextView email = rowView.findViewById(R.id.email_pessoa);

        nomeNick.setText(jogadores.get(position).getBooking_id());
        String primeiroNomeSet = jogadores.get(position).getInvite_id();
        primeiroNome.setText(primeiroNomeSet);
        email.setText(jogadores.get(position).getStatus());
        return rowView;

    }

}


