package com.example.mc_project.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Client covidClient = null;
    private static Client fourSquareClient = null;

    private covidApiRequest covidApiRequest;
    private FourSquareApiRequest fourSquareApiRequest;

    private Client(int clientId) {

        Retrofit retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        if(clientId == 0){

            retrofit = new Retrofit.Builder().baseUrl(ApiUrl.COVID_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

            covidApiRequest = retrofit.create(covidApiRequest.class);

        } else {

            retrofit = new Retrofit.Builder().baseUrl(ApiUrl.FOUR_SQUARE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

            fourSquareApiRequest = retrofit.create(FourSquareApiRequest.class);
        }
    }

    public static Client getCovidClient() {
        if (covidClient == null) covidClient = new Client(0);
        return covidClient;
    }

    public static Client getFourSquareClient() {
        if (fourSquareClient == null) fourSquareClient = new Client(1);
        return fourSquareClient;
    }

    public covidApiRequest getCovidApiRequest() {
        return covidApiRequest;
    }

    public FourSquareApiRequest getFourSquareApiRequest() {
        return fourSquareApiRequest;
    }
}
