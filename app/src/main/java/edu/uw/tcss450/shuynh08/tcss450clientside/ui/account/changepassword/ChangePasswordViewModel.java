package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changepassword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class ChangePasswordViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }
}