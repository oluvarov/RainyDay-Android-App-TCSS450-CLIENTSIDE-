package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

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
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherCurrentBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;


public class WeatherCurrentFragment extends Fragment {

    private FragmentWeatherBinding mWeatherBinding;
    private UserInfoViewModel mUserInfoModel;
    private LocationViewModel mLocationModel;
    private FragmentWeatherCurrentBinding binding;
    private WeatherCurrentViewModel mWeatherCurrentModel;
    private RecyclerView recyclerView;
    private String ip;

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
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*Context context = requireContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/

        ip = "2601:603:1a7f:84d0:60bf:26b3:c5ba:4de";

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

        mWeatherCurrentModel.connectCurrentIP(ip, mUserInfoModel.getmJwt());
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

            System.out.println("ICON URL: " + url);


            JSONObject tempObject = response.getJSONObject("main");
            double temp = Math.floor(tempObject.getDouble("temp"));

            String city = response.getString("name");
            Weather weatherThing = new Weather(weatherType, weatherDescription, temp, city, "Today", url);
            List<Weather> weatherList = new ArrayList<>();
            weatherList.add(weatherThing);
            recyclerView.setAdapter(new WeatherRecyclerViewAdapter(weatherList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    private void observeGetLatLng(final LatLng latLng) {
        mWeatherCurrentModel.connectCurrentLatLng(latLng.latitude, latLng.longitude, mUserInfoModel.getmJwt());
    }

    private void observeGetZipcode(final String zipcode) {
        mWeatherCurrentModel.connectCurrentZipcode(zipcode, mUserInfoModel.getmJwt());
    }






}