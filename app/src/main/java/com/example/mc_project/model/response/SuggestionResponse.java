package com.example.mc_project.model.response;

import com.google.gson.annotations.SerializedName;

public class SuggestionResponse {

    @SerializedName("meta")
    private BaseResponse baseResponse;

    @SerializedName("response")
    private PlaceResponse placeResponse;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public PlaceResponse getPlaceResponse() {
        return placeResponse;
    }
}
