package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import android.content.Context;
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
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeather5dayBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;


public class Weather5DayFragment extends Fragment {

    private LocationViewModel mLocationModel;
    private FragmentWeather5dayBinding binding;
    private Weather5DayViewModel mWeather5DayModel;
    private RecyclerView recyclerView;
    private String ip;

    public Weather5DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather5DayModel = new ViewModelProvider(getActivity())
                .get(Weather5DayViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeather5dayBinding.inflate(inflater);
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



        binding.buttonWeather5day.setOnClickListener(this::attemptWeatherZipcode);

        recyclerView = binding.listWeather5day;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWeather5DayModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeather5Day);

        mLocationModel.addLatLngObserver(
                getViewLifecycleOwner(),
                this::observeGetLatLng);

        String ipAddress = getIPAddress(true);
        Log.e("IPADDRESS", ipAddress);
        mWeather5DayModel.connect5DaysIP(ip);


    }



    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }


    private void attempt5DayWeather(final View button) {
        mWeather5DayModel.connect5DaysIP(ip);
    }

    private void attemptWeatherZipcode(final View button) {
        String zipcode = binding.editLocation5day.getText().toString().trim();
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        if (zipcode.matches(regex)) {
            mWeather5DayModel.connect5DaysZipcode(zipcode);
        } else {
            binding.editLocation5day.setError("Zipcode must either have the format of *****"
                    + " or *****-**** and contain only digits.");
        }
    }

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
                Double temp = Math.floor(mainObj.getDouble("temp"));

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

                weatherList.add(new Weather(weatherType, weatherDescription, temp, city, str, url));
            }
            recyclerView.setAdapter(new WeatherRecyclerViewAdapter(weatherList));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void observeWeather5Day(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editLocation5day.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
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

    private void observeGetLatLng(final LatLng latLng) {
        mWeather5DayModel.connect5DaysLatLng(latLng.latitude, latLng.longitude);
    }


}