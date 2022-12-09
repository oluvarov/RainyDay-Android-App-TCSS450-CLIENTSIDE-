package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * A FragmentStateAdapter used to create tabs for the Weather fragment and displays them.
 */
public class MyViewPagerAdapter extends FragmentStateAdapter {

    /**
     * Constructor for our FragmentStateAdapter, using super class's defaults.
     * @param fragmentActivity The current fragment
     */
    public MyViewPagerAdapter(@NonNull WeatherFragment fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Constructs the various associated Weather-related fragments based on the specified input.
     *      0 → WeatherCurrentFragment (Default)
     *      1 → Weather24HourFragment
     *      2 → Weather5DayFragment
     *      3 → LocationFragment
     * @param position An integer for one of the options
     * @return The selected fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
            default:
                return new WeatherCurrentFragment();
            case 1:
                return new Weather24HourFragment();
            case 2:
                return new Weather5DayFragment();
            case 3:
                return new LocationFragment();
        }
    }

    /**
     * @return total number of Weather-related tabs.
     */
    @Override
    public int getItemCount() {
        return 4;
    }


}
