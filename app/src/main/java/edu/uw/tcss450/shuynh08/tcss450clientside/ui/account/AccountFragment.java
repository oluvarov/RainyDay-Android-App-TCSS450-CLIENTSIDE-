package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.register.RegisterViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.auth.signin.SignInFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private AccountViewModel mAccountModel;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountModel = new ViewModelProvider(getActivity())
                .get(AccountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater);
        return binding.getRoot();
    }

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

    }
}