package com.example.foodtruck.models;

public class Payment {

    private long m_ID;
    private String m_PaymentType;
    private String m_NameOnCard;
    private String m_CreditCardNumber;
    private String m_CCEXPDATE;
    private String m_CCV;
    private String m_DateAdded;

    //default constructor
    public Payment() {};

    public long getM_ID() {
        return m_ID;
    }

    public void setM_ID(long m_ID) {
        this.m_ID = m_ID;
    }

    public String getM_PaymentType() {
        return m_PaymentType;
    }

    public void setM_PaymentType(String m_PaymentType) {
        this.m_PaymentType = m_PaymentType;
    }

    public String getM_NameOnCard() {
        return m_NameOnCard;
    }

    public void setM_NameOnCard(String m_NameOnCard) {
        this.m_NameOnCard = m_NameOnCard;
    }

    public String getM_CreditCardNumber() {
        return m_CreditCardNumber;
    }

    public void setM_CreditCardNumber(String m_CreditCardNumber) {
        this.m_CreditCardNumber = m_CreditCardNumber;
    }

    public String getM_CCEXPDATE() {
        return m_CCEXPDATE;
    }

    public void setM_CCEXPDATE(String m_CCEXPDATE) {
        this.m_CCEXPDATE = m_CCEXPDATE;
    }

    public String getM_CCV() {
        return m_CCV;
    }

    public void setM_CCV(String m_CCV) {
        this.m_CCV = m_CCV;
    }

    public String getM_DateAdded() {
        return m_DateAdded;
    }

    public void setM_DateAdded(String m_DateAdded) {
        this.m_DateAdded = m_DateAdded;
    }
}
