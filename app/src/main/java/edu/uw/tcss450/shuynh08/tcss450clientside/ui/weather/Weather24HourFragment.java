package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeather24hourBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;

/**
 * Fragment that's used to get the weather of the next 24 hours in a given location.
 */
public class Weather24HourFragment extends Fragment {

    private UserInfoViewModel mUserInfoModel;
    private LocationViewModel mLocationModel;
    private FragmentWeather24hourBinding binding;
    private Weather24HourViewModel mWeather24HourModel;
    private RecyclerView recyclerView;
    private String ip;

    public Weather24HourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather24HourModel = new ViewModelProvider(getActivity())
                .get(Weather24HourViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeather24hourBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = binding.listWeather24hour;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeather24HourModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeather24Hour);

        mLocationModel.addLatLngObserver(
                getViewLifecycleOwner(),
                this::observeGetLatLng);

        mLocationModel.addZipcodeObserver(
                getViewLifecycleOwner(),
                this::observeGetZipcode);

        mLocationModel.addLocationObserver(
                getViewLifecycleOwner(),
                this::observeGetLocation);

    }
    /**
     * Used to parse through JSONObject and display 24 hour weather.
     * @param response The JSONObject given by the API for 24 hour weather.
     */
    private void setUp24Hour(JSONObject response) {
        System.out.println(response);
        try {
            List<Weather> weatherList = new ArrayList<>();

            JSONObject cityObj = response.getJSONObject("city");
            String city = cityObj.getString("name");

            JSONArray arrayOfWeather = response.getJSONArray("list");

            for (int i = 0; i < arrayOfWeather.length(); i++) {
                JSONObject listObj = arrayOfWeather.getJSONObject(i);
                JSONObject mainObj = listObj.getJSONObject("main");
                Double temp = Math.floor(mainObj.getDouble("temp"));;
                String time = listObj.getString("dt_txt");
                int indexofSpace = time.indexOf(' ');
                String word = time.substring(indexofSpace);
                Log.e("WORD", word);


                JSONArray weatherArray = listObj.getJSONArray("weather");
                JSONObject weatherObj = weatherArray.getJSONObject(0);
                String weatherType = weatherObj.getString("main");

                String weatherDescription = weatherObj.getString("description");

                String icon = weatherObj.getString("icon");
                String url = "http://openweathermap.org/img/wn/"+ icon + "@2x.png";

                weatherList.add(new Weather(weatherType, weatherDescription, temp, city, word, url));
            }
            recyclerView.setAdapter(new WeatherRecyclerViewAdapter(weatherList));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * Used to observe for a response from the API for a JSONObject of 24 hour weather
     * @param response The JSONObject given by the API for 24 hour weather.
     */
    private void observeWeather24Hour(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.frameLayoutWeather24hour,"Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    /*binding.editLocation24hour.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));*/
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUp24Hour(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    /**
     * Used to observe a response from LocationViewModel on a change of latitude and longitude.
     * @param latLng The latlng from LocationViewModel.
     */
    private void observeGetLatLng(final LatLng latLng) {
        mWeather24HourModel.connect24HourLatLng(latLng.latitude, latLng.longitude, mUserInfoModel.getmJwt());
    }
    /**
     * Used to observe a response from LocationViewModel on a change of zipcode.
     * @param zipcode The zipcode from LocationViewModel.
     */
    private void observeGetZipcode(final String zipcode) {
        mWeather24HourModel.connect24HourZipcode(zipcode, mUserInfoModel.getmJwt());
    }

    private void observeGetLocation(final Location location) {
        mWeather24HourModel.connect24HourLatLng(location.getLatitude(),location.getLongitude(), mUserInfoModel.getmJwt());
    }

}