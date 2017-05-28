package com.afrasilv.spotifyclientapp.api

import android.support.v7.app.AppCompatActivity
import com.google.gson.annotations.SerializedName


/**
 * Created by Alejandro Franco on 27/05/17.
 */

class CustomSpotifyResponse {
    private var count: Int = 0

    private var albums: List<String>? = null

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getAlbum(): List<String>? {
        return albums
    }

    fun setAlbum(recipes: List<String>) {
        this.albums = recipes
    }
}

class Album {

}