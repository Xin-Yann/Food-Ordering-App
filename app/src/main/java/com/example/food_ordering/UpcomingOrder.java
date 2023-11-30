package com.example.food_ordering;

public class UpcomingOrder {
    private String documentId;
    private String foodImgUrl;
    private String foodDetailsText;
    private double priceValue;
    private int foodQuantity;
    private String orderNumberText;
    private String pickupTimeText;
    private String orderStatusText;
    private String paymentMethodText;
    private String totalamountText;
    private String remarkText;



    // Add a no-argument constructor
    public UpcomingOrder() {
        // Default constructor required for Firebase
    }

    public UpcomingOrder(String documentId, String foodImgUrl, String foodDetailsText, int foodQuantity, double priceValue, String orderNumberText, String pickupTimeText, String orderStatusText, String paymentMethodText,String totalamountText, String remarks) {
        this.documentId = documentId;
        this.foodImgUrl = foodImgUrl;
        this.foodDetailsText = foodDetailsText;
        this.foodQuantity = foodQuantity;
        this.priceValue = priceValue;
        this.orderNumberText = orderNumberText;
        this.pickupTimeText = pickupTimeText;
        this.orderStatusText = orderStatusText;
        this.paymentMethodText = paymentMethodText;
        this.totalamountText = totalamountText;
        this.remarkText = remarks;
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

    public String getTotalAmountText() {
        return totalamountText;
    }

    public String getRemarkText() {
        return remarkText;
    }
}