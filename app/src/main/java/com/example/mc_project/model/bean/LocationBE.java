package com.example.mc_project.model.bean;

import com.google.gson.annotations.SerializedName;

public class LocationBE {

    @SerializedName("lat")
    private String latitude;

    @SerializedName("lng")
    private String longitude;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
