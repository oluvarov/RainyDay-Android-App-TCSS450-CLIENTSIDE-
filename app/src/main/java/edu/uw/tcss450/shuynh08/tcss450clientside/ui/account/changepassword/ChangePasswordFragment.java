package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changepassword;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChangePasswordBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountFragmentDirections;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    private ChangePasswordViewModel mChangePasswordModel;

    private UserInfoViewModel model;

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangePasswordModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        binding.buttonUpdatePasswordAttempt.setOnClickListener(this::attemptChangePassword);

        mChangePasswordModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

    }

    /**
     * When the button is pressed, start the process of changing the user's password.
     * @param button
     */
    private void attemptChangePassword(final View button) {
        validateOldPassword();
    }

    /**
     * Checks whether or not the old password input is a valid password.
     */
    private void validateOldPassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editOldPassword.getText().toString().trim()),
                this::validateNewPassword,
                result -> binding.editOldPassword.setError("Please enter your old password."));
    }

    /**
     * Checks whether or not the new password input is a valid password.
     */
    private void validateNewPassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editNewPassword.getText().toString().trim()),
                this::validateNewPasswordAgain,
                result -> binding.editNewPassword.setError("New password must be length > 6, have a number,"
                        + " have an uppercase and lowercase letter,"
                        + " and one of these characters \"@#$%&*!?\"."));
    }

    /**
     * Checks whether or not the second new password input is a valid password.
     */
    private void validateNewPasswordAgain() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editNewPasswordAgain.getText().toString().trim()),
                this::validateBothNewPasswords,
                result -> binding.editNewPasswordAgain.setError("New password must be length > 6, have a number,"
                        + " have an uppercase and lowercase letter,"
                        + " and one of these characters \"@#$%&*!?\"."));
    }

    /**
     * Checks whether or not both new passwords are the same.
     */
    private void validateBothNewPasswords() {
        if (binding.editNewPassword.getText().toString().equals(binding.editNewPasswordAgain.getText().toString())) {
            validateNotSamePassword();
        } else {
            binding.editNewPassword.setError("Type in your new password correctly.");
        }
    }

    /**
     * Checks whether or not the old password and new password are the same.
     */
    private void validateNotSamePassword() {
        if (binding.editOldPassword.getText().toString().equals(binding.editNewPassword.getText().toString())) {
            binding.editNewPassword.setError("Your new password cannot be the same as your old password.");
        } else {
            verifyChangePasswordWithServer();
        }
    }

    /**
     * Sends a HTTP request to the server to change passwords.
     */
    private void verifyChangePasswordWithServer() {
        mChangePasswordModel.connect(
                binding.editOldPassword.getText().toString(),
                binding.editNewPassword.getText().toString(),
                model.getmJwt());
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
                    binding.editNewPassword.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Snackbar snackbar = Snackbar.make(binding.buttonUpdatePasswordAttempt,"Password successfully changed.",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }



}