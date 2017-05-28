package com.afrasilv.spotifyclientapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public class CustomSpotifyApiClient {
    private Retrofit retrofit;
    private final static String BASE_URL = "https://api.spotify.com/";

    public CustomSpotifyApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public CustomSpotifyApiService getSpotifyApiService() {
        return retrofit.create(CustomSpotifyApiService.class);
    }
}
