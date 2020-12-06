package com.example.foodtruck.Models;

public class CartOptions {
    private long m_Id;
    private Cart m_Cart;
    private Item m_itemId;
    private Option m_Option;


    public CartOptions(){}

    public long getM_Id(){return m_Id;}
    public void setM_Id(long m_Id){this.m_Id = m_Id;}

    public Cart getM_Cart() {
        return m_Cart;
    }

    public void setM_Cart(Cart m_Cart) {
        this.m_Cart = m_Cart;
    }

    public Item getM_itemId(){return m_itemId;}
    public void setM_itemId(Item itemId){this.m_itemId = itemId;}

    public Option getM_Option() {
        return m_Option;
    }

    public void setM_Option(Option m_Option) {
        this.m_Option = m_Option;
    }
}
