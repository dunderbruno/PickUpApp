package com.pickupapp.dominio;

import com.pickupapp.infra.EnumSpaceType;

import java.math.BigDecimal;

public class Space {
    private long id;
    private String name;
    private String phone;
    private String email;
    private Address address;
    private BigDecimal priceHour;
    private EnumSpaceType spaceType;

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
}
