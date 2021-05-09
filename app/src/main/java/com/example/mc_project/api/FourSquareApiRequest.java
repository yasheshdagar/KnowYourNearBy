package com.example.mc_project.api;

import com.example.mc_project.model.response.RestaurantsNearByResponse;
import com.example.mc_project.model.response.SuggestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FourSquareApiRequest {

    @GET(ApiUrl.SUGGESTIONS)
    Call<SuggestionResponse> getSuggestions(@Query("near") String near, @Query("query") String placeToQuery,
                                            @Query("client_id") String clientId, @Query("client_secret") String secret,
                                            @Query("v") String currentDate);

    @GET(ApiUrl.RESTAURANTS)
    Call<RestaurantsNearByResponse> getRestaurants(@Query("categoryId") String categoryId, @Query("near") String near, @Query("limit") int limit,
                                                   @Query("client_id") String clientId, @Query("client_secret") String secret,
                                                   @Query("v") String currentDate);
}
