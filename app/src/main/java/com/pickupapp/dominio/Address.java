package com.pickupapp.dominio;

import com.google.gson.annotations.SerializedName;

public class Address {

    private long id;
    private City cityObj;
    @SerializedName("city")
    private String cityCall;
    private String street;
    private String neighborhood;
    private int number;
    private String cep;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public City getCityObj() {
        return cityObj;
    }

    public void setCityObj(City cityObj) {
        this.cityObj = cityObj;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighboorhood() {
        return neighborhood;
    }

    public void setNeighboorhood(String neighboorhood) {
        this.neighborhood = neighboorhood;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString(){
        return this.getStreet() + " " + this.getNumber() + ",  " + this.getNeighboorhood() + " - " + this.getCityCall()+ " - " + this.getState();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityCall() {
        return cityCall;
    }

    public void setCityCall(String cityCall) {
        this.cityCall = cityCall;
    }
}
