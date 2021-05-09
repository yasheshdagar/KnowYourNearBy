package com.example.mc_project.model.response;

import com.example.mc_project.model.bean.PlaceBE;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceResponse {

    @SerializedName("minivenues")
    private List<PlaceBE> placeList;

    public List<PlaceBE> getPlaceList() {
        return placeList;
    }
}
