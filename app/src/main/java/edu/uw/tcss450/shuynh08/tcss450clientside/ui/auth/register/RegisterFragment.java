package edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.register;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.*;

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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

/**
 * A fragment used to display a registration screen for new users.
 */
public class RegisterFragment extends Fragment {

    /**
     * Binding object for the Register fragment.
     */
    private FragmentRegisterBinding binding;

    /**
     * ViewModel object for the Register fragment.
     */
    private RegisterViewModel mRegisterModel;

    /**
     * Serves as a validator for the new user's name.
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * Serves as a validator for the new user's email address.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Serves as a validator for the new user's password. This is to ensure they meet the following
     * requirements:
     *      - Length > 6
     *      - Includes a special char. '@' '#' '$' '%' '&' '*' '!' '?'
     *      - Excludes whitespace
     *      - Includes a digit, and
     *      - Includes an uppercase and a lowercase letter
     */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Required empty public constructor.
     */
    public RegisterFragment() {
    }

    /**
     * Initializes the associated ViewModel for Registration.
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    /**
     * Generates and assigns a view binding for the Registration layout.
     * @param inflater The LayoutInflater
     * @param container The ViewGroup
     * @param savedInstanceState The data of the UI state
     * @return a ConstraintLayout based on the associated XML class for the Register fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * Here, the Register Fragment is listening for input from the user.
     * @param view The View
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Upon user clicking the Register button, kicks off the registration validation process into a
     * sequence of helper methods for each of the fields (first name, last name, etc.).
     *
     * If any measures fail, user will need to correct invalid input, hit the button again, and
     * the validation process repeats.
     *
     * @param button The Register button
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Processes new user's first name with mNameValidator, then proceed with validating last name.
     *
     * Shows an error to the user if the first name is invalid.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * Processes new user's last name with mNameValidator, then proceed with validating email
     * address.
     *
     * Shows an error to the user if the last name is excluded.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * Processes new user's email address with mEmailValidator, then proceed with validating
     * passwords starting with seeing if they match.
     *
     * Shows an error to the user if the email address is excluded.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Another field instructs user to enter the same password, then
     * continue with validating the password based on the requirements.
     *
     * Shows an error to the user if the password is mismatched.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Rules password against requirements with mPassWordValidator.
     *
     * Shows an error to the user if the password is invalid.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Password must be length > 6, have a number,"
                        + " have an uppercase and lowercase letter,"
                        + " and one of these characters \"@#$%&*!?\"."));
    }

    /**
     * Calls the Register View Model's connect() method to send user's info to the server,
     * registering them as a new user.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        // This is an Asynchronous call. No statements after should rely on the
        // result of connect().

    }

    /**
     * Switches back to the sign-in screen upon user's successful registration. Email & password
     * are automatically entered in the sign-in fields, so user can simply hit "Sign In."
     */
    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

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
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

}