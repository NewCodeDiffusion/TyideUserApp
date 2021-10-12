package com.codediffusion.tyideuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modelCheckout {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("pick_address")
        @Expose
        private String pickAddress;
        @SerializedName("drop_address")
        @Expose
        private String dropAddress;
        @SerializedName("trip_start")
        @Expose
        private String tripStart;
        @SerializedName("car_type")
        @Expose
        private String carType;
        @SerializedName("car_model")
        @Expose
        private String carModel;
        @SerializedName("pick_date")
        @Expose
        private String pickDate;
        @SerializedName("return_date")
        @Expose
        private String returnDate;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("gst")
        @Expose
        private String gst;
        @SerializedName("extra_charge")
        @Expose
        private String extraCharge;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("package_name")
        @Expose
        private String packageName;
        @SerializedName("note")
        @Expose
        private String note;

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPickAddress() {
            return pickAddress;
        }

        public void setPickAddress(String pickAddress) {
            this.pickAddress = pickAddress;
        }

        public String getDropAddress() {
            return dropAddress;
        }

        public void setDropAddress(String dropAddress) {
            this.dropAddress = dropAddress;
        }

        public String getTripStart() {
            return tripStart;
        }

        public void setTripStart(String tripStart) {
            this.tripStart = tripStart;
        }

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getPickDate() {
            return pickDate;
        }

        public void setPickDate(String pickDate) {
            this.pickDate = pickDate;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(String returnDate) {
            this.returnDate = returnDate;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getExtraCharge() {
            return extraCharge;
        }

        public void setExtraCharge(String extraCharge) {
            this.extraCharge = extraCharge;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

    }

}
