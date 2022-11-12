package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changename;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChangeNameBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountFragmentDirections;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

public class ChangeNameFragment extends Fragment {

    private ChangeNameViewModel mChangeNameModel;

    private FragmentChangeNameBinding binding;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private UserInfoViewModel model;

    public ChangeNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeNameModel = new ViewModelProvider(getActivity())
                .get(ChangeNameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeNameBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        binding.buttonUpdatename.setOnClickListener(this::attemptChangeName);

        mChangeNameModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

    }

    private void attemptChangeName(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirstName.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLastName.getText().toString().trim()),
                this::verifyChangeNameWithServer,
                result -> binding.editLastName.setError("Please enter a last name."));
    }

    private void verifyChangeNameWithServer() {
        System.out.println(model.getmJwt());
        mChangeNameModel.connect(
                binding.editFirstName.getText().toString(),
                binding.editLastName.getText().toString(),
                model.getmJwt());

        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().

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
                    binding.editFirstName.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {

                Snackbar snackbar = Snackbar.make(binding.buttonUpdatename,"Name successfully changed.",Snackbar.LENGTH_SHORT);
                snackbar.show();

            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}