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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentContactsBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentFriendListBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.Contacts;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.ContactsGetInfoViewModel;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentFriendListBinding binding;
    private UserInfoViewModel mUserInfoModel;
    private FriendListViewModel mContactsModel;
    private ContactsGetInfoViewModel mContactsGetInfoModel;
    private FragmentContactsBinding mContactsBinding;
    private FriendListDeleteViewModel mFriendListDeleteModel;
    private int mMemberID;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsModel = new ViewModelProvider(getActivity()).get(FriendListViewModel.class);
        mContactsGetInfoModel = new ViewModelProvider(getActivity()).get(ContactsGetInfoViewModel.class);
        mFriendListDeleteModel= new ViewModelProvider(getActivity()).get(FriendListDeleteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendListBinding.inflate(inflater);
        mContactsBinding = FragmentContactsBinding.inflate(inflater);
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
        mFriendListDeleteModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeDeleteContact);

        mContactsGetInfoModel.connectMemberInfo(mUserInfoModel.getmJwt());

        binding.listFriend.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0) {
                    mContactsBinding.addFriendFab.hide();
                } else if (dy < 0) {
                    mContactsBinding.addFriendFab.show();
                }
            }
        });
    }

    private void observeDeleteContact(JSONObject response) {

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
                approveDelete();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

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
            JSONObject friends = response.getJSONObject("friends");
            JSONArray friendsKeys = friends.names();
            if (friendsKeys == null) {
                return;
            }
            for (int i = 0; i < friendsKeys.length(); i++) {
                String key = friendsKeys.getString(i);
                JSONObject obj = friends.getJSONObject(key);
                System.out.println(obj);
                String verified = obj.getString("verified");
                String email = obj.getString("username");
                String name = obj.getString("firstname") + " " + obj.getString("lastname");
                int memberID = obj.getInt("memberid");
                contactsList.add(new Contacts(email, name, R.drawable.ic_rainychat_launcher_foreground,memberID));
            }
            recyclerView.setAdapter(new FriendListRecyclerViewAdapter(getContext(),contactsList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void approveDelete() {
        Toast toast = Toast.makeText(getContext(),"Successful Deletion Of Contact",Toast.LENGTH_SHORT);
        toast.show();
    }


}