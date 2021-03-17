package com.example.mc_project.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InContainmentZoneRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("latlngs")
    private ArrayList<ArrayList<Double>> latLngs;

    public void setKey(String key) {
        this.key = key;
    }

    public void setLatLngs(ArrayList<ArrayList<Double>> latLngs) {
        this.latLngs = latLngs;
    }
}
