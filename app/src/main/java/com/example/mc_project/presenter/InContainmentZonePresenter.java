package com.example.mc_project.presenter;

import android.widget.Toast;

import com.example.mc_project.api.covidApiRequest;
import com.example.mc_project.api.Client;
import com.example.mc_project.model.request.InContainmentZoneRequest;
import com.example.mc_project.model.response.InContainmentZoneResponse;
import com.example.mc_project.view.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InContainmentZonePresenter {

    private MainActivity mainActivity;

    public InContainmentZonePresenter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void getInContainmentZoneStatus(InContainmentZoneRequest inContainmentZoneRequest){

        covidApiRequest apiRequest = Client.getCovidClient().getCovidApiRequest();
        Call<InContainmentZoneResponse> call = apiRequest.getInContainmentZoneStatus(inContainmentZoneRequest);

        call.enqueue(new Callback<InContainmentZoneResponse>() {
            @Override
            public void onResponse(Call<InContainmentZoneResponse> call, Response<InContainmentZoneResponse> response) {
                if(response.code() == 200 && response.body().getStatus() == 200){
                    mainActivity.runOnUiThread(()-> mainActivity.onSuccessZoneStatus(response.body()));
                }
            }

            @Override
            public void onFailure(Call<InContainmentZoneResponse> call, Throwable t) {
                Toast.makeText(mainActivity,"Request couldn't be processed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
