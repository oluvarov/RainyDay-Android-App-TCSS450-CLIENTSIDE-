package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherCardBinding;

/**
 * A RecyclerViewAdapter used to help create weather cards and display them in a RecyclerView.
 */
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private final List<Weather> mWeather;

    public WeatherRecyclerViewAdapter(List<Weather> weather) {
        this.mWeather = weather;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeather.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentWeatherCardBinding binding;

        public WeatherViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherCardBinding.bind(view);
        }

        void setWeather(final Weather weather) {
            binding.textCity.setText(weather.getCity());
            binding.textType.setText(weather.getWeatherType());
            binding.textDesc.setText(weather.getWeatherDescription());

            binding.textTemp.setText(weather.getTemperature());
            binding.textTime.setText(weather.getTime());
            Log.e("ICON URL", weather.getIconUrl());
            Picasso.get()
                    .load(weather.getIconUrl())
                    .placeholder(R.drawable.ic_rainychat_launcher_foreground)
                    .error(R.drawable.ic_error_blue_24dp)
                    .fit()
                    .into(binding.imageWeathericon);
        }


    }
}
