package com.example.food_ordering;

public class Confirmation {

    String cart_name, cart_price, cart_remarks;
    long cart_quantity;

    public Confirmation(){}

    public Confirmation(String cart_name, String cart_price, String cart_remarks, long cart_quantity) {
        this.cart_name = cart_name;
        this.cart_price = cart_price;
        this.cart_remarks = cart_remarks;
        this.cart_quantity = cart_quantity;
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
}
