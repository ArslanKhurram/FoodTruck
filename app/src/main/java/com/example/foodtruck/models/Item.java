package com.example.foodtruck.models;

import java.util.Arrays;

public class Item {

    private long m_Id;
    private String m_Name;
    private String m_Price;
    private String m_Available;
    private byte[] m_Image;
    private Menu m_Menu;

    //default constructor
    public Item() {};

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public String getM_Price() {
        return m_Price;
    }

    public void setM_Price(String m_Price) {
        this.m_Price = m_Price;
    }

    public String getM_Available() {
        return m_Available;
    }

    public void setM_Available(String m_Available) {
        this.m_Available = m_Available;
    }

    public byte[] getM_Image() {
        return m_Image;
    }

    public void setM_Image(byte[] m_Image) {
        this.m_Image = m_Image;
    }

    public Menu getM_Menu() {
        return m_Menu;
    }

    public void setM_Menu(Menu m_Menu) {
        this.m_Menu = m_Menu;
    }

    @Override
    public String toString() {
        return "Item{" +
                "m_Id=" + m_Id +
                ", m_Name='" + m_Name + '\'' +
                ", m_Price='" + m_Price + '\'' +
                ", m_Available='" + m_Available + '\'' +
                ", m_Menu For=" + m_Menu.getM_Id() +
                '}';
    }
}
