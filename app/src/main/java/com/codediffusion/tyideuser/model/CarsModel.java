package com.codediffusion.tyideuser.model;

public class CarsModel {
    private int CarImage;
    private String CarType,CarName,TotalDistance,Price,driverCharges,nightCharges,timeduration;

    public CarsModel(int carImage, String carType, String carName, String totalDistance, String price, String driverCharges, String nightCharges, String timeduration) {
        CarImage = carImage;
        CarType = carType;
        CarName = carName;
        TotalDistance = totalDistance;
        Price = price;
        this.driverCharges = driverCharges;
        this.nightCharges = nightCharges;
        this.timeduration = timeduration;
    }

    public int getCarImage() {
        return CarImage;
    }

    public void setCarImage(int carImage) {
        CarImage = carImage;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getTotalDistance() {
        return TotalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        TotalDistance = totalDistance;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDriverCharges() {
        return driverCharges;
    }

    public void setDriverCharges(String driverCharges) {
        this.driverCharges = driverCharges;
    }

    public String getNightCharges() {
        return nightCharges;
    }

    public void setNightCharges(String nightCharges) {
        this.nightCharges = nightCharges;
    }

    public String getTimeduration() {
        return timeduration;
    }

    public void setTimeduration(String timeduration) {
        this.timeduration = timeduration;
    }
}
