package com.afrasilv.spotifyclientapp.repository;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public interface SpotifyRepository {
    void getNewReleases(String country);

    void getListCategories();

    void getTopPlaylist();
}
