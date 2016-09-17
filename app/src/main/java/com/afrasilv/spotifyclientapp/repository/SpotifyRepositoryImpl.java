package com.afrasilv.spotifyclientapp.repository;

import com.afrasilv.spotifyclientapp.libs.EventBus;

/**
 * Created by Alejandro Franco on 13/09/16.
 */
public class SpotifyRepositoryImpl implements SpotifyRepository {
    private final EventBus eventBus;

    public SpotifyRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getNewReleases() {

    }

    @Override
    public void getListCategories() {

    }

    @Override
    public void getTopPlaylist() {

    }
}
