package com.pickupapp.dominio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Space;

import java.net.ConnectException;
import java.util.ArrayList;

public class SpaceAdapter extends ArrayAdapter<Space> {

    private final Context context;
    private final ArrayList<Space> espacos;


    public SpaceAdapter(Context context, ArrayList<Space> espacos){
        super(context, R.layout.linha_spaces, espacos);
        this.context = context;
        this.espacos = espacos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_spaces, parent, false);

        TextView nameSpace = rowView.findViewById(R.id.id_nome);
        TextView addressSpace = rowView.findViewById(R.id.id_address_list_spaces);
        TextView priceSpace = rowView.findViewById(R.id.id_price_list_spaces);

        nameSpace.setText(espacos.get(position).getName());
        addressSpace.setText(espacos.get(position).getPhone());
        String preco = "R$" + espacos.get(position).getPriceHour();
        priceSpace.setText(preco);
        return rowView;

    }


}
