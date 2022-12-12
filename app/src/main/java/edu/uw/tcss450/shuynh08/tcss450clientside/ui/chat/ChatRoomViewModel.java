package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.shuynh08.tcss450clientside.io.RequestQueueSingleton;

/**
 * A ViewModel managing the data for the ChatRoom fragment.
 */
public class ChatRoomViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mChatRoom;

    /**
     * Constructor for the Chat Room ViewModel. Initializes our mChatRoom with a blank
     * JSONObject.
     * @param application for maintaining global Application state
     */
    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mChatRoom = new MutableLiveData<>();
        mChatRoom.setValue(new JSONObject());
    }

    /**
     * Used to observe our API responses.
     * @param owner The fragment's lifecycle owner
     * @param observer The observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mChatRoom.observe(owner, observer);
    }

    /**
     * Used to handle errors with API calls.
     * @param error VolleyError
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mChatRoom.setValue(new JSONObject("{" +
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
                mChatRoom.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Asks the server for the member info.
     * [May be deleted if not used.]
     * @param jwt the user's signed JWT
     */
    public void connectMemberInfo(final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/user/";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mChatRoom::setValue,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer "  + jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Load chat member's data from the server.
     * @param memberID String of the member's ID
     * @param jwt the user's signed JWT
     */
    public void connectChatRoom(final int memberID, final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/user/list/chat";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mChatRoom::setValue,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("memberID", Integer.toString(memberID));
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
}