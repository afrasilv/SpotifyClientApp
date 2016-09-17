package com.afrasilv.spotifyclientapp.api;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public interface tokenApi {

    @GET
    void requestAuth(@Query("client_id") String client_id, @Query("response_type") String response_type, @Query("redirect_uri") String redirect_uri, Callback<String> cb);
}
