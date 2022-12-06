package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.LocationViewModel;


public class WeatherFragment extends Fragment {

    private LocationViewModel mLocationModel;
    private FragmentWeatherBinding binding;
    private String ip;
    MyViewPagerAdapter mViewPagerAdapter;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);

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

        mViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPagerWeather.setAdapter(mViewPagerAdapter);
        binding.buttonWeather.setOnClickListener(this::attemptGetZipcode);


        binding.tabWeatherLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPagerWeather.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPagerWeather.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabWeatherLayout.getTabAt(position).select();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == SCROLL_STATE_DRAGGING && binding.viewPagerWeather.getCurrentItem() == 3) {
                    binding.viewPagerWeather.setUserInputEnabled(false);
                } else {
                    binding.viewPagerWeather.setUserInputEnabled(true);
                }
            }
        });

        //binding.viewPagerWeather.setUserInputEnabled(false);

        /*Context context = requireContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/

        ip = "2601:603:1a7f:84d0:60bf:26b3:c5ba:4de";

    }

    private void attemptGetZipcode(final View button) {
        String zipcode = binding.editLocation.getText().toString().trim();
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        if (zipcode.matches(regex)) {
            mLocationModel.setZipcode(zipcode);
        } else {
            binding.editLocation.setError("Zipcode must either have the format of *****"
                    + " or *****-**** and contain only digits.");
        }
    }



}