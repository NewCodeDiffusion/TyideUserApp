package com.codediffusion.tyideuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class modelCompleteOrder {

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

        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("payment_amount")
        @Expose
        private String paymentAmount;
        @SerializedName("from_place")
        @Expose
        private String fromPlace;
        @SerializedName("to_place")
        @Expose
        private String toPlace;
        @SerializedName("return_date")
        @Expose
        private String returnDate;
        @SerializedName("pick_up_date")
        @Expose
        private String pickUpDate;
        @SerializedName("pick_up_at_date")
        @Expose
        private String pickUpAtDate;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getFromPlace() {
            return fromPlace;
        }

        public void setFromPlace(String fromPlace) {
            this.fromPlace = fromPlace;
        }

        public String getToPlace() {
            return toPlace;
        }

        public void setToPlace(String toPlace) {
            this.toPlace = toPlace;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(String returnDate) {
            this.returnDate = returnDate;
        }

        public String getPickUpDate() {
            return pickUpDate;
        }

        public void setPickUpDate(String pickUpDate) {
            this.pickUpDate = pickUpDate;
        }

        public String getPickUpAtDate() {
            return pickUpAtDate;
        }

        public void setPickUpAtDate(String pickUpAtDate) {
            this.pickUpAtDate = pickUpAtDate;
        }

    }


}
