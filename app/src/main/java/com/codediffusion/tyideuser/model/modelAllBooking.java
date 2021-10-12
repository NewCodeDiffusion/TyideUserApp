package com.codediffusion.tyideuser.model;


public class modelAllBooking {
   private String CustomerCode,Date,CustomerNAme,customerMobile,Address,description,statusType,paymentType,totalAmount,pickUpTime,returenDate,carName;

    public modelAllBooking(String customerCode, String date, String customerNAme, String customerMobile, String address, String description, String statusType, String paymentType, String totalAmount, String pickUpTime, String returenDate, String carName) {
        CustomerCode = customerCode;
        Date = date;
        CustomerNAme = customerNAme;
        this.customerMobile = customerMobile;
        Address = address;
        this.description = description;
        this.statusType = statusType;
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.pickUpTime = pickUpTime;
        this.returenDate = returenDate;
        this.carName = carName;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCustomerNAme() {
        return CustomerNAme;
    }

    public void setCustomerNAme(String customerNAme) {
        CustomerNAme = customerNAme;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getReturenDate() {
        return returenDate;
    }

    public void setReturenDate(String returenDate) {
        this.returenDate = returenDate;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
