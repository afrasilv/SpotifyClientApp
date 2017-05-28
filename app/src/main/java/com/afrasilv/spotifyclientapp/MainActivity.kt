package com.afrasilv.spotifyclientapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.afrasilv.spotifyclientapp.api.CustomSpotifyApiClient
import com.afrasilv.spotifyclientapp.api.CustomSpotifyApiService

import com.afrasilv.spotifyclientapp.dbmanager.DBManager
import com.afrasilv.spotifyclientapp.dbmanager.DBManagerImpl
import com.afrasilv.spotifyclientapp.libs.EventBus
import com.afrasilv.spotifyclientapp.libs.EventBusImpl
import com.afrasilv.spotifyclientapp.model.DBToken
import com.afrasilv.spotifyclientapp.repository.SpotifyRepository
import com.afrasilv.spotifyclientapp.repository.SpotifyRepositoryImpl
import com.afrasilv.spotifyclientapp.utils.SpotifyApiKey
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MainActivity : AppCompatActivity() {
    private var databaseManager: DBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseManager = DBManagerImpl.getInstance(this)

        val dbtokens = databaseManager!!.listTokens()

        if (dbtokens.isEmpty())
            initSpotifySession()
        else {
            Snackbar.make(findViewById(R.id.main_container), "Tenemos tu token guardado... Será el que usemos ;)", Snackbar.LENGTH_LONG)
                    .show()

            val service = CustomSpotifyApiClient().getSpotifyApiService()
            val repository = SpotifyRepositoryImpl(EventBusImpl.getInstance(), service, "Bearer " + dbtokens.get(0).tokenvalue)
            repository.getNewReleases("ES")
        }
    }

    private fun initSpotifySession() {
        val builder = AuthenticationRequest.Builder(SpotifyApiKey.CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
            // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    // Handle successful response
                    val token = DBToken()
                    var todoOk = false
                    if (response.accessToken != null && !response.accessToken.isEmpty()) {
                        databaseManager!!.deleteToken()
                        token.tokenvalue = response.accessToken
                        todoOk = databaseManager!!.insertToken(token)
                    }

                    val snackBarText: String
                    if (todoOk) {
                        snackBarText = "¡Sesión iniciada con éxito y guardado en DB encriptada!"

                        val service = CustomSpotifyApiClient().getSpotifyApiService()
                        val repository = SpotifyRepositoryImpl(EventBusImpl.getInstance(), service, "Bearer " + token.tokenvalue)
                        repository.getNewReleases("ES")
                    } else {
                        snackBarText = "Ha habido un error"
                    }

                    Snackbar.make(findViewById(R.id.main_container), snackBarText, Snackbar.LENGTH_LONG)
                            .show()
                }

            // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    // Handle error response
                    Snackbar.make(findViewById(R.id.main_container), "Ha habido un error: " + response.error, Snackbar.LENGTH_LONG)
                            .show()
                    println(response.toString())
                }

            // Most likely auth flow was cancelled
                else ->
                    // Handle other cases
                    println(response.toString())
            }

            val dbtokens = databaseManager!!.listTokens()

            Snackbar.make(findViewById(R.id.main_container), "En Db hay " + dbtokens.size + " tokens guardados", Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    companion object {
        private val REQUEST_CODE = 1337
        private val REDIRECT_URI = "afrasilv://callback"
    }
}
