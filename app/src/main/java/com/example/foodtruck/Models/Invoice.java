package com.example.foodtruck.Models;

public class Invoice {
    private long m_Id;
    private String m_InvoiceDate;
    private String m_Total;
    private String m_ServiceCharge;
    private String m_TaxAmount;
    private String m_TotalInvoiceAmount;
    private Order m_Order;
    private Payment m_Payment;
    private Customer m_Customer;

    public Invoice() {
    }

    public long getM_Id() {
        return m_Id;
    }
    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_InvoiceDate() {
        return m_InvoiceDate;
    }
    public void setM_InvoiceDate(String m_InvoiceDate) {
        this.m_InvoiceDate = m_InvoiceDate;
    }

    public String getM_Total() {
        return m_Total;
    }
    public void setM_Total(String m_Total) {
        this.m_Total = m_Total;
    }

    public String getM_ServiceCharge() {
        return m_ServiceCharge;
    }
    public void setM_ServiceCharge(String m_ServiceCharge) {
        this.m_ServiceCharge = m_ServiceCharge;
    }

    public String getM_TaxAmount() {
        return m_TaxAmount;
    }
    public void setM_TaxAmount(String m_TaxAmount) {
        this.m_TaxAmount = m_TaxAmount;
    }

    public String getM_TotalInvoiceAmount() {
        return m_TotalInvoiceAmount;
    }
    public void setM_TotalInvoiceAmount(String m_TotalInvoiceAmount) {
        this.m_TotalInvoiceAmount = m_TotalInvoiceAmount;
    }

    public Order getM_Order() {
        return m_Order;
    }
    public void setM_Order(Order m_Order) {
        this.m_Order = m_Order;
    }

    public Payment getM_Payment() {
        return m_Payment;
    }
    public void setM_Payment(Payment m_Payment) {
        this.m_Payment = m_Payment;
    }

    public Customer getM_Customer() {
        return m_Customer;
    }
    public void setM_Customer(Customer m_Customer) {
        this.m_Customer = m_Customer;
    }
}
