package com.pickupapp.dominio;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("person_id")
    private int id;

    private String name;

    private String surname;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
