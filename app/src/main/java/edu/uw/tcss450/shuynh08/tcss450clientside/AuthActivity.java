package edu.uw.tcss450.shuynh08.tcss450clientside;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import edu.uw.tcss450.shuynh08.tcss450clientside.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * The activity that facilitates user authentication before gaining access
 * to the app.
 *      - Sign-in
 *      - Register
 *      - Forgot password
 */
public class AuthActivity extends AppCompatActivity {

    /**
     * Initialize the ContentView and requests a PushyToken.
     * @param savedInstanceState The data of the UI state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);

        initiatePushyTokenRequest();
    }

    /**
     * Requests the PushyToken.
     */
    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}