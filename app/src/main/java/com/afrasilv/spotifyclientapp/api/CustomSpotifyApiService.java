package com.afrasilv.spotifyclientapp.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public interface CustomSpotifyApiService {

    @GET
    void requestAuth(@Query("client_id") String client_id, @Query("response_type") String response_type, @Query("redirect_uri") String redirect_uri, Callback<String> cb);


    @GET("v1/browse/new-releases")
    Call<CustomSpotifyResponse> requestNewReleases(@Header("Authorization") String authorization, @Header("Accept") String dataFormat, @Query("country") String country);
}
