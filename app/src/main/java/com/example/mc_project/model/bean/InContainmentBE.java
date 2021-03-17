package com.example.mc_project.model.bean;

import com.google.gson.annotations.SerializedName;

public class InContainmentBE {

    @SerializedName("inContainmentZone")
    private boolean zoneStatus;

    @SerializedName("district")
    private String district;

    @SerializedName("districtTotalConfirmed")
    private int totalCases;

    @SerializedName("districtCurrentActive")
    private int activeCases;

    public boolean isZoneStatus() {
        return zoneStatus;
    }

    public String getDistrict() {
        return district;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getActiveCases() {
        return activeCases;
    }
}
