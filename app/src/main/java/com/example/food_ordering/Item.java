package com.example.food_ordering;

public class Item {
    String cart_name, cart_price, cart_remarks, cart_image, payment_status;
    long cart_quantity;

    public Item(){}

    public Item(String cart_name, String cart_price, String cart_remarks, long cart_quantity, String payment_status) {
        this.cart_name = cart_name;
        this.cart_price = cart_price;
        this.cart_remarks = cart_remarks;
        this.cart_quantity = cart_quantity;
        this.payment_status = payment_status;
    }

    public String getCart_name() {
        return cart_name;
    }

    public void setCart_name(String cart_name) {
        this.cart_name = cart_name;
    }

    public String getCart_price() {
        return cart_price;
    }

    public void setCart_price(String cart_price) {
        this.cart_price = cart_price;
    }

    public String getCart_remarks() {
        return cart_remarks;
    }

    public void setCart_remarks(String cart_remarks) {
        this.cart_remarks = cart_remarks;
    }

    public long getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(long cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getCart_image() {
        return cart_image;
    }

    public void setCart_image(String cart_image) {
        this.cart_image = cart_image;
    }

    public String getPayment_status() { return payment_status; }

    public void setPayment_status(String payment_status) { this.payment_status = payment_status; }
}
