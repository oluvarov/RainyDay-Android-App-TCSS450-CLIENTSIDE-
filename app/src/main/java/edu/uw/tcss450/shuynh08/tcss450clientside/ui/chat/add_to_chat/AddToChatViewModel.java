package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.add_to_chat;

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

/**
 * A ViewModel managing the data for the Add-To-Chat fragment.
 */
public class AddToChatViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mChatRecipient;

    /**
     * Constructor for the Add-To-Chat ViewModel. Initializes our mChatRecepient object with a blank
     * JSONObject.
     * @param application for maintaining global Application state
     */
    public AddToChatViewModel(@NonNull Application application) {
        super(application);
        mChatRecipient = new MutableLiveData<>();
        mChatRecipient.setValue(new JSONObject());
    }

    /**
     * Used to observe our API responses.
     * @param owner The fragment's lifecycle owner
     * @param observer The observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer){
        mChatRecipient.observe(owner, observer);
    }

    /**
     * Used to handle errors with API calls.
     * @param error VolleyError
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mChatRecipient.setValue(new JSONObject("{" +
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
                mChatRecipient.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Calls the API to add a user to a chatroom.
     * @param email the String of the email address.
     * @param chatID the String of the chatID
     * @param jwt the String of the signed JWT of the user
     */
    public void connectAddToChatroom(String email, String chatID, final String jwt) {

        String url = "https://tcss450-weather-chat.herokuapp.com/chats/" + chatID + "/" + email;
        Log.i("info",jwt);

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                mChatRecipient::setValue,
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
