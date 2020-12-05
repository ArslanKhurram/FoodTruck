package com.example.foodtruck.Models;

public class CartItemOption {
    private long m_Id;
    private Customer m_Customer;
    private Item m_itemId;
    private Option m_OptionId;


    public CartItemOption(){}

    public long getM_Id(){return m_Id;}
    public void setM_Id(long m_Id){this.m_Id = m_Id;}

    public Customer getCustId(){return m_Customer;}
    public void setM_Customer(Customer cust_Id){this.m_Customer = cust_Id; }

    public Item getM_itemId(){return m_itemId;}
    public void setM_itemId(Item itemId){this.m_itemId = itemId;}

    public Option getM_OptionId(){return m_OptionId;}
    public void setM_OptionId(Option optionId){this.m_OptionId = optionId;}
}
