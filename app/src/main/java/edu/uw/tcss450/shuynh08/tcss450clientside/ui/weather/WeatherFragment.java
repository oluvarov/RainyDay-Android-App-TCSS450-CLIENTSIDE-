package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    private WeatherViewModel mWeatherModel;

    String ip;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);
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

        binding.textIP.setText("Your Fake IP Address: " + ip);

        binding.buttonCurrent.setOnClickListener(this::attemptCurrentWeather);

        binding.button24hour.setOnClickListener(this::attempt24HourWeather);

        binding.button5day.setOnClickListener(this::attempt5DayWeather);


        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptCurrentWeather(final View button) {
        mWeatherModel.connectCurrent(ip);
    }

    private void attempt24HourWeather(final View button) {

    }

    private void attempt5DayWeather(final View button) {

    }

    private void setUpWeather(JSONObject response) {
        System.out.println(response);
        try {
            JSONArray weather = response.getJSONArray("weather");
            System.out.println("weather :" + weather);
            JSONObject weatherObject = weather.getJSONObject(0);

            //JSONObject weatherTypeObject = weather.getJSONObject(1);
            String weatherType = weatherObject.getString("main");

            //JSONObject weatherDescriptionObject = weather.getJSONObject(2);
            String weatherDescription = weatherObject.getString("description");

            JSONObject tempObject = response.getJSONObject("main");
            double temp = tempObject.getDouble("temp");

            String city = response.getString("name");

            binding.textWeathertype.setText(weatherType);
            binding.textWeatherdescription.setText(weatherDescription);
            binding.textWeathertemp.setText(Double.toString(temp));
            binding.textCityname.setText(city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editLocation.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpWeather(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}