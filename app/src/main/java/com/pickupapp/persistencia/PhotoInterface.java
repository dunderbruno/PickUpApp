package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.SetCall;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PhotoInterface {

    @Multipart
    @POST("spot/{spot_id}/photo")
    Call<SetCall> registrarPhoto(@Header("Authorization") String token,
                                 @Header("x-access-token") String token1,
                                 @Part MultipartBody.Part file,
                                 @Path("spot_id") String spot);
}
