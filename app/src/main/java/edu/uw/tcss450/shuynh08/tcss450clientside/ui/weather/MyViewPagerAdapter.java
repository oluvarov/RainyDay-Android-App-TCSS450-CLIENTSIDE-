package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull WeatherFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new WeatherCurrentFragment();
            case 1:
                return new Weather24HourFragment();
            case 2:
                return new Weather5DayFragment();
            default:
                return new WeatherCurrentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
