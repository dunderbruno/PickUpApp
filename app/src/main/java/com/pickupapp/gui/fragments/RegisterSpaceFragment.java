package com.pickupapp.gui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pickupapp.R;
import com.pickupapp.dominio.Address;
import com.pickupapp.dominio.City;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.State;
import com.pickupapp.infra.EnumSpaceType;
import com.pickupapp.infra.MonetaryMask;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.SpaceDAO;
import com.pickupapp.persistencia.UserDAO;

import org.json.JSONException;

import java.math.BigDecimal;

public class RegisterSpaceFragment extends Fragment {

    private EditText nomeEspaco;
    private EditText telefone;
    private EditText email;
    private EditText logradouro;
    private EditText numero;
    private EditText bairro;
    private EditText cidade;
    private EditText estado;
    private EditText valor;
    private EditText cep;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register_space, container, false);
        Button voltar;
        Button cadastrar = inflate.findViewById(R.id.button_cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (checkFields()) {
                    Space space = createSpace();
                    SpaceDAO spaceDAO = new SpaceDAO(getContext());
                    try {
                        spaceDAO.register(space);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nomeEspaco = inflate.findViewById(R.id.inputNomeEspaco);
        telefone = inflate.findViewById(R.id.inputTelefoneEspacos);
        email = inflate.findViewById(R.id.inputEmailEspacos);
        logradouro = inflate.findViewById(R.id.inputLogradouroEspacos);
        numero = inflate.findViewById(R.id.inputNumeroEspacos);
        bairro = inflate.findViewById(R.id.inputBairroEspacos);
        cidade = inflate.findViewById(R.id.inputCidadeEspacos);
        estado = inflate.findViewById(R.id.inputEstadoEspacos);
        radioGroup = inflate.findViewById(R.id.radioGroupEspacos);
        cep = inflate.findViewById(R.id.inputCepEspacos);
        valor = inflate.findViewById(R.id.inputValorEspacos);
        valor.addTextChangedListener(new MonetaryMask(valor));
        return inflater.inflate(R.layout.fragment_register_space, container, false);
    }

    public EnumSpaceType checkButton (View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = (radioButton).findViewById(radioId);
        return getType(radioButton.getText().toString());
    }

    private boolean checkFields(){
        ValidacaoGui validacao = new ValidacaoGui();
        if (!validacao.verificarTamanhoCampo(nomeEspaco.getText().toString())){
            nomeEspaco.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(telefone.getText().toString())){
            telefone.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(cep.getText().toString())){
            cep.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarCampoEmail(email.getText().toString())){
            email.setError("Email obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(logradouro.getText().toString())){
            logradouro.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(bairro.getText().toString())){
            bairro.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(numero.getText().toString())){
            numero.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(estado.getText().toString())){
            estado.setError("Campo obrigatório");
            return false;
        }
        if (validacao.verificarTamanhoCampo(cidade.getText().toString())){
            cidade.setError("Campo obrigatório");
            return false;
        }

        return true;

    }

    private Space createSpace(){
        Space space = new Space();
        space.setName(nomeEspaco.getText().toString());
        space.setPhone(telefone.getText().toString());
        space.setPriceHour(new BigDecimal(valor.getText().toString()));
        space.setAddress(createAddress());
        space.setSpaceType(getType(radioButton.getText().toString()));
        return space;
    }

    private Address createAddress(){
        Address address = new Address();
        address.setCep(cep.getText().toString());
        address.setNeighboorhood(bairro.getText().toString());
        address.setNumber(Integer.parseInt(numero.getText().toString()));
        State state = new State();
        state.setName(estado.getText().toString());
        City city = new City();
        city.setName(cidade.getText().toString());
        address.setCity(city);
        address.setStreet(logradouro.getText().toString());
        return address;
    }

    private EnumSpaceType getType(String type){
        if (type.equals("Society")){
            return EnumSpaceType.SOCIETY;
        }
        else if (type.equals("Gramado")){
            return EnumSpaceType.GRASS;
        }
        else if (type.equals("Terra")){
            return EnumSpaceType.EARTH;
        }
        else{
            return EnumSpaceType.COURT;
        }
    }
}
