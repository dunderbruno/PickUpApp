package com.pickupapp.dominio;

public class Address {
    private long id;
    private City city;
    private String street;
    private String neighboorhood;
    private int number;
    private String cep;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighboorhood() {
        return neighboorhood;
    }

    public void setNeighboorhood(String neighboorhood) {
        this.neighboorhood = neighboorhood;
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
        return "Rua " + this.getStreet() + " número " + this.getNumber() + ", bairro " + this.getNeighboorhood();
    }
}
