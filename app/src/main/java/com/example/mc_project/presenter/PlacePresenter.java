package com.example.mc_project.presenter;

import android.widget.Toast;

import com.example.mc_project.MapsActivity;
import com.example.mc_project.api.ApiUrl;
import com.example.mc_project.api.Client;
import com.example.mc_project.api.FourSquareApiRequest;
import com.example.mc_project.model.response.SuggestionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacePresenter {

    private MapsActivity mapActivity;

    public PlacePresenter(MapsActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    public void getPlaceSuggestions(String near, String placeToQuery){

        FourSquareApiRequest apiRequest = Client.getFourSquareClient().getFourSquareApiRequest();
        Call<SuggestionResponse> call = apiRequest.getSuggestions(near, placeToQuery, ApiUrl.CLIENT_ID, ApiUrl.CLIENT_SECRET, ApiUrl.VERSION);

        call.enqueue(new Callback<SuggestionResponse>() {
            @Override
            public void onResponse(Call<SuggestionResponse> call, Response<SuggestionResponse> response) {
                if(response.code() == 200 && response.body().getBaseResponse().getResponseCode() == 200){
                    mapActivity.runOnUiThread(()-> mapActivity.onSuccessSuggestion(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SuggestionResponse> call, Throwable t) {
                mapActivity.runOnUiThread(()-> mapActivity.onErrorSuggestion());
            }
        });
    }

}
