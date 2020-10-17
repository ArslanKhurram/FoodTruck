package com.example.foodtruck.Models;

public class Admin {
    private long m_Id;
    private String m_Email;
    private String m_Password;


    public Admin(){}

    public long getM_Id() {return m_Id;}
    public void setM_Id(long m_Id) { this.m_Id = m_Id; }

    public String getM_Email () {return  m_Email;}
    public void setM_Email(String m_Email) {
        this.m_Email = m_Email;
    }

    public String getM_Password() {
        return m_Password;
    }
    public void setM_Password(String m_Password) {
        this.m_Password = m_Password;
    }
}
