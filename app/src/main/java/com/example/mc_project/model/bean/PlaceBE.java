package com.example.mc_project.model.bean;

import com.google.gson.annotations.SerializedName;

public class PlaceBE {

    private String id;

    @SerializedName("name")
    private String placeName;

    private LocationBE location;

    public String getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public LocationBE getLocation() {
        return location;
    }
}
