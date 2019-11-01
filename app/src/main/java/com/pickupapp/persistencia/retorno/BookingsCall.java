package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.Booking;

import java.util.ArrayList;

public class BookingsCall {

    @SerializedName("bookings")
    private ArrayList<Booking> bookings;

    public ArrayList<Booking> getBookings() {
        return bookings;
    }
}
