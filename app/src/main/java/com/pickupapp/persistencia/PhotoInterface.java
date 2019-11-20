package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.SpotPhotosCall;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET("/spot/{spot_id}/photo/list")
    Call<SpotPhotosCall> getSpotPhotos(@Header("Authorization") String token,
                                       @Header("x-access-token") String token1,
                                       @Path("spot_id") String spot);

    @GET("/photo/{filename}")
    Call<ResponseBody> getPhoto(@Header("x-access-token") String token1,
                                @Path("filename") String photo);
}
