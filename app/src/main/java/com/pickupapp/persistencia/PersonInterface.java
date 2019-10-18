package com.pickupapp.persistencia;

import com.pickupapp.dominio.Person;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PersonInterface {

    @POST("person")
    Call<Person> criarPessoa(@Body Map<String,String> parameters);

    @POST("user/{id}/person")
    Call<Person> setPessoaUsuario(@Body Map<String,String> parameters, @Path("id") int id);

    @GET("person")
    Call<Person> getPessoa();
}
