package com.pickupapp.gui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.pickupapp.R;
import com.pickupapp.dominio.Address;
import com.pickupapp.dominio.City;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.State;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.EnumSpaceType;
import com.pickupapp.infra.Mask;
import com.pickupapp.infra.MoneyTextWatcher;
import com.pickupapp.infra.Sessao;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.AddressInterface;
import com.pickupapp.persistencia.PhotoInterface;
import com.pickupapp.persistencia.ScheduleInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.CitysCall;
import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.SpotCall;
import com.pickupapp.persistencia.retorno.StatesCall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterSpaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText nomeEspaco, telefone, email, logradouro, numero, valor, cep;
    private Spinner cidade;
    private Spinner estado;
    private Spinner tipo;
    private ImageView imageView, imageView1, imageView2, imageView3, imageView4, imageView5;
    private Bitmap reducedImage;
    private RadioGroup radioGroup;
    private RadioButton radioButon;
    private String imagemAtual;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private static final int RESULT_LOAD_IMAGE = 1;
    private PlacesClient placesClient;
    private String autocompleteBairro;
    private Button voltar, voltar1, voltar2, cancelar, proximo, proximo1, proximo2, cadastrar;
    private LinearLayout dadosSpot, dadosEndereco, dadosImagens, dadosFuncionamento;
    private Switch segunda, terca, quarta, quinta, sexta, sabado, domingo;
    private LinearLayout horarios, horarioSegunda, horarioTerca, horarioQuarta, horarioQuinta;
    private LinearLayout horarioSexta, horarioSabado, horarioDomingo;
    private EditText inicioSegunda, inicioTerca, inicioQuarta, inicioQuinta, inicioSexta, inicioSabado, inicioDomingo;
    private EditText fimSegunda, fimTerca, fimQuarta, fimQuinta, fimSexta, fimSabado, fimDomingo;
    private ArrayList<byte[]> galeria = new ArrayList<byte[]>();
    private ArrayList<State> states;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register_space, container, false);
        setListaEstados();
        setarVariaveis(inflate);
        openAutoComplete();
        autocompleteSupportFragment.setHint("Bairro");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                autocompleteBairro = place.getAddress();
            }
            @Override
            public void onError(@NonNull Status status) {
            }
        });
        cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    if (validaHorarios()) {
                        checkRadio();
                        final Space space = createSpace();
                        cadastrarEspaco(space);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        final int[] count = {0};
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(count[0] != 0){
                    setarListaCidades(Math.toIntExact(states.get(i).getId()));
                }else {
                    count[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        imageOnClick();
        controlarRota();
        onClickSwitchs();
        addMascara();
        return inflate;
    }

    private void setListaEstados() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressInterface addressInterface = retrofit.create(AddressInterface.class);
        User usuario = Sessao.getSessao(getContext());
        String credentials = usuario.getUsername()+":"+
                usuario.getPassword();
        final String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        final String token = Sessao.getSessao(getContext()).getToken();
        Call<StatesCall> call = addressInterface.getAllStates(auth,token);
        call.enqueue(new Callback<StatesCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<StatesCall> call, Response<StatesCall> response) {
                StatesCall statesCall = response.body();
                states = statesCall.getEstados();
                ArrayList<String> listString = new ArrayList<>();
                states.forEach((n) -> listString.add(n.getName()));
                ArrayAdapter<String> estadoAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listString);
                estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                estado.setAdapter(estadoAdapter);
            }

            @Override
            public void onFailure(Call<StatesCall> call, Throwable t) {
            }
        });
    }

    private void setarListaCidades(int i) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressInterface addressInterface = retrofit.create(AddressInterface.class);
        User usuario = Sessao.getSessao(getContext());
        String credentials = usuario.getUsername()+":"+
                usuario.getPassword();
        final String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        final String token = Sessao.getSessao(getContext()).getToken();
        String state = String.valueOf(i);
        Call<CitysCall> call = addressInterface.getAllCity(auth,token,state);
        call.enqueue(new Callback<CitysCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<CitysCall> call, Response<CitysCall> response) {
                Log.d("resposta", String.valueOf(response.body().getCidades().isEmpty()));
                CitysCall citysCall = response.body();
                ArrayList<City> cities = citysCall.getCidades();
                ArrayList<String> listString = new ArrayList<>();
                cities.forEach((n) -> listString.add(n.getName()));
                ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listString);
                cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidade.setAdapter(cidadeAdapter);
            }

            @Override
            public void onFailure(Call<CitysCall> call, Throwable t) {
            }
        });
    }

    private void addMascara() {
        inicioSegunda.addTextChangedListener(Mask.insert(Mask.HORA, inicioSegunda));
        inicioTerca.addTextChangedListener(Mask.insert(Mask.HORA, inicioTerca));
        inicioQuarta.addTextChangedListener(Mask.insert(Mask.HORA, inicioQuarta));
        inicioQuinta.addTextChangedListener(Mask.insert(Mask.HORA, inicioQuinta));
        inicioSexta.addTextChangedListener(Mask.insert(Mask.HORA, inicioSexta));
        inicioSabado.addTextChangedListener(Mask.insert(Mask.HORA, inicioSabado));
        inicioDomingo.addTextChangedListener(Mask.insert(Mask.HORA, inicioDomingo));
        fimSegunda.addTextChangedListener(Mask.insert(Mask.HORA, fimSegunda));
        fimTerca.addTextChangedListener(Mask.insert(Mask.HORA, fimTerca));
        fimQuarta.addTextChangedListener(Mask.insert(Mask.HORA, fimQuarta));
        fimQuinta.addTextChangedListener(Mask.insert(Mask.HORA, fimQuinta));
        fimSexta.addTextChangedListener(Mask.insert(Mask.HORA, fimSexta));
        fimSabado.addTextChangedListener(Mask.insert(Mask.HORA, fimSabado));
        fimDomingo.addTextChangedListener(Mask.insert(Mask.HORA, fimDomingo));
        telefone.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, telefone));
        valor.addTextChangedListener(new MoneyTextWatcher(valor));
        cep.addTextChangedListener(Mask.insert(Mask.CEP_MASK, cep));
    }

    private void onClickSwitchs() {
        segunda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioSegunda.setVisibility(View.VISIBLE);
                }else {
                    horarioSegunda.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        terca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioTerca.setVisibility(View.VISIBLE);
                }else {
                    horarioTerca.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        quarta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioQuarta.setVisibility(View.VISIBLE);
                }else {
                    horarioQuarta.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        quinta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioQuinta.setVisibility(View.VISIBLE);
                }else {
                    horarioQuinta.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        sexta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioSexta.setVisibility(View.VISIBLE);
                }else {
                    horarioSexta.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        sabado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioSabado.setVisibility(View.VISIBLE);
                }else {
                    horarioSabado.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
        domingo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    horarios.setVisibility(View.VISIBLE);
                    horarioDomingo.setVisibility(View.VISIBLE);
                }else {
                    horarioDomingo.setVisibility(View.GONE);
                    if(switchsCheck()){
                        horarios.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private boolean switchsCheck() {
        return !segunda.isChecked() && !terca.isChecked() && !quarta.isChecked() &&
                !quinta.isChecked() && !sexta.isChecked() && !sabado.isChecked() &&
                        !domingo.isChecked();
    }

    private void controlarRota() {
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ListSpacesFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }
        });
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaDadosSpot() ){
                    dadosSpot.setVisibility(View.GONE);
                    dadosEndereco.setVisibility(View.VISIBLE);
                }
            }
        });
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dadosEndereco.setVisibility(View.GONE);
                dadosSpot.setVisibility(View.VISIBLE);

            }
        });
        proximo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaDadosEndereco()) {
                    dadosEndereco.setVisibility(View.GONE);
                    dadosImagens.setVisibility(View.VISIBLE);
                }
            }
        });
        voltar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dadosImagens.setVisibility(View.GONE);
                dadosEndereco.setVisibility(View.VISIBLE);
            }
        });
        proximo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dadosImagens.setVisibility(View.GONE);
                dadosFuncionamento.setVisibility(View.VISIBLE);
            }
        });
        voltar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dadosFuncionamento.setVisibility(View.GONE);
                dadosImagens.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean validaDadosSpot(){
        ValidacaoGui validacaoGui = new ValidacaoGui();
        if (!validacaoGui.verificarTamanhoCampo(nomeEspaco.getText().toString(), 4, 30)){
            nomeEspaco.setError("Campo obrigatório, mínimo 4 caracteres e máximo 30");
            return false;
        }
       // else if (!validacaoGui.verificarValor(valor.getText().toString())){
         //   valor.setError("O valor informado deve ser maior que 0");
           // return false;
        //}
        else if (!validacaoGui.verificarCampoEmail(email.getText().toString())){
            email.setError("Email inválido");
            return false;
        }
        else if (!validacaoGui.verificarTamanhoCampo(telefone.getText().toString(), 15, 15)){
            telefone.setError("Telefone inválido");
            return false;}
        return true;
    }

    private boolean validaHorarios() throws ParseException {
        ValidacaoGui validacaoGui = new ValidacaoGui();
        boolean result = true;
        if (horarioDomingo.isShown()){
            if(!validacaoGui.verificaHora(inicioDomingo, fimDomingo)){
                result = false;
            }
        }
        if (horarioSegunda.isShown()){
            if (!validacaoGui.verificaHora(inicioSegunda,fimSegunda)){
                result = false;
            }
        }
        if (horarioTerca.isShown()){
            if (!validacaoGui.verificaHora(inicioTerca,fimTerca)){
                result = false;
            }
        }
        if (horarioQuarta.isShown()){
            if (!validacaoGui.verificaHora(inicioQuarta,fimQuarta)){
                result = false;
            }
        }
        if (horarioQuinta.isShown()){
            if (!validacaoGui.verificaHora(inicioQuinta,fimQuinta)){
                result = false;
            }
        }
        if (horarioSexta.isShown()){
            if (!validacaoGui.verificaHora(inicioSexta,fimSexta)){
                result = false;
            }
        }
        if (horarioSabado.isShown()){
            if (!validacaoGui.verificaHora(inicioSabado,fimSabado)){
                result = false;
            }
        }
        return result;
    }

    private boolean validaDadosEndereco(){
        ValidacaoGui validacaoGui = new ValidacaoGui();
        if (!validacaoGui.verificarTamanhoCampo(cep.getText().toString(),9,9)){
            cep.setError("Cep inválido");
            return false;
        }
        else if (!validacaoGui.verificarTamanhoCampo(logradouro.getText().toString(),1, 50)){
            logradouro.setError("Logradouro inválido");
            return false;
        } else if (!validacaoGui.verificarTamanhoCampo(numero.getText().toString(), 1, 10)) {
            numero.setError("Número inválido");
            return false;
        }
        return true;
    }
    private void setarVariaveis(View inflate) {
        setControleCadastro(inflate);
        setDadosEspaco(inflate);
        setCamposEndereco(inflate);
        setImagens(inflate);
        setCamposFuncionamento(inflate);
        imagemAtual = "0";
    }

    private void setCamposFuncionamento(View inflate) {
        segunda = inflate.findViewById(R.id.switchSegunda);
        terca = inflate.findViewById(R.id.switchTerca);
        quarta = inflate.findViewById(R.id.switchQuarta);
        quinta = inflate.findViewById(R.id.switchQuinta);
        sexta = inflate.findViewById(R.id.switchSexta);
        sabado = inflate.findViewById(R.id.switchSabado);
        domingo = inflate.findViewById(R.id.switchDomingo);
        horarios = inflate.findViewById(R.id.horariosDiaSpot);
        horarioSegunda = inflate.findViewById(R.id.horarioSegundaSpot);
        horarioTerca = inflate.findViewById(R.id.horarioTercaSpot);
        horarioQuarta = inflate.findViewById(R.id.horarioQuartaSpot);
        horarioQuinta = inflate.findViewById(R.id.horarioQuintaSpot);
        horarioSexta = inflate.findViewById(R.id.horarioSextaSpot);
        horarioSabado = inflate.findViewById(R.id.horarioSabadoSpot);
        horarioDomingo = inflate.findViewById(R.id.horarioDomingoSpot);
        inicioSegunda = inflate.findViewById(R.id.horaInicialSegunda);
        inicioTerca = inflate.findViewById(R.id.horaInicialTerca);
        inicioQuarta = inflate.findViewById(R.id.horaInicialQuarta);
        inicioQuinta = inflate.findViewById(R.id.horaInicialQuinta);
        inicioSexta = inflate.findViewById(R.id.horaInicialSexta);
        inicioSabado = inflate.findViewById(R.id.horaInicialSabado);
        inicioDomingo = inflate.findViewById(R.id.horaInicialDomingo);
        fimSegunda = inflate.findViewById(R.id.horaFinalSegunda);
        fimTerca = inflate.findViewById(R.id.horaFinalTerca);
        fimQuarta = inflate.findViewById(R.id.horaFinalQuarta);
        fimQuinta = inflate.findViewById(R.id.horaFinalQuinta);
        fimSexta = inflate.findViewById(R.id.horaFinalSexta);
        fimSabado = inflate.findViewById(R.id.horaFinalSabado);
        fimDomingo = inflate.findViewById(R.id.horaFinalDomingo);
    }

    private void setDadosEspaco(View inflate) {
        nomeEspaco = inflate.findViewById(R.id.inputNomeEspaco);
        telefone = inflate.findViewById(R.id.inputTelefoneEspacos);
        email = inflate.findViewById(R.id.inputEmailEspacos);
        tipo = inflate.findViewById(R.id.tipo);
        valor = inflate.findViewById(R.id.inputValorEspacos);
    }

    private void setCamposEndereco(View inflate) {
        logradouro = inflate.findViewById(R.id.inputLogradouroEspacos);
        numero = inflate.findViewById(R.id.inputNumeroEspacos);
        //bairro = inflate.findViewById(R.id.inputBairroEspacos);
        autocompleteSupportFragment  = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        cidade = inflate.findViewById(R.id.inputCidadeEspacos);
        estado = inflate.findViewById(R.id.inputEstadoEspacos);
        cep = inflate.findViewById(R.id.inputCepEspacos);
    }

    private void setControleCadastro(View inflate) {
        dadosSpot = inflate.findViewById(R.id.dadosCadastroSpot);
        dadosEndereco = inflate.findViewById(R.id.dadosEnderecoSpot);
        dadosImagens = inflate.findViewById(R.id.addImagesSpot);
        dadosFuncionamento = inflate.findViewById(R.id.cadastroFuncionamentoSpot);
        proximo = inflate.findViewById(R.id.proximoCadastroSpot);
        proximo1 = inflate.findViewById(R.id.buttonProximoEspaco);
        proximo2 = inflate.findViewById(R.id.buttonProximoCriacaoImagensSpot);
        cancelar = inflate.findViewById(R.id.cancelarCadastroSpot);
        voltar = inflate.findViewById(R.id.buttonVoltarCadastroSpot);
        voltar1 = inflate.findViewById(R.id.buttonVoltarImagensSpot);
        voltar2 = inflate.findViewById(R.id.voltarCadastroSpot3);
        cadastrar = inflate.findViewById(R.id.cadastrarSpot);
    }

    private void setImagens(View inflate) {
        imageView = inflate.findViewById(R.id.ImageViewEspaco);
        imageView1 = inflate.findViewById(R.id.imageViewSpace1);
        imageView2 = inflate.findViewById(R.id.imageViewEspaco2);
        imageView3 = inflate.findViewById(R.id.imageViewEspaco3);
        imageView4 = inflate.findViewById(R.id.imageViewEspaco4);
        imageView5 = inflate.findViewById(R.id.imageViewEspaco5);
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
        params.put("city_id", String.valueOf(space.getAddress().getCity().getId()));
        params.put("street", space.getAddress().getStreet());
        params.put("cep", space.getAddress().getCep());
        params.put("number", String.valueOf(space.getAddress().getNumber()));
        params.put("neighborhood", autocompleteBairro);
        Log.d("cadastro", "cadastrarEspaco: "+autocompleteBairro);
        params.put("email", space.getEmail());
        params.put("phone", space.getPhone());
        params.put("price", String.valueOf(space.getPriceHour()));
        params.put("ground_id", space.getSpaceType().getDescription());
        Call<SpotCall> call = spaceInterface.registerSpace(auth,token,params);
        call.enqueue(new Callback<SpotCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                Log.d("resposta", "cadastro space: "+resposta.getSpot_id());
//                galeria.forEach((n) -> cadastrarFoto(String.valueOf(space.getId()), n));
                if(segunda.isChecked()){
                    cadastrarFuncionamento("2",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(terca.isChecked()){
                    cadastrarFuncionamento("3",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(quarta.isChecked()){
                    cadastrarFuncionamento("4",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(quinta.isChecked()){
                    cadastrarFuncionamento("5",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(sexta.isChecked()){
                    cadastrarFuncionamento("6",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(sabado.isChecked()){
                    cadastrarFuncionamento("7",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                if(domingo.isChecked()){
                    cadastrarFuncionamento("1",inicioSegunda.getText().toString(),
                            fimSegunda.getText().toString(),String.valueOf(space.getId()));
                }
                Fragment fragment = new ListSpacesFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<SpotCall> call, Throwable t) {
                Log.d("resposta", "cadastro space: "+t);
                Toast.makeText(getContext(),"Local Não Cadastrado",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cadastrarFuncionamento(String dia, String inicio, String fim, String id) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ScheduleInterface bookingInterface = retrofit.create(ScheduleInterface.class);
        User usuario = Sessao.getSessao(getContext());
        String credentials = usuario.getUsername()+":"+
                usuario.getPassword();
        final String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        final String token = Sessao.getSessao(getContext()).getToken();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("week_day", dia);
        params.put("opening_time", inicio);
        params.put("closing_time", fim);
        params.put("spot_id", id);
        Call<SetCall> call = bookingInterface.registrarSchedule(auth,token,params);
        call.enqueue(new Callback<SetCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro Horario: "+response);
                    return;
                }
                SetCall resposta = response.body();
            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
                Log.d("resposta", "cadastro Horario: "+t);
            }
        });
    }

    private void cadastrarFoto(String id, byte[] photo){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PhotoInterface photoInterface = retrofit.create(PhotoInterface.class);
        User usuario = Sessao.getSessao(getContext());
        String credentials = usuario.getUsername()+":"+
                usuario.getPassword();
        final String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        final String token = Sessao.getSessao(getContext()).getToken();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("spot_id", id);
        params.put("image", new String(photo));
        Call<SetCall> call = photoInterface.registrarPhoto(auth, token, params);
        call.enqueue(new Callback<SetCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro imagem: "+response);
                    return;
                }
                SetCall resposta = response.body();
            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
                Log.d("resposta", "cadastro Imagem: "+t);
            }
        });
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
                        Drawable d = imageView.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();
                        galeria.add(bitmapdata);
                        break;
                    case "1":
                        imageView1.setImageBitmap(bitmaps);
                        Drawable d1 = imageView1.getDrawable();
                        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                        byte[] bitmapdata1 = stream1.toByteArray();
                        galeria.add(bitmapdata1);
                        break;
                    case "2":
                        imageView2.setImageBitmap(bitmaps);
                        Drawable d2 = imageView1.getDrawable();
                        Bitmap bitmap2 = ((BitmapDrawable)d2).getBitmap();
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                        byte[] bitmapdata2 = stream2.toByteArray();
                        galeria.add(bitmapdata2);
                        break;
                    case "3":
                        imageView3.setImageBitmap(bitmaps);
                        Drawable d3 = imageView1.getDrawable();
                        Bitmap bitmap3 = ((BitmapDrawable)d3).getBitmap();
                        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
                        byte[] bitmapdata3 = stream3.toByteArray();
                        galeria.add(bitmapdata3);
                        break;
                    case "4":
                        imageView4.setImageBitmap(bitmaps);
                        Drawable d4 = imageView1.getDrawable();
                        Bitmap bitmap4 = ((BitmapDrawable)d4).getBitmap();
                        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
                        bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
                        byte[] bitmapdata4 = stream4.toByteArray();
                        galeria.add(bitmapdata4);
                        break;
                    case "5":
                        imageView5.setImageBitmap(bitmaps);
                        Drawable d5 = imageView1.getDrawable();
                        Bitmap bitmap5 = ((BitmapDrawable)d5).getBitmap();
                        ByteArrayOutputStream stream5 = new ByteArrayOutputStream();
                        bitmap5.compress(Bitmap.CompressFormat.JPEG, 100, stream5);
                        byte[] bitmapdata5 = stream5.toByteArray();
                        galeria.add(bitmapdata5);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkRadio(){
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        radioButon = (RadioButton) radioGroup.getChildAt(idx);
        radioGroup.check(idx);
    }

//        //validação do autocomplete
//        if (autocompleteBairro.isEmpty()){
//            return false;

    private Space createSpace(){
        Space space = new Space();
        space.setName(nomeEspaco.getText().toString());
        space.setPhone(telefone.getText().toString());
        space.setEmail(email.getText().toString());
        space.setPriceHour(new BigDecimal(valor.getText().toString()));
        space.setAddress(createAddress());
        switch (tipo.getSelectedItem().toString()){
            case "Tipo do Espaço":
                TextView errorText = (TextView)tipo.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText(R.string.errorTipo);
            case "Quadra":
                space.setSpaceType(EnumSpaceType.COURT);
            case "Grama":
                space.setSpaceType(EnumSpaceType.GRASS);
            case "Terra":
                space.setSpaceType(EnumSpaceType.EARTH);
            case "Grama Sintetica":
                space.setSpaceType(EnumSpaceType.SOCIETY);
        }
        return space;
    }

    private Address createAddress(){
        Address address = new Address();
        address.setCep(cep.getText().toString());
        address.setNeighboorhood(autocompleteBairro);
        address.setNumber(Integer.parseInt(numero.getText().toString()));
        State state = new State();
        state.setName(estado.getSelectedItem().toString());
        City city = new City();
        city.setName(cidade.getSelectedItem().toString());
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

    private void openAutoComplete() {

        //String apiKey = "AIzaSyDv8_UTCi8upqXd85vEn3mz1s-FZYBXTg8";
        String apiKey = "AIzaSyC9SolfTyKKTNoBHgbAo4GIjRtLna0wvDE";

        if(!Places.isInitialized()){
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(getActivity().getApplicationContext());

        //final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.values()));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
