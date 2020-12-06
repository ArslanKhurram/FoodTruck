package com.example.foodtruck.Models;

public class CartOptions {
    private long m_Id;
    private Customer m_Customer;
    private Item m_itemId;
    private long m_cartId;


    public CartOptions(){}

    public long getM_Id(){return m_Id;}
    public void setM_Id(long m_Id){this.m_Id = m_Id;}

    public Customer getCustId(){return m_Customer;}
    public void setM_Customer(Customer cust_Id){this.m_Customer = cust_Id; }

    public Item getM_itemId(){return m_itemId;}
    public void setM_itemId(Item itemId){this.m_itemId = itemId;}

    public long getM_cartId() {
        return m_cartId;
    }

    public void setM_cartId(long m_cartId) {
        this.m_cartId = m_cartId;
    }
}
