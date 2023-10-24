package com.example.food_ordering;

public class User {
    private String name;
    private String id;
    private String email;
    private String contact;
    private String password;


    public User(String name, String id, String email, String contact, String password) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getPassword() {
        return password;
    }
}

