package edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.forgotpassword;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdSpecialChar;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentForgotPasswordBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

/**
 * A fragment used to display the Forgot Password screen for returning users.
 */
public class ForgotPasswordFragment extends Fragment {

    /**
     * Binding object for the Forgot Password fragment
     */
    private FragmentForgotPasswordBinding binding;

    /**
     * ViewModel object for the Forgot Password fragment
     */
    private ForgotPasswordViewModel mForgotPasswordModel;

    /**
     * Required empty public constructor.
     */
    public ForgotPasswordFragment() {
    }

    /**
     * Serves as a validator for the user's email address.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Initializes the associated ViewModel for Forgot Password.
     * @param savedInstanceState The Data of the UI state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForgotPasswordModel = new ViewModelProvider(getActivity())
                .get(ForgotPasswordViewModel.class);
    }

    /**
     * Generates and assigns a view binding for the Forgot Password layout.
     * @param inflater The LayoutInflater
     * @param container The ViewGroup
     * @param savedInstanceState The data of the UI state
     * @return a ConstraintLayout based on the associated XML class for the Forgot Password fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * Here, the Forgot Password fragment is listening for input from the user.
     * @param view The View
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonForgotGetPassword.setOnClickListener(this::validateEmail);
        mForgotPasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Processes the returning user's email with mEmailValidator, then proceed with validating
     * password. Sends an error to the user for an incorrect email address.
     * @param button The Next button to process the entered email address.
     */
    private void validateEmail(final View button) {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editForgetEmail.getText().toString().trim()),
                this::verifyEmailWithServer,
                result -> binding.editForgetEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Calls the Forgot Password ViewModel to ask the server to accept the email address for
     * password recovery.
     */
    private void verifyEmailWithServer() {
        mForgotPasswordModel.connect(binding.editForgetEmail.getText().toString());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response The Response from the server
     */
    private void observeResponse(final JSONObject response) {
        Log.i("info", "connect: " + response);
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editForgetEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Snackbar snackbar = Snackbar.make(binding.buttonForgotGetPassword,"Successful! New password sent to your email.",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}