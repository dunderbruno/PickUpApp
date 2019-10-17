package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.pickupapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private CircularImageView imagemUsuario;
    private TextView nomeUsuario;
    private TextView emailUsuario;
    private TextView enderecoUsuario;
    private TextView telefoneUsuario;
    private Button botaoEditarPerfil;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_profile, container, false);
        botaoEditarPerfil = inflate.findViewById(R.id.botaoEditarPerfilId);
        botaoEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar Activity Editar
                Toast.makeText(getContext(),"activity editar", Toast.LENGTH_LONG);
            }
        });


        imagemUsuario = inflate.findViewById(R.id.circularImageId);
        nomeUsuario = inflate.findViewById(R.id.nomeUsuarioId);
        emailUsuario = inflate.findViewById(R.id.emailUsuarioId);
        enderecoUsuario = inflate.findViewById(R.id.enderecoUsuarioId);
        telefoneUsuario= inflate.findViewById(R.id.telefoneUsuarioId);
        setUsuario();



        return inflater.inflate(R.layout.fragment_profile, container, false);








    }

    private void setUsuario() {
        //retornar objeto do banco
        //setar imagem, nome, email, endereco e usuario pelo banco
    }


}
