package com.example.food_ordering;

public class CartReport {

    String cart_name, cart_price, payment_status;
    long cart_quantity;

    public CartReport(){}

    public CartReport(String cart_name, String cart_price, long cart_quantity, String payment_status) {
        this.cart_name = cart_name;
        this.cart_price = cart_price;
        this.payment_status = payment_status;
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

    public long getCart_quantity() { return cart_quantity; }

    public void setCart_quantity(long cart_quantity) { this.cart_quantity = cart_quantity; }

    public void setCart_price(String cart_price) {
        this.cart_price = cart_price;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
