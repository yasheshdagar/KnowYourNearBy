package com.example.mc_project.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Client instance = null;
    private ApiRequest apiRequest;

    private Client() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        apiRequest = retrofit.create(ApiRequest.class);
    }

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }
}
