package com.example.foodtruck.Models;

public class OrderedItem {
    private long m_id;
    private String m_Quantity;
    private Item m_Item;
    private Order m_Order;

    public OrderedItem() {
    }

    public long getM_id() {
        return m_id;
    }

    public void setM_id(long m_id) {
        this.m_id = m_id;
    }

    public String getM_Quantity() {
        return m_Quantity;
    }

    public void setM_Quantity(String m_Quantity) {
        this.m_Quantity = m_Quantity;
    }

    public Item getM_Item() {
        return m_Item;
    }

    public void setM_Item(Item m_Item) {
        this.m_Item = m_Item;
    }

    public Order getM_Order() {
        return m_Order;
    }

    public void setM_Order(Order m_Order) {
        this.m_Order = m_Order;
    }
}
