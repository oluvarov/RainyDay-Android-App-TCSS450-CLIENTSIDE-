package edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.changepassword;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAccountBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChangePasswordBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountFragmentDirections;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.account.AccountViewModel;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    private ChangePasswordViewModel mChangePasswordModel;

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



    }



}