package com.example.mc_project.model.bean;

import com.google.gson.annotations.SerializedName;

public class RestaurantBE {

    private String id;

    @SerializedName("name")
    private String restaurantName;

    private LocationBE location;

    public String getId() {
        return id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public LocationBE getLocation() {
        return location;
    }
}
