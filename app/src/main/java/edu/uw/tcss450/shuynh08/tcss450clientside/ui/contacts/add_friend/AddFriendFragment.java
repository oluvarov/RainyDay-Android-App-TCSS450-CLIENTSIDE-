package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.add_friend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddFriendBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;

/**
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private FragmentAddFriendBinding binding;
    private AddFriendViewModel mAddFriendViewModel;
    private UserInfoViewModel mUserInfoViewModel;

    public AddFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddFriendViewModel = new ViewModelProvider(getActivity())
                .get(AddFriendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddFriendBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoViewModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        binding.buttonRequest.setOnClickListener(button->sendFriendRequest());
    }

    private void sendFriendRequest() {
        String email = binding.editEmail.getText().toString().trim();
        mAddFriendViewModel.connectAddFriends(email,mUserInfoViewModel.getmJwt());
    }


}