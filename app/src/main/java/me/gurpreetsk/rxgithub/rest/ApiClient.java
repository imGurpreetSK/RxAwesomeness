package me.gurpreetsk.rxgithub.rest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class ApiClient {

    private static Retrofit retrofitInstance = null;
    private static final String BASE_URL = "https://api.github.com/";

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .build();

        return restAdapter.create(clazz);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

}
