package com.example.mc_project.model.response;
import com.example.mc_project.model.bean.InContainmentBE;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InContainmentZoneResponse {

    @SerializedName("data")
    private List<InContainmentBE> inContainmentList;

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public List<InContainmentBE> getInContainmentList() {
        return inContainmentList;
    }
}
