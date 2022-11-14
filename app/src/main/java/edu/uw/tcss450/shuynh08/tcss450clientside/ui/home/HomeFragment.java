package edu.uw.tcss450.shuynh08.tcss450clientside.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.MainActivity;
import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChangeNameBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentHomeBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changename.ChangeNameViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String mName;

    private FragmentHomeBinding binding;

    private HomeViewModel mHomeModel;

    private UserInfoViewModel model;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeModel = new ViewModelProvider(getActivity())
                .get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mName = model.getEmail();

        mHomeModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        attemptGetInfo();

        binding.textHello.setText("Hello " + mName);
        //FragmentHomeBinding.bind(getView()).textHello.setText("Hello " + mName);
        System.out.println(model.getmJwt());
    }

    private void attemptGetInfo() {
        mHomeModel.connect(model.getmJwt());
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
                    System.out.println("UNEXPECTED");
                    Snackbar snackbar = Snackbar.make(binding.textHello,
                            "Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mName = response.getString("firstname") +
                            " " + response.getString("lastname");
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }

            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}