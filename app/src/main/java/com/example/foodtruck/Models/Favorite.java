package com.example.foodtruck.Models;

public class Favorite {
    private long m_Id;
    private FoodTruck m_FoodTruckID;
    private Customer m_CustomerID;

    public Favorite() {
    }

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public FoodTruck getM_FoodTruckID() {
        return m_FoodTruckID;
    }

    public void setM_FoodTruckID(FoodTruck m_FoodTruckID) {
        this.m_FoodTruckID = m_FoodTruckID;
    }

    public Customer getM_CustomerID() {
        return m_CustomerID;
    }

    public void setM_CustomerID(Customer m_CustomerID) {
        this.m_CustomerID = m_CustomerID;
    }
}
