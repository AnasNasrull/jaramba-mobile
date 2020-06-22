package com.example.jarambamobile.models;

public class PointAddressModel {
    private Double Latitude;
    private Double Longitude;

    public PointAddressModel(){}

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
