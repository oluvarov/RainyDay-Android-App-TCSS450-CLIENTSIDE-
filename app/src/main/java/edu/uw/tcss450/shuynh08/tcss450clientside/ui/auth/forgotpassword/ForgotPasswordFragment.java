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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentForgotPasswordBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

public class ForgotPasswordFragment extends Fragment {

    private ForgotPasswordViewModel mForgotPasswordModel;

    private FragmentForgotPasswordBinding binding;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    public ForgotPasswordFragment() {

    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForgotPasswordModel = new ViewModelProvider(getActivity())
                .get(ForgotPasswordViewModel.class);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonForgotGetPassword.setOnClickListener(this::validateEmail);
        mForgotPasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void validateEmail(final View button) {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editForgetEmail.getText().toString().trim()),
                this::verifyEmailWithServer,
                result -> binding.editForgetEmail.setError("Please enter a valid Email address."));
    }

    private void verifyEmailWithServer() {
        mForgotPasswordModel.connect(binding.editForgetEmail.getText().toString());
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
                    binding.editForgetEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {

            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}