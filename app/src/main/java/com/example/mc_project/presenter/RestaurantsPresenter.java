package com.example.mc_project.presenter;

import com.example.mc_project.api.ApiUrl;
import com.example.mc_project.api.Client;
import com.example.mc_project.api.FourSquareApiRequest;
import com.example.mc_project.model.response.RestaurantsNearByResponse;
import com.example.mc_project.view.RestaurantsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsPresenter {

    private RestaurantsFragment restaurantsFragment;

    public RestaurantsPresenter(RestaurantsFragment restaurantsFragment) {
        this.restaurantsFragment = restaurantsFragment;
    }

    public void getRestaurants(String categoryId, String near, int limit) {

        FourSquareApiRequest apiRequest = Client.getFourSquareClient().getFourSquareApiRequest();
        Call<RestaurantsNearByResponse> call = apiRequest.getRestaurants(categoryId, near, limit, ApiUrl.CLIENT_ID, ApiUrl.CLIENT_SECRET, ApiUrl.VERSION);

        call.enqueue(new Callback<RestaurantsNearByResponse>() {
            @Override
            public void onResponse(Call<RestaurantsNearByResponse> call, Response<RestaurantsNearByResponse> response) {
                if(response.code() == 200 && response.body().getBaseResponse().getResponseCode() == 200){
                    restaurantsFragment.getActivity().runOnUiThread(()-> restaurantsFragment.onSuccessRestaurants(response.body()));
                }
            }

            @Override
            public void onFailure(Call<RestaurantsNearByResponse> call, Throwable t) {
                restaurantsFragment.getActivity().runOnUiThread(()-> restaurantsFragment.onErrorSuggestion());
            }
        });
    }
}
