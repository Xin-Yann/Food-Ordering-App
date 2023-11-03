package com.example.food_ordering;



public class AdminOrderHistory {
    private String foodImgUrl;
    private String foodDetailsText;
    private int foodQuantity;
    private double priceValue;
    private String orderNumber;
    private String pickupTime;
    private String orderStatus;
    private String paymentMethod;
    private String pickupedOn;

    public AdminOrderHistory(String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumber, String pickupTime, String orderStatus, String paymentMethod, String pickupedOn) {
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumber = orderNumber;
        this.pickupTime = pickupTime;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.pickupedOn = pickupedOn;
    }

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

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPickupedOn() {
        return pickupedOn;
    }
}


