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
import android.widget.Toast;

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

/**
 * A fragment used to display a change-name screen for the user.
 */
public class ChangeNameFragment extends Fragment {

    /**
     * Binding object for the Register fragment.
     */
    private FragmentChangeNameBinding binding;

    /**
     * ViewModel object for the Change Name fragment.
     */
    private ChangeNameViewModel mChangeNameModel;

    /**
     * Serves as a validator for the user's new name.
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * A ViewModel representing and holding the state of the current user.
     */
    private UserInfoViewModel model;

    /**
     * Required empty public constructor.
     */
    public ChangeNameFragment() {
    }

    /**
     * Initializes the associated ViewModel for Change Name.
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeNameModel = new ViewModelProvider(this)
                .get(ChangeNameViewModel.class);
    }

    /**
     * Generates and assigns a view binding for the Change Name layout.
     * @param inflater The LayoutInflater
     * @param container The ViewGroup
     * @param savedInstanceState The data of the UI state
     * @return a ConstraintLayout based on the associated XML class for the Change Name fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeNameBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * Here, the Change Name fragment is listening for input from the user.
     * @param view The View
     * @param savedInstanceState The data of the UI state
     */
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

    /**
     * Upon user clicking the Update Name button, kicks off the sign-in process into a sequence
     * of helper methods for both fields (first name and last name).
     *
     * If any measures fail, user will need to correct invalid input, hit the button again, and
     * the validation process repeats.
     * @param button The Update Name button
     */
    private void attemptChangeName(final View button) {
        validateFirst();
    }

    /**
     * Processes new user's first name with mNameValidator, then proceed with validating last name.
     *
     * Shows an error to the user if the first name is excluded.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirstName.setError("Please enter a first name."));
    }

    /**
     * Processes new user's last name with mNameValidator, then proceed with verifying
     * with the server.
     *
     * Shows an error to the user if the last name is excluded.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLastName.getText().toString().trim()),
                this::verifyChangeNameWithServer,
                result -> binding.editLastName.setError("Please enter a last name."));
    }

    /**
     * Calls the Change Name ViewModel's connect() method to send the new first/last name info to
     * the server, updating the record with the new name.
     */
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
     * An observer on the HTTP Response from the web server.
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

                Toast toast = Toast.makeText(getContext(),"Name successfully changed.",Toast.LENGTH_SHORT);
                toast.show();
                /*Snackbar snackbar = Snackbar.make(binding.buttonUpdatename,"Name successfully changed.",Snackbar.LENGTH_SHORT);
                snackbar.show();*/

            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}