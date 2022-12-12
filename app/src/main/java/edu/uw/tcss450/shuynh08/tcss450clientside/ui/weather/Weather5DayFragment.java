package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeather5dayBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;

/**
 * Fragment that's used to get the weather of the next 5 days in a given location.
 */
public class Weather5DayFragment extends Fragment {

    /**
     * Binding object for the 5-Day Weather fragment.
     */
    private FragmentWeather5dayBinding binding;

    /**
     * ViewModel object for the 5-Day Weather fragment.
     */
    private Weather5DayViewModel mWeather5DayModel;

    /**
     * A ViewModel representing and holding the state of the current user.
     */
    private UserInfoViewModel mUserInfoModel;

    /**
     * ViewModel object of Location for the Weather fragment.
     */
    private LocationViewModel mLocationModel;

    /**
     * RecyclerView to hold 5 days' worth of weather forecast cards.
     */
    private RecyclerView recyclerView;

    private FragmentAccountBinding mAccountBinding;

    /**
     * Required empty public constructor.
     */
    public Weather5DayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather5DayModel = new ViewModelProvider(getActivity())
                .get(Weather5DayViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeather5dayBinding.inflate(inflater);
        mAccountBinding = FragmentAccountBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.listWeather5day;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeather5DayModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeather5Day);

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
     * Used to parse through JSONObject and display 5-day weather.
     * @param response The JSONObject given by the API for 5-day weather.
     */
    private void setUp5Day(JSONObject response) {
        System.out.println(response);
        Format f = new SimpleDateFormat("EEEE");
        try {

            List<Weather> weatherList = new ArrayList<>();
            JSONObject cityObj = response.getJSONObject("city");
            String city = cityObj.getString("name");

            JSONArray arrayOfWeather = response.getJSONArray("list");
            for (int i = 0; i < arrayOfWeather.length(); i = i + 8) {
                JSONObject listObj = arrayOfWeather.getJSONObject(i);
                JSONObject mainObj = listObj.getJSONObject("main");
                Double temp = Math.floor(mainObj.getDouble("temp"));;
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

                String time = listObj.getString("dt_txt");
                int indexOfSpace = time.indexOf(' ');
                String justDate = time.substring(0, indexOfSpace);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
                Date date = formatter.parse(justDate);

                JSONArray weatherArray = listObj.getJSONArray("weather");
                JSONObject weatherObj = weatherArray.getJSONObject(0);
                String weatherType = weatherObj.getString("main");
                String weatherDescription = weatherObj.getString("description");
                String icon = weatherObj.getString("icon");
                String url = "http://openweathermap.org/img/wn/"+ icon + "@2x.png";
                String str = f.format(date);

                weatherList.add(new Weather(weatherType, weatherDescription, tempString, city, str, url));
            }
            recyclerView.setAdapter(new WeatherRecyclerViewAdapter(weatherList));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Used to observe for a response from the API for a JSONObject of 5 day weather
     * @param response The JSONObject given by the API for 5 day weather.
     */
    private void observeWeather5Day(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.frameLayoutWeather5day,"Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    /*binding.editLocation5day.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));*/
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUp5Day(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Used to observe a response from LocationViewModel on a change of latitude and longitude.
     * @param latLng The latLng from LocationViewModel.
     */
    private void observeGetLatLng(final LatLng latLng) {
        mWeather5DayModel.connect5DaysLatLng(latLng.latitude, latLng.longitude, mUserInfoModel.getmJwt());
    }

    /**
     * Used to observe a response from LocationViewModel on a change of ZIP Code.
     * @param zipcode The ZIP Code from LocationViewModel.
     */
    private void observeGetZipcode(final String zipcode) {
        mWeather5DayModel.connect5DaysZipcode(zipcode, mUserInfoModel.getmJwt());
    }

    /**
     * Used to observe a response from LocationViewModel on a change of location.
     * @param location The location from LocationViewModel.
     */
    private void observeGetLocation(final Location location) {
        System.out.println("observeGetLocation");
        mWeather5DayModel.connect5DaysLatLng(location.getLatitude(),location.getLongitude(), mUserInfoModel.getmJwt());
    }


}