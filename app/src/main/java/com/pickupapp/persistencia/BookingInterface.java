package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.BookingsCall;
import com.pickupapp.persistencia.retorno.SetCall;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookingInterface {

    @POST("booking")
    Call<SetCall> registerBooking(@Header("Authorization") String token,
                                  @Header("x-access-token") String token1,
                                  @Body Map<String,String> parameters);

    @GET("/spot/{spot_id}/booking")
    Call<BookingsCall> getBooking(@Header("x-access-token") String token1,
                                  @Path("spot_id") String spot);

    @GET("/booking/my")
    Call<BookingsCall> getmyBooking(@Header("x-access-token") String token1);

    @GET("/booking/my/owner")
    Call<BookingsCall> getmyBookingSpots(@Header("x-access-token") String token1);

}
