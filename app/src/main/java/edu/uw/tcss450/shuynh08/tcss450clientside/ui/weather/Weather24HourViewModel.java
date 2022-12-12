package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

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
 * A viewmodel for 24 hour weather that helps make API calls.
 */
public class Weather24HourViewModel extends AndroidViewModel {

    /**
     * Publishes responses from our API web calls.
     */
    private MutableLiveData<JSONObject> mWeather;

    /**
     * Constructor for the 24-Hour Weather ViewModel. Initializes our mWeather with a
     * blank JSONObject.
     * @param application for maintaining global Application state
     */
    public Weather24HourViewModel(@NonNull Application application) {
        super(application);
        mWeather = new MutableLiveData<>();
        mWeather.setValue(new JSONObject());
    }

    /**
     * Used to observe our API responses.
     * @param owner The fragment's lifecycle owner
     * @param observer The observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mWeather.observe(owner, observer);
    }

    /**
     * Used to handle errors with API calls.
     * @param error VolleyError
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mWeather.setValue(new JSONObject("{" +
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
                mWeather.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Method that creates a HTTP request to an API for 24 hour weather using an IP.
     * @param ip The IP of the device making the request.
     * @param jwt The JSON web token of the user.
     */
    public void connect24HourIP(final String ip, final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/weather/today";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mWeather::setValue,
                this::handleError) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer "  + jwt);
                headers.put("ip", ip);
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

    /**
     * Method that creates a HTTP request to an API for 24 hour weather using a zipcode.
     * @param zipcode The zipcode given by the user.
     * @param jwt The JSON web token of the user.
     */
    public void connect24HourZipcode(final String zipcode, final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/weather/today";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mWeather::setValue,
                this::handleError) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer "  + jwt);
                headers.put("zip", zipcode);
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

    /**
     * Method that creates a HTTP request to an API for 24 hour weather using latitude and longitude.
     * @param lat The latitude given by the user.
     * @param lon The longitude given by the user.
     * @param jwt The JSON web token of the user.
     */
    public void connect24HourLatLng(final double lat, final double lon, final String jwt) {
        String url = "https://tcss450-weather-chat.herokuapp.com/weather/today";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mWeather::setValue,
                this::handleError) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer "  + jwt);
                headers.put("lat", String.valueOf(lat));
                headers.put("lon", String.valueOf(lon));
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