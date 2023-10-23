package com.example.food_ordering;

public class Dish {
    private String name;
    private String price;
    private String image;

    public Dish(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}

