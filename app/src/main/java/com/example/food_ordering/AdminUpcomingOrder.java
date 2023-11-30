package com.example.food_ordering;

public class AdminUpcomingOrder {
    private String documentId;
    private String foodImgUrl;
    private String foodDetailsText;
    private int foodQuantity;
    private double priceValue;
    private String orderNumberText;
    private String pickupTimeText;
    private String orderStatusText;
    private String paymentMethodText;

    private String totalPrice;

    private String remarks;

    private String email;

    public AdminUpcomingOrder(String documentId, String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumberText, String pickupTimeText, String orderStatusText, String paymentMethodText, String email, String totalPrice, String remarks) {
        this.documentId = documentId;
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumberText = orderNumberText;
        this.pickupTimeText = pickupTimeText;
        this.orderStatusText = orderStatusText;
        this.paymentMethodText = paymentMethodText;
        this.totalPrice = totalPrice;
        this.email = email;
        this.remarks = remarks;
    }

    public String getDocumentId() {
        return documentId;
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

    public String getOrderNumberText() {
        return orderNumberText;
    }

    public String getPickupTimeText() {
        return pickupTimeText;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public String getPaymentMethodText() {return paymentMethodText;}

    public String getTotalamountText() {return totalPrice;}

    public String getemail() {return email;}

    public String getRemarkText() {return remarks;}}
