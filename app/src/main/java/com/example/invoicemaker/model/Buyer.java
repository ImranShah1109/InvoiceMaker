package com.example.invoicemaker.model;

public class Buyer {

    private int bid;
    private String name;
    private String address;
    private String mobile;

    public Buyer(int bid, String name, String address, String mobile)
    {
        this.bid = bid;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
    }

    public Buyer(String name, String address, String mobile)
    {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
    }

    public Buyer()
    {
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
