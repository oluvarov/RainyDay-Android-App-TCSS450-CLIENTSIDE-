
package edu.uw.tcss450.shuynh08.tcss450clientside.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.MainActivity;
import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChangeNameBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentHomeBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changename.ChangeNameViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.Weather;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.Weather24HourViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.Weather5DayViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.WeatherCurrentViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.WeatherRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private WeatherCurrentViewModel mWeatherCurrentModel;
    private RecyclerView recyclerView;
    private LocationViewModel mLocationModel;
    private UserInfoViewModel mUserInfoModel;
    private FragmentAccountBinding mAccountBinding;

    public HomeFragment() {
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
        binding = FragmentHomeBinding.inflate(inflater);
        mAccountBinding = FragmentAccountBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);


        recyclerView = binding.listHomeweather;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeatherCurrentModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherCurrent);

        mLocationModel.addLocationObserver(
                getViewLifecycleOwner(),
                this::observeGetLocation);

        FragmentHomeBinding.bind(getView()).textHello.setText("Hello " + model.getEmail());

        Log.e("JWT", model.getmJwt());

    }
    private void observeWeatherCurrent(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.textHome,"Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),Snackbar.LENGTH_SHORT);
                    snackbar.show();
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

    private void observeGetLocation(final Location location) {
        mWeatherCurrentModel.connectCurrentLatLng(location.getLatitude(),location.getLongitude(), mUserInfoModel.getmJwt());
    }

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



            JSONObject tempObject = response.getJSONObject("main");
            double temp = Math.floor(tempObject.getDouble("temp"));;
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



}