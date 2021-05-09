package com.example.mc_project.model.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("code")
    private int responseCode;

    @SerializedName("requestId")
    private String requestId;

    public int getResponseCode() {
        return responseCode;
    }

    public String getRequestId() {
        return requestId;
    }
}
