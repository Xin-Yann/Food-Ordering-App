package com.example.food_ordering;

public class AdminMenu {
    String menu_name;
    String menu_price;
    String menu_image;
    String menu_detail;
    String menu_id;

    public AdminMenu(String menu_name, String menu_price, String menu_image, String menu_detail, String menu_id) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_image = menu_image;
        this.menu_detail = menu_detail;
        this.menu_id = menu_id;
    }

    public String getName() {
        return menu_name;
    }

    public String getPrice() {
        return menu_price;
    }

    public String getImage() {
        return menu_image;
    }

    public String getDetail() {
        return menu_detail;
    }

    public String getId() {
        return menu_id;
    }
}