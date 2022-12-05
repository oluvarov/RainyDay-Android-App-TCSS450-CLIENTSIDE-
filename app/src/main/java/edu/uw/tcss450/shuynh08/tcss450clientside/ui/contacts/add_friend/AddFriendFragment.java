package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.add_friend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddFriendBinding;

/**
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private FragmentAddFriendBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddFriendBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }


}