package com.example.mc_project.api;

import com.example.mc_project.model.request.InContainmentZoneRequest;
import com.example.mc_project.model.request.LocationGeoIQRequest;
import com.example.mc_project.model.response.InContainmentZoneResponse;
import com.example.mc_project.model.response.LocationGeoIQResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface covidApiRequest {

    @POST(ApiUrl.LOCATION_BASED_NEARBY_ZONES)
    Call<LocationGeoIQResponse> getLocationBasedRegions(@Body LocationGeoIQRequest locationGeoIQRequest);

    @POST(ApiUrl.IN_CONTAINMENT_ZONE)
    Call<InContainmentZoneResponse> getInContainmentZoneStatus(@Body InContainmentZoneRequest inContainmentZoneRequest);

}
