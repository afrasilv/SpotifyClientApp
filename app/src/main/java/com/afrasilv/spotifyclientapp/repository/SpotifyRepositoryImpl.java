package com.afrasilv.spotifyclientapp.repository;

import com.afrasilv.spotifyclientapp.BuildConfig;
import com.afrasilv.spotifyclientapp.api.CustomSpotifyApiService;
import com.afrasilv.spotifyclientapp.api.CustomSpotifyResponse;
import com.afrasilv.spotifyclientapp.libs.EventBus;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public class SpotifyRepositoryImpl implements SpotifyRepository {
    private final EventBus eventBus;
    private CustomSpotifyApiService service;
    private String token;

    public SpotifyRepositoryImpl(EventBus eventBus, CustomSpotifyApiService service, String token) {
        this.eventBus = eventBus;
        this.service = service;
        this.token = token;
    }

    public SpotifyRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getNewReleases(String country) {
        Call<CustomSpotifyResponse> call = service.requestNewReleases(this.token, "application/json", country);
        call.enqueue(new Callback<CustomSpotifyResponse>() {
            @Override
            public void onResponse(Call<CustomSpotifyResponse> call, Response<CustomSpotifyResponse> response) {
                System.out.println();
            }

            @Override
            public void onFailure(Call<CustomSpotifyResponse> call, Throwable t) {
                System.out.println();
            }
        });
    }

    @Override
    public void getListCategories() {

    }

    @Override
    public void getTopPlaylist() {

    }
}
