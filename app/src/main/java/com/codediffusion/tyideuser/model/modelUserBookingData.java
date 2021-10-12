package com.codediffusion.tyideuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modelUserBookingData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("trip_type")
        @Expose
        private String tripType;
        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("car_name")
        @Expose
        private String carName;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("total_booking_amount")
        @Expose
        private String totalBookingAmount;
        @SerializedName("advance_payment")
        @Expose
        private String advancePayment;
        @SerializedName("departure_date")
        @Expose
        private String departureDate;
        @SerializedName("departure_time")
        @Expose
        private String departureTime;
        @SerializedName("return_date")
        @Expose
        private String returnDate;
        @SerializedName("from_place")
        @Expose
        private String fromPlace;
        @SerializedName("to_place")
        @Expose
        private String toPlace;
        @SerializedName("running_distance")
        @Expose
        private String runningDistance;
        @SerializedName("extra_charge_kilo")
        @Expose
        private String extraChargeKilo;
        @SerializedName("extra_hour_rate")
        @Expose
        private String extraHourRate;
        @SerializedName("number_of_days")
        @Expose
        private String numberOfDays;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;

        public String getTripType() {
            return tripType;
        }

        public void setTripType(String tripType) {
            this.tripType = tripType;
        }

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTotalBookingAmount() {
            return totalBookingAmount;
        }

        public void setTotalBookingAmount(String totalBookingAmount) {
            this.totalBookingAmount = totalBookingAmount;
        }

        public String getAdvancePayment() {
            return advancePayment;
        }

        public void setAdvancePayment(String advancePayment) {
            this.advancePayment = advancePayment;
        }

        public String getDepartureDate() {
            return departureDate;
        }

        public void setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(String returnDate) {
            this.returnDate = returnDate;
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

        public String getRunningDistance() {
            return runningDistance;
        }

        public void setRunningDistance(String runningDistance) {
            this.runningDistance = runningDistance;
        }

        public String getExtraChargeKilo() {
            return extraChargeKilo;
        }

        public void setExtraChargeKilo(String extraChargeKilo) {
            this.extraChargeKilo = extraChargeKilo;
        }

        public String getExtraHourRate() {
            return extraHourRate;
        }

        public void setExtraHourRate(String extraHourRate) {
            this.extraHourRate = extraHourRate;
        }

        public String getNumberOfDays() {
            return numberOfDays;
        }

        public void setNumberOfDays(String numberOfDays) {
            this.numberOfDays = numberOfDays;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

    }
}
