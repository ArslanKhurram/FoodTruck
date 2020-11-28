package com.example.foodtruck.Models;

public class OrderedItemOptions {
    private long m_id;
    private Item m_Item;
    private Option m_Option;
    private OrderedItem m_OrderedItem;

    public OrderedItemOptions() {
    }

    public long getM_id() {
        return m_id;
    }

    public void setM_id(long m_id) {
        this.m_id = m_id;
    }

    public Option getM_Option() {
        return m_Option;
    }

    public void setM_Option(Option m_Option) {
        this.m_Option = m_Option;
    }

    public Item getM_Item() {
        return m_Item;
    }

    public void setM_Item(Item m_Item) {
        this.m_Item = m_Item;
    }

    public OrderedItem getM_OrderItem() {
        return m_OrderedItem;
    }

    public void setM_OrderedItem(OrderedItem m_OrderedItem) {
        this.m_OrderedItem = m_OrderedItem;
    }

}
