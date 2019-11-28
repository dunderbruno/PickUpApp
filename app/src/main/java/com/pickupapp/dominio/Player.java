package com.pickupapp.dominio;

public class Player {

    private String id;
    private String name;
    private String surname;
    private String matches_count;
    private String average_rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMatches_count() {
        return matches_count;
    }

    public void setMatches_count(String matches_count) {
        this.matches_count = matches_count;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
