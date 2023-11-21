package com.example.food_ordering;

public class AdminMenu {
    String main_name;
    String main_price;
    String main_image;
    String main_detail;
    String main_id;

    public AdminMenu(){}

    public AdminMenu(String main_name, String main_price, String main_image, String main_detail, String main_id) {
        this.main_name = main_name;
        this.main_price = main_price;
        this.main_image = main_image;
        this.main_detail = main_detail;
        this.main_id = main_id;
    }

    public String getName() {
        return main_name;
    }

    public String getPrice() {
        return main_price;
    }

    public String getImage() {
        return main_image;
    }

    public String getDetail() {
        return main_detail;
    }

    public String getId() {
        return main_id;
    }

}
