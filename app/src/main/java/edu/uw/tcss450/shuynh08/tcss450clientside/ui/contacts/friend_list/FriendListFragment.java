package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.friend_list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentFriendListBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.Contacts;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.ContactsGetInfoViewModel;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerView;
    private @NonNull FragmentFriendListBinding binding;
    private UserInfoViewModel mUserInfoModel;
    private FriendListViewModel mContactsModel;
    private ContactsGetInfoViewModel mContactsGetInfoModel;
    private int mMemberID;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsModel = new ViewModelProvider(getActivity()).get(FriendListViewModel.class);
        mContactsGetInfoModel = new ViewModelProvider(getActivity()).get(ContactsGetInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.listFriend;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mContactsGetInfoModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeMemberInfo);
        mContactsModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeContacts);
        mContactsGetInfoModel.connectMemberInfo(mUserInfoModel.getmJwt());
    }

    private void observeContacts(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textContactFriendList.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpContacts(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void observeMemberInfo(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textContactFriendList.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpInfo(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void setUpInfo(JSONObject response) {
        try {
            mMemberID = response.getInt("memberid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mContactsModel.connectContacts(mMemberID, mUserInfoModel.getmJwt());
    }

    private void setUpContacts(JSONObject response) {

        try {
            List<Contacts> contactsList = new ArrayList<>();
            JSONArray keys = response.names();
            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                JSONObject obj = response.getJSONObject(key);
                String email = obj.getString("username");
                String name = obj.getString("firstname") + " " + obj.getString("lastname");
                contactsList.add(new Contacts(email, name, R.drawable.ic_rainychat_launcher_foreground));
            }
            recyclerView.setAdapter(new FriendListRecyclerViewAdapter(contactsList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}