package com.pickupapp.dominio;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.infra.EnumSpaceType;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Space {

    private long id;
    private String name;
    private String phone;
    private String email;
    @SerializedName("address")
    private Address address;

    @SerializedName("price")
    private BigDecimal priceHour;
    private EnumSpaceType spaceType;
    private ArrayList<Schedule> schedule;

    public EnumSpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(EnumSpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;

    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(BigDecimal priceHour) {
        this.priceHour = priceHour;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public ArrayList<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<Schedule> schedule) {
        this.schedule = schedule;
    }
}
