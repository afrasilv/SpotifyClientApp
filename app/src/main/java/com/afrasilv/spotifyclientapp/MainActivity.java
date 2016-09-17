package com.afrasilv.spotifyclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.afrasilv.spotifyclientapp.dbmanager.DBManager;
import com.afrasilv.spotifyclientapp.dbmanager.DBManagerImpl;
import com.afrasilv.spotifyclientapp.model.DBToken;
import com.afrasilv.spotifyclientapp.utils.SpotifyApiKey;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "afrasilv://callback";
    private DBManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = DBManagerImpl.getInstance(this);

        List<DBToken> dbtokens = databaseManager.listTokens();

        if(dbtokens.isEmpty())
            initSpotifySession();
        else{
            Snackbar.make(findViewById(R.id.main_container), "Tenemos tu token guardado... Será el que usemos ;)", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void initSpotifySession(){
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(SpotifyApiKey.CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    DBToken token = new DBToken();
                    boolean todoOk = false;
                    if(response.getAccessToken() != null && !response.getAccessToken().isEmpty()){
                        databaseManager.deleteToken();
                        token.setTokenvalue(response.getAccessToken());
                        todoOk = databaseManager.insertToken(token);
                    }

                    String snackBarText;
                    if(todoOk)
                        snackBarText = "¡Sesión iniciada con éxito y guardado en DB encriptada!";
                    else
                        snackBarText = "Ha habido un error";

                    Snackbar.make(findViewById(R.id.main_container), snackBarText, Snackbar.LENGTH_LONG)
                            .show();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Snackbar.make(findViewById(R.id.main_container), "Ha habido un error: " + response.getError(), Snackbar.LENGTH_LONG)
                            .show();
                    System.out.println(response.toString());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    System.out.println(response.toString());
                    break;
            }

            List<DBToken> dbtokens = databaseManager.listTokens();

            Snackbar.make(findViewById(R.id.main_container), "En Db hay " + dbtokens.size() + " tokens guardados", Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
