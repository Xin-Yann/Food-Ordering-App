package com.example.food_ordering;

public class AdminMenu {
    private String name;
    private String price;
    private String image;
    private String detail;
    private String id;

    public AdminMenu(String name, String price, String image, String detail, String id) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.detail = detail;
        this.id = id;
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

    public String getDetail() {
        return detail;
    }

    public String getId() {
        return id;
    }

}
