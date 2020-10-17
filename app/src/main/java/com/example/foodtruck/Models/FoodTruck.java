package com.example.foodtruck.Models;

public class FoodTruck {

    private long m_ID;
    private String m_Name;
    private String m_Category;
    private byte[] m_Image;
    private double m_Latitude;
    private double m_Longitude;
    private Vendor m_Vendor;

    public FoodTruck() { }

    public long getM_ID() {
        return m_ID;
    }

    public void setM_ID(long m_ID) {
        this.m_ID = m_ID;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public String getM_Category() {
        return m_Category;
    }

    public void setM_Category(String m_Category) {
        this.m_Category = m_Category;
    }

    public byte[] getM_Image() {
        return m_Image;
    }

    public void setM_Image(byte[] m_Image) {
        this.m_Image = m_Image;
    }

    public double getM_Latitude() {
        return m_Latitude;
    }

    public void setM_Latitude(double m_Latitude) {
        this.m_Latitude = m_Latitude;
    }

    public double getM_Longitude() {
        return m_Longitude;
    }

    public void setM_Longitude(double m_Longitude) {
        this.m_Longitude = m_Longitude;
    }

    public Vendor getM_Vendor() {
        return m_Vendor;
    }

    public void setM_Vendor(Vendor m_Vendor) {
        this.m_Vendor = m_Vendor;
    }

    @Override
    public String toString() {
        return "FoodTruck{" +
                "m_ID=" + m_ID +
                ", m_Name='" + m_Name + '\'' +
                ", m_Category='" + m_Category + '\'' +
                ", m_Vendor=" + m_Vendor.getM_FirstName() + " " + m_Vendor.getM_LastName() +
                '}';
    }

}
