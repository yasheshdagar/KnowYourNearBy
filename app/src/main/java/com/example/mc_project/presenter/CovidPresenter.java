package com.example.mc_project.presenter;

import android.widget.Toast;

import com.example.mc_project.api.covidApiRequest;
import com.example.mc_project.api.Client;
import com.example.mc_project.model.request.LocationGeoIQRequest;
import com.example.mc_project.model.response.LocationGeoIQResponse;
import com.example.mc_project.view.CovidFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidPresenter {

    private CovidFragment covidFragment;

    public CovidPresenter(CovidFragment covidFragment){
        this.covidFragment = covidFragment;
    }

    public void getLocationBasedRegions(LocationGeoIQRequest locationGeoIQRequest){

        covidApiRequest apiRequest = Client.getCovidClient().getCovidApiRequest();
        Call<LocationGeoIQResponse> call = apiRequest.getLocationBasedRegions(locationGeoIQRequest);

        call.enqueue(new Callback<LocationGeoIQResponse>() {
            @Override
            public void onResponse(Call<LocationGeoIQResponse> call, Response<LocationGeoIQResponse> response) {
                if(response.code() == 200 && response.body().getStatus() == 200){
                    covidFragment.getActivity().runOnUiThread(()-> covidFragment.onSuccessLocationBased(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LocationGeoIQResponse> call, Throwable t) {
                Toast.makeText(covidFragment.getActivity(),"Request couldn't be processed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
