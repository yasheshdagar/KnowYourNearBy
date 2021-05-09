package com.example.mc_project.model.response;

import com.google.gson.annotations.SerializedName;

public class RestaurantsNearByResponse {

    @SerializedName("meta")
    private BaseResponse baseResponse;

    @SerializedName("response")
    private RestaurantResponse restaurantResponse;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public RestaurantResponse getRestaurantResponse() {
        return restaurantResponse;
    }
}
