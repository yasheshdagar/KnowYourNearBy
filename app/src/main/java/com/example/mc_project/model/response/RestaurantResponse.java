package com.example.mc_project.model.response;

import com.example.mc_project.model.bean.RestaurantBE;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantResponse {

    @SerializedName("venues")
    private List<RestaurantBE> restaurantList;

    public List<RestaurantBE> getRestaurantList() {
        return restaurantList;
    }
}
