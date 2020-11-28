package com.example.foodtruck.Models;

public class Rating {
    private long m_Id;
    private String m_Rating;
    private String m_Title;
    private String m_Detail;
    private FoodTruck m_FoodTruck;
    private Customer m_Customer;

    //Default Customer
    public Rating() {
    }

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_Rating() {
        return m_Rating;
    }

    public void setM_Rating(String m_Rating) {
        this.m_Rating = m_Rating;
    }

    public String getM_Title() {
        return m_Title;
    }

    public void setM_Title(String m_Title) {
        this.m_Title = m_Title;
    }

    public String getM_Detail() {
        return m_Detail;
    }

    public void setM_Detail(String m_Detail) {
        this.m_Detail = m_Detail;
    }

    public FoodTruck getM_FoodTruck() {
        return m_FoodTruck;
    }

    public void setM_FoodTruck(FoodTruck m_FoodTruck) {
        this.m_FoodTruck = m_FoodTruck;
    }

    public Customer getM_Customer() {
        return m_Customer;
    }

    public void setM_Customer(Customer m_Customer) {
        this.m_Customer = m_Customer;
    }
}
