package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.outgoing_request_list;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.shuynh08.tcss450clientside.io.RequestQueueSingleton;

public class OutgoingRequestViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mContacts;

    public OutgoingRequestViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>();
        mContacts.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mContacts.observe(owner, observer);
    }

    public void connectContacts(final int memberID, final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/contact/list";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mContacts::setValue,
                this::handleError) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("memberid_a", Integer.toString(memberID));
                headers.put("Authorization", "Bearer "  + jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mContacts.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mContacts.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}