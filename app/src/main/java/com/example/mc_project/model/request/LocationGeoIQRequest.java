package com.example.mc_project.model.request;

import com.google.gson.annotations.SerializedName;

public class LocationGeoIQRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("lng")
    private String longitude;

    @SerializedName("radius")
    private String radius;

    public void setKey(String key) {
        this.key = key;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
