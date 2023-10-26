package com.example.food_ordering;

public class Menu {
    private String name;
    private String price;
    private String image;
    private String detail;

    public Menu(String name, String price, String image, String detail) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.detail = detail;
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

}
