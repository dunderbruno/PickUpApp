package com.pickupapp.gui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Address;
import com.pickupapp.dominio.City;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.State;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.EnumSpaceType;
import com.pickupapp.infra.MonetaryMask;
import com.pickupapp.infra.Sessao;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.AddressInterface;
import com.pickupapp.persistencia.ContactInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.AddressCall;
import com.pickupapp.persistencia.retorno.ContactCall;
import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.SpotCall;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterSpaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText nomeEspaco;
    private EditText telefone;
    private EditText email;
    private EditText logradouro;
    private EditText numero;
    private EditText bairro;
    private Spinner cidade;
    private Spinner estado;
    private EditText valor;
    private EditText cep;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private Bitmap reducedImage;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String imagemAtual;
    public static final int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register_space, container, false);
        setarVariaveis(inflate);
        Button cadastrar = inflate.findViewById(R.id.buttonCadastrarEspaco);
        cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (checkFields()) {
                    checkRadio();
                    final Space space = createSpace();
                    space.setName(nomeEspaco.getText().toString());
                    space.setPhone(telefone.getText().toString());
                    space.setEmail(email.getText().toString());
                    space.setPriceHour(new BigDecimal(valor.getText().toString()));
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);
                    RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                    String selectedtext = r.getText().toString();
                    space.setSpaceType(EnumSpaceType.valueOf(selectedtext));
                    Address address = new Address();
                    address.setCep(cep.getText().toString());
                    City city = new City();
                    city.setName("");
                    city.setId(1);
                    address.setCity(city);
                    address.setNeighboorhood(bairro.getText().toString());
                    address.setStreet(logradouro.getText().toString());
                    space.setAddress(address);
                    cadastrarEspaco(space);
                }
            }
        });
        imageOnClick();
        return inflate;
    }

    private void setarVariaveis(View inflate) {
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
        setSpinnerAdapter();
        imageView = inflate.findViewById(R.id.ImageViewEspaco);
        imageView1 = inflate.findViewById(R.id.imageViewSpace1);
        imageView2 = inflate.findViewById(R.id.imageViewEspaco2);
        imageView3 = inflate.findViewById(R.id.imageViewEspaco3);
        imageView4 = inflate.findViewById(R.id.imageViewEspaco4);
        imageView5 = inflate.findViewById(R.id.imageViewEspaco5);
        imagemAtual = "0";
    }

    private void imageOnClick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("0");
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("1");
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("2");
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("3");
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("4");
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagem("5");
            }
        });
    }

    private void buscarImagem(String posicao) {
        imagemAtual = posicao;
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void cadastrarEspaco(final Space space) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface = retrofit.create(SpaceInterface.class);
        User usuario = Sessao.getSessao(getContext());
        String credentials = usuario.getUsername()+":"+
                usuario.getPassword();
        final String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        final String token = Sessao.getSessao(getContext()).getToken();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("spot_name", space.getName());
        Call<SpotCall> call = spaceInterface.registerSpace(auth,token,params);
        call.enqueue(new Callback<SpotCall>() {
            @Override
            public void onResponse(Call<SpotCall> call, Response<SpotCall> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(),"Local Não Cadastrado",Toast.LENGTH_LONG).show();
                    Log.d("resposta", "cadastro space: "+response);
                    return;
                }
                Toast.makeText(getContext(),"Local Cadastrado Com Sucesso",Toast.LENGTH_LONG).show();
                SpotCall resposta = response.body();
                space.setId(Long.parseLong(resposta.getSpot_id()));
            }

            @Override
            public void onFailure(Call<SpotCall> call, Throwable t) {
                Log.d("resposta", "cadastro space: "+t);
                Toast.makeText(getContext(),"Local Não Cadastrado",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.states, android.R.layout.simple_spinner_item);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado.setAdapter(estadoAdapter);
        ArrayAdapter<CharSequence> cidadeAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),R.array.cities_pernambuco, android.R.layout.simple_spinner_item);
        cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cidade.setAdapter(cidadeAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmaps = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                switch (imagemAtual) {
                    case "0":
                        imageView.setImageBitmap(bitmaps);
                        break;
                    case "1":
                        imageView1.setImageBitmap(bitmaps);
                        break;
                    case "2":
                        imageView2.setImageBitmap(bitmaps);
                        break;
                    case "3":
                        imageView3.setImageBitmap(bitmaps);
                        break;
                    case "4":
                        imageView4.setImageBitmap(bitmaps);
                        break;
                    case "5":
                        imageView5.setImageBitmap(bitmaps);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkRadio(){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (radioButton).findViewById(id);
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
        state.setName(estado.getPrompt().toString());
        City city = new City();
        city.setName(cidade.getPrompt().toString());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
