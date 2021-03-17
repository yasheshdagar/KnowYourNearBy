package com.example.mc_project.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationGeoIQResponse {

    @SerializedName("containmentZoneNames")
    private List<String> containmentZones;

    @SerializedName("status")
    private int status;

    @SerializedName("numberOfNearbyZones")
    private String zonesCount;

    public List<String> getContainmentZones() {
        return containmentZones;
    }

    public int getStatus() {
        return status;
    }

    public String getZonesCount() {
        return zonesCount;
    }
}
