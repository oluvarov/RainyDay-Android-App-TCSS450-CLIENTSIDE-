package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

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
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherCurrentBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;

/**
 * Fragment that's used to get the current weather of a given location.
 */
public class WeatherCurrentFragment extends Fragment {

    /**
     * Binding object for the Current Weather fragment.
     */
    private FragmentWeatherCurrentBinding binding;

    private FragmentWeatherBinding mWeatherBinding;
    private UserInfoViewModel mUserInfoModel;
    private LocationViewModel mLocationModel;
    private FragmentAccountBinding mAccountBinding;


    private WeatherCurrentViewModel mWeatherCurrentModel;
    private RecyclerView recyclerView;
    // private String ip;

    public WeatherCurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherCurrentModel = new ViewModelProvider(getActivity())
                .get(WeatherCurrentViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherCurrentBinding.inflate(inflater);
        mWeatherBinding = FragmentWeatherBinding.inflate(inflater);
        mAccountBinding = FragmentAccountBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.listWeatherCurrent;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeatherCurrentModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherCurrent);

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
     * Used to parse through JSONObject and display current weather.
     * @param response The JSONObject given by the API for the current weather.
     */
    private void setUpCurrent(JSONObject response) {
        System.out.println(response);
        try {
            JSONArray weather = response.getJSONArray("weather");
            System.out.println("weather :" + weather);
            JSONObject weatherObject = weather.getJSONObject(0);

            //JSONObject weatherTypeObject = weather.getJSONObject(1);
            String weatherType = weatherObject.getString("main");

            //JSONObject weatherDescriptionObject = weather.getJSONObject(2);
            String weatherDescription = weatherObject.getString("description");

            String icon = weatherObject.getString("icon");
            String url = "http://openweathermap.org/img/wn/"+ icon + "@2x.png";

            System.out.println("ICON URL: " + url);


            JSONObject tempObject = response.getJSONObject("main");
            double temp = Math.floor(tempObject.getDouble("temp"));
            String tempString;

            SharedPreferences prefs = getActivity().getSharedPreferences(
                    getString(R.string.keys_shared_prefs),
                    Context.MODE_PRIVATE);
            boolean tempUnit = prefs.getBoolean("unit",true);
            if (tempUnit) {
                temp = Math.floor(temp * 1.8) + 32;
                tempString = temp + "\u00B0" + "F";
            } else {
                tempString = temp + "\u00B0" + "C";
            }
            String city = response.getString("name");
            Weather weatherThing = new Weather(weatherType, weatherDescription, tempString, city, "Today", url);
            List<Weather> weatherList = new ArrayList<>();
            weatherList.add(weatherThing);
            recyclerView.setAdapter(new WeatherRecyclerViewAdapter(weatherList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to observe for a response from the API for a JSONObject of current weather
     * @param response The JSONObject given by the API for the current weather.
     */
    private void observeWeatherCurrent(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mWeatherBinding.editLocation.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpCurrent(response);
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
        mWeatherCurrentModel.connectCurrentLatLng(latLng.latitude, latLng.longitude, mUserInfoModel.getmJwt());
    }

    /**
     * Used to observe a response from LocationViewModel on a change of zipcode.
     * @param zipcode The zipcode from LocationViewModel.
     */
    private void observeGetZipcode(final String zipcode) {
        mWeatherCurrentModel.connectCurrentZipcode(zipcode, mUserInfoModel.getmJwt());
    }


    private void observeGetLocation(final Location location) {
        System.out.println("observeGetLocation");
        mWeatherCurrentModel.connectCurrentLatLng(location.getLatitude(),location.getLongitude(), mUserInfoModel.getmJwt());
    }






}