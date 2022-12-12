package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

/**
 * A ViewModel managing the data for the Account fragment.
 */
public class AccountViewModel extends AndroidViewModel {

    /**
     * Publishes responses from our API web calls.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for the Register ViewModel. Initializes our mResponse with a blank JSONObject.
     * @param application for maintaining global Application state
     */
    public AccountViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }



}
