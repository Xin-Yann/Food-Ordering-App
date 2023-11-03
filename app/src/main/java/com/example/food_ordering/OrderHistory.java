package com.example.food_ordering;

public class OrderHistory {
    private String documentId;
    private String foodImgUrl;
    private String foodDetailsText;
    private double priceValue;
    private int foodQuantity;
    private String orderNumberText;
    private String pickupTimeText;
    private String orderStatusText;
    private String paymentMethodText;

    private String pickupedOnText;

    // Add a no-argument constructor
    public OrderHistory() {
        // Default constructor required for Firebase
    }

    public OrderHistory(String documentId, String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumberText, String pickupTimeText, String orderStatusText, String paymentMethodText, String pickupedOnText) {
        this.documentId = documentId;
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumberText = orderNumberText;
        this.pickupTimeText = pickupTimeText;
        this.orderStatusText = orderStatusText;
        this.paymentMethodText = paymentMethodText;
        this.pickupedOnText = pickupedOnText;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public String getFoodDetailsText() {
        return foodDetailsText;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public int getFoodQuantity() {
        return foodQuantity;
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

    public String getPickupedOnText() { return pickupedOnText;}

}

