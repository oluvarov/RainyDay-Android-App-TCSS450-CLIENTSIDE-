package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.new_chat;

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

public class AddChatroomViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mFriends;

    public AddChatroomViewModel(@NonNull Application application) {
        super(application);
        mFriends = new MutableLiveData<>();
        mFriends.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer){
        mFriends.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mFriends.setValue(new JSONObject("{" +
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
                mFriends.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectNewChatroom(String name, final String jwt) {

        String url = "https://tcss450-weather-chat.herokuapp.com/chats";
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("info",jwt);
        Log.i("info",name);

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mFriends::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
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


}
