package edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.signin;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdSpecialChar;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentSignInBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.PushyTokenViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

/**
 * A fragment used to display the main sign-in screen.
 */
public class SignInFragment extends Fragment {

    /**
     * Binding object for the Sign-In fragment
     */
    private FragmentSignInBinding binding;

    /**
     * ViewModel object for the Sign-In fragment
     */
    private SignInViewModel mSignInModel;

    /**
     * PushyTokenViewModel object for the Sign-In fragment using Pushy for user authentication.
     */
    private PushyTokenViewModel mPushyTokenViewModel;

    /**
     * ViewModel object representing the user.
     */
    private UserInfoViewModel mUserViewModel;

    /**
     * Required empty public constructor.
     */
    public SignInFragment() {
    }

    /**
     * Serves as a validator for the returning user's email address.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Serves as a validator for the returning user's password.
     */
    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    /**
     * Initializes the associated ViewModels (PushyTokenViewModel and SignInModel)
     * for sign-in.
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

    }

    /**
     * Generates and assigns a view binding for the Sign-In fragment layout.
     * @param inflater The LayoutInflater
     * @param container The ViewGroup
     * @param savedInstanceState The data of the UI state
     * @return A ConstraintLayout based on the associated XML class for sign-in fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * Here, the sign-in page is ready to observe user activity. The user can:
     *      - Sign in
     *      - Recover password
     *      - Register for a new account
     * @param view The View
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        binding.buttonForgotGetPassword.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
                ));
        binding.buttonSignIn.setOnClickListener(this::attemptSignIn);

        /*binding.buttonSignIn.setOnClickListener(button -> {
            attemptSignIn(binding.buttonSignIn);
            binding.layoutWait.setVisibility(View.VISIBLE);
        });*/

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        /*mSignInModel.addResponseObserver(getViewLifecycleOwner(), result-> {
                observeResponse(result);
                binding.layoutWait.setVisibility(View.GONE);
                });*/

        // Don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.buttonSignIn.setEnabled(!token.isEmpty()));


        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Upon startup of the app, checks for JWT and if it is still valid before navigating
     * to the Activity past Authentication.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
                return;
            }
        }
    }

    /**
     * Upon user clicking the Sign In button, kicks off the sign-in process into a sequence
     * of helper methods for both fields (email and password).
     * @param button The Sign In button
     */
    private void attemptSignIn(final View button) {
        validateEmail();
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getmJwt());
    }

    /**
     * Processes the returning user's email with mEmailValidator, then proceed with validating
     * password. Calls helper method to handle incorrect email address.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> errorEmail());
    }

    /**
     * Handles incorrect email address.
     */
    private void errorEmail() {
        binding.editEmail.setError("Please enter a valid Email address.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Attempts to authenticate password. Calls helper method to handle incorrect password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> errorPassword());
    }

    /**
     * Handles incorrect password.
     */
    private void errorPassword() {
        binding.editPassword.setError("Please enter a valid Password.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Calls the Sign-In's ViewModel to ask the server if the user is authentic.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Calls the Sign-In's ViewModel to ask the server if the user is verified.
     */
    private void verifyVerificationWithServer() {
        mSignInModel.connectVerified(
                binding.editEmail.getText().toString()
        );
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        if (binding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionLoginFragmentToMainActivity(email, jwt));
        //Remove THIS activity from the Task list. Pops off the backstack
        getActivity().finish();
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
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.editEmail.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);

                    sendPushyToken();

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        Log.i("info", "connect: " + response);
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.editEmail.getText().toString(),
                        mUserViewModel.getmJwt()
                );
            }
        }
    }

}