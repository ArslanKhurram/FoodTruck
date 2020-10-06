package com.example.foodtruck.Models;

public class Menu {

    private long m_Id;
    private FoodTruck m_FoodTruck;

    public Menu() {
    }

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public FoodTruck getM_FoodTruck() {
        return m_FoodTruck;
    }

    public void setM_FoodTruck(FoodTruck m_FoodTruck) {
        this.m_FoodTruck = m_FoodTruck;
    }
}
