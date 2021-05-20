package com.finix.kisiiattend;

public class Location {
    String longitude,latitude;

    public Location(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
