package edu.uw.tcss450.shuynh08.tcss450clientside.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;
    private MutableLiveData<LatLng> mLatLng;

    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
        mLatLng = new MediatorLiveData<>();
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    public void addLatLngObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super LatLng> observer) {
        mLatLng.observe(owner, observer);
    }

    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

    public void setLatLng(final LatLng latlng) {
        if (!latlng.equals(mLatLng.getValue())) {
            mLatLng.setValue(latlng);
        }
    }

    public LatLng getCurrentLatLng() {
        return new LatLng(mLatLng.getValue().latitude, mLatLng.getValue().longitude);
    }

}
