package com.example.foodtruck;

public class PaymentCard {
    private int imResource;
    private String paymentName, paymentNumber;

    public PaymentCard(int imageResource, String name, String number) {
        imResource = imageResource;
        paymentName = name;
        paymentNumber = number;
    }

    public int getPaymentImage() { return imResource; }

    public String getPaymentName() { return paymentName; }

    public String getPaymentNumber() { return paymentNumber; }


}
