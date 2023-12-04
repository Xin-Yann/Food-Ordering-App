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
    private String totalamountText;

    private String email;
    private String remarks;

    public AdminOrderHistory(String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumber, String pickupTime, String orderStatus, String paymentMethod, String pickupedOn, String email, String totalamountText, String remarks) {
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumber = orderNumber;
        this.pickupTime = pickupTime;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.totalamountText = totalamountText;
        this.pickupedOn = pickupedOn;
        this.email = email;
        this.remarks = remarks;
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

    public String getTotalamountText() {return totalamountText;}

    public String getPickupedOn() {
        return pickupedOn;
    }

    public String getemail() {
        return email;
    }

    public String getRemarkText() {return remarks;}
}


