package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import edu.uw.tcss450.shuynh08.tcss450clientside.AuthActivity;
import edu.uw.tcss450.shuynh08.tcss450clientside.MainActivity;
import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.PushyTokenViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.register.RegisterViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.signin.SignInFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.signin.SignInFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Represents the Account Details page with the following options:
 *      - Change Name
 *      - Change Password
 *      - Sign Out
 */
public class AccountFragment extends Fragment {

    /**
     * Binding object for the Account Details fragment.
     */
    private FragmentAccountBinding binding;

    /**
     * ViewModel object for the Account Details fragment.
     */
    private AccountViewModel mAccountModel;

    /**
     * A ViewModel representing and holding the state of the current user.
     */
    private UserInfoViewModel mUserInfoModel;

    /**
     * Required empty public constructor.
     */
    public AccountFragment() {
    }

    /**
     * Initializes the associated ViewModels (AccountViewModel and UserInfoViewModel)
     * for the Account Details fragment.
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountModel = new ViewModelProvider(getActivity())
                .get(AccountViewModel.class);
        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
    }

    /**
     * Generates and assigns a view binding for the Account Details fragment layout.
     * @param inflater The LayoutInflater
     * @param container The ViewGroup
     * @param savedInstanceState The data of the UI state
     * @return A ConstraintLayout based on the associated XML class for Account Details fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * Here, the Account Details page is listening for interactions from the user
     * to pick an option and acts upon the selection.
     * @param view The View
     * @param savedInstanceState The data of the UI state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonChangeName.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        AccountFragmentDirections.actionNavigationAccountToChangeNameFragment()
                ));

        binding.buttonChangePassword.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        AccountFragmentDirections.actionNavigationAccountToChangePasswordFragment()
                ));

        binding.buttonAccountSignout.setOnClickListener(button ->
                signOut());

        SharedPreferences prefs = getActivity().getSharedPreferences(
                getString(R.string.keys_shared_prefs),
                Context.MODE_PRIVATE);

        boolean tempUnit = prefs.getBoolean("unit",true);
        if (tempUnit) {
            binding.switchAccountTemperature.setChecked(true);
        } else {
            binding.switchAccountTemperature.setChecked(false);
        }

        binding.switchAccountTemperature.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                prefs.edit().putBoolean("unit",true).apply();
            } else {
                prefs.edit().putBoolean("unit",false).apply();
            }
        });

        binding.switchAccountTheme.setOnCheckedChangeListener(((compoundButton, checked) -> {
            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }));

        switch(getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                binding.switchAccountTheme.setChecked(true);
                break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding.switchAccountTheme.setChecked(false);
        }


    }

    /**
     * Sign out process deletes the JWT.
     */
    private void signOut() {
        SharedPreferences prefs =
                this.getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();


        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);

        model.addResponseObserver(this, result -> getActivity().finishAndRemoveTask());

        model.deleteTokenFromWebservice(mUserInfoModel.getmJwt());
        //End the app completely
        //this.getActivity().finish();
    }


}