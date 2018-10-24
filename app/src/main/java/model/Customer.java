package model;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Customer {

    private String custName;
    private String phoneNumber;
    private String address;
    private String custImageUrl;
    private String sellerId;
    private @ServerTimestamp Date inputDatetime;

    public Customer() {
    }

    public Customer(String custName, String phoneNumber, String address, String custImageUrl, String sellerId) {
        this.custName = custName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.custImageUrl = custImageUrl;
        this.sellerId = sellerId;
    }


    public String getCustName() {
        return custName;
    }

    public Customer setCustName(String custName) {
        this.custName = custName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Customer setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getcustImageUrl() {
        return custImageUrl;
    }

    public Customer setcustImageUrl(String custImageUrl) {
        this.custImageUrl = custImageUrl;
        return this;
    }

    public Date getInputDatetime() {
        return inputDatetime;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
