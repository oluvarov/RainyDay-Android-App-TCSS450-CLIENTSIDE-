package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

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



public class Weather24HourFragment extends Fragment {

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
        /*Context context = requireContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/

        ip = "2601:603:1a7f:84d0:60bf:26b3:c5ba:4de";

        binding.buttonWeather24hour.setOnClickListener(this::attemptWeatherZipcode);

        recyclerView = binding.listWeather24hour;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeather24HourModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeather24Hour);

        mWeather24HourModel.connect24HourIP(ip);
    }


    private void attempt24HourWeather(final View button) {
        mWeather24HourModel.connect24HourIP(ip);
    }

    private void attemptWeatherZipcode(final View button) {
        String zipcode = binding.editLocation24hour.getText().toString().trim();
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        if (zipcode.matches(regex)) {
            mWeather24HourModel.connect24HourZipcode(zipcode);
        } else {
            binding.editLocation24hour.setError("Zipcode must either have the format of *****"
                    + " or *****-**** and contain only digits.");
        }
    }


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
                Double temp = mainObj.getDouble("temp");
                String time = listObj.getString("dt_txt");
                int indexofSpace = time.indexOf(' ');
                String word = time.substring(indexofSpace);


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

    private void observeWeather24Hour(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editLocation24hour.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
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

}