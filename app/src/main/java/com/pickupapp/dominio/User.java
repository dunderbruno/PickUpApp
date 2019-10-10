package com.pickupapp.dominio;

public class User {
    private int id;
    private String username;
    private String token;
    private String password;
    private Person person;
    private Contact contact;
    private Group group;

    public User(){
        token = "";
        id = 0;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Person getPerson() {
        return person;
    }

    public Contact getContact() {
        return contact;
    }

    public Group getGroup() {
        return group;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
