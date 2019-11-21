package com.pickupapp.gui.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.PhotoInterface;
import com.pickupapp.persistencia.retorno.SetCall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {
    private TextView nome, contato;
    private Button salvar;
    private ImageView imagemPerfil;
    private static final int RESULT_LOAD_IMAGE = 1;
    private ArrayList<Uri> galeria = new ArrayList<Uri>();



    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ToDo função get user para carregar os dados e para poder salvar a imagem
        View inflate = inflater.inflate(R.layout.fragment_perfil, container, false);
        nome = inflate.findViewById(R.id.nome_jogador);
        contato = inflate.findViewById(R.id.contato);
        imagemPerfil = inflate.findViewById(R.id.profile_image);
        String name = "Jogador teste";
        nome.setText(name);
        salvar = inflate.findViewById(R.id.SalvarImagemJogador);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Uri n : galeria) {
                    try {
                        cadastrarFoto(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarImagem("0");
            }
        });
        return inflate;
    }

    private void buscarImagem(String posicao) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void cadastrarFoto(Uri photo) throws IOException {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PhotoInterface photoInterface = retrofit.create(PhotoInterface.class);
        User usuario = Sessao.getSessao(getContext());
        final String token = Sessao.getSessao(getContext()).getToken();
        File file = new File(getRealPathFromURI(photo));
        File compressedImageFile = new Compressor(getContext()).setQuality(50).compressToFile(file);
        Log.d("resposta", "caminhoFoto: " + getRealPathFromURI(photo));
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), compressedImageFile);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(),requestFile);
        Call<SetCall> call = photoInterface.registrarPhotoPlayer(token, multipartBody, String.valueOf(usuario.getId()));
        call.enqueue(new Callback<SetCall>() {
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
                imagemPerfil.setImageBitmap(bitmaps);
                Drawable d = imagemPerfil.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                galeria.add(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        Cursor cursor = null;
        try {

            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(contentUri,  proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }
    }

}
