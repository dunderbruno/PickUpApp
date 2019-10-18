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
    private Bitmap reducedImage;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public static final int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register_space, container, false);
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
        imageView = inflate.findViewById(R.id.ImageViewEspaco);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
/*        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }
            }
        });

 */

        //return inflater.inflate(R.layout.fragment_register_space, container, false);
        return inflate;
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
                cadastrarEndereco(space, auth, token);
            }

            @Override
            public void onFailure(Call<SpotCall> call, Throwable t) {
                Log.d("resposta", "cadastro space: "+t);
                Toast.makeText(getContext(),"Local Não Cadastrado",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cadastrarEndereco(final Space space, final String auth, final String token) {
        Retrofit retrofita = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressInterface addressInterface = retrofita.create(AddressInterface.class);
        Map<String, String> parames = new HashMap<String, String>();
        parames.put("street", space.getAddress().getStreet());
        parames.put("number", String.valueOf(space.getAddress().getNumber()));
        parames.put("neighborhood", space.getAddress().getNeighboorhood());
        parames.put("cep", space.getAddress().getCep());
        Call<AddressCall> callC = addressInterface.registerAddress(auth,token,parames);
        callC.enqueue(new Callback<AddressCall>() {
            @Override
            public void onResponse(Call<AddressCall> call, Response<AddressCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro Address: "+response);
                    return;
                }
                Log.d("resposta", "cadastro Address: "+response);
                AddressCall addressCall = response.body();
                space.getAddress().setId(Long.parseLong(addressCall.getAddress_id()));
                setSpaceAddress(auth, token, space);
            }

            @Override
            public void onFailure(Call<AddressCall> call, Throwable t) {
                Log.d("resposta", "cadastro Address: "+t);
            }
        });
    }

    private void setSpaceAddress(final String auth, final String token, final Space space) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface2 = retrofit.create(SpaceInterface.class);
        Map<String, String> paramet = new HashMap<String, String>();
        paramet.put("address_id", String.valueOf(space.getAddress().getId()));
        Call<SetCall> callS = spaceInterface2.setSpaceAdress(auth,token,String.valueOf(space.getId()), paramet);
        callS.enqueue(new Callback<SetCall>() {
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "set space address: "+response);
                    return;
                }
                Log.d("resposta", "set space address: "+response);
                registerContact(space, auth, token);

            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
                Log.d("resposta", "set space address: "+t);
            }
        });
    }

    private void registerContact(final Space space, final String auth, final String token) {
        Retrofit retrofit3 = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContactInterface contactInterface = retrofit3.create(ContactInterface.class);
        Map<String, String> paramet = new HashMap<String, String>();
        paramet.put("email", String.valueOf(space.getEmail()));
        paramet.put("prone", String.valueOf(space.getPhone()));
        Call<ContactCall> callCall= contactInterface.registerContact(auth,token, paramet);
        callCall.enqueue(new Callback<ContactCall>() {
            @Override
            public void onResponse(Call<ContactCall> call, Response<ContactCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro Contato: "+response);
                    return;
                }
                ContactCall contactCall = response.body();
                setContactSpace(contactCall, auth, token, space);
            }

            @Override
            public void onFailure(Call<ContactCall> call, Throwable t) {
                Log.d("resposta", "cadastro Contato: "+t);
            }
        });
    }

    private void setContactSpace(ContactCall contactCall, String auth, String token, Space space) {
        Retrofit retrofit4 = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface2 = retrofit4.create(SpaceInterface.class);
        Map<String, String> paramet = new HashMap<String, String>();
        paramet.put("contact_id", contactCall.getContact_id());
        Call<SetCall> callSet = spaceInterface2.setSpaceAdress(auth,token,String.valueOf(space.getId()), paramet);
        callSet.enqueue(new Callback<SetCall>() {
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "set space contact: "+response);
                    return;
                }
                Log.d("resposta", "set space contact: "+response);
            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
                Log.d("resposta", "set space contact: "+t);
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

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmaps = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmaps);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onActivityResult2(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1000) {
                try{
                    Uri returnUri = data.getData();
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    imageView.setImageBitmap(bitmapImage);
                    //SALVA NO BANCO
                    //SALVA NO BANCO
                    //SALVA NO BANCO
                    //SALVA NO BANCO
                    //SALVA NO BANCO
                    //SALVA NO BANCO
                    //SALVA NO BANCO


                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        //Uri returnUri;
        //returnUri = data.getData();
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
