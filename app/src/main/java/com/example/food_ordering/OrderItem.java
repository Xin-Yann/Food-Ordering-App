package com.example.food_ordering;

public class OrderItem {
    private String foodImgUrl;
    private String foodDetailsText;
    private int foodQuantity;
    private double priceValue;
    private String orderNumberText;
    private String pickupTimeText;
    private String orderStatusText;
    private String paymentMethodText;

    public OrderItem(String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumberText, String pickupTimeText, String orderStatusText, String paymentMethodText) {
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumberText = orderNumberText;
        this.pickupTimeText = pickupTimeText;
        this.orderStatusText = orderStatusText;
        this.paymentMethodText = paymentMethodText;
    }

    // Getters for your fields
    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public String getFoodDetailsText() {
        return foodDetailsText;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public String getOrderNumberText() {
        return orderNumberText;
    }

    public String getPickupTimeText() {
        return pickupTimeText;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public String getPaymentMethodText() {
        return paymentMethodText;
    }
}
