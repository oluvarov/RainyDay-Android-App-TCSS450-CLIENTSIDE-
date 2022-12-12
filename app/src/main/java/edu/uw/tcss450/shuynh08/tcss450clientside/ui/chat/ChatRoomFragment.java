package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChatroomBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather.WeatherRecyclerViewAdapter;

/**
 * A fragment used to display all chatrooms a user can choose from.
 */
public class ChatRoomFragment extends Fragment {

    private ChatRoomViewModel mChatRoomModel;
    private ChatRoomGetInfoViewModel mMemberInfoModel;
    private FragmentChatroomBinding binding;
    private RecyclerView recyclerView;
    private UserInfoViewModel mUserInfoModel;
    private int mMemberID;

    /**
     * Required empty public constructor.
     */
    public ChatRoomFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemberInfoModel = new ViewModelProvider(getActivity())
                .get(ChatRoomGetInfoViewModel.class);

        mChatRoomModel = new ViewModelProvider(getActivity())
                .get(ChatRoomViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatroomBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        recyclerView = binding.listChatroom;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mMemberInfoModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeMemberInfo);

        mChatRoomModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeChatRoom);

        mMemberInfoModel.connectMemberInfo(mUserInfoModel.getmJwt());

        binding.addChatroom.setOnClickListener(button->
                Navigation.findNavController(getView()).navigate(ChatRoomFragmentDirections
                        .actionNavigationChatroomToAddChatroomFragment()));

    }

    /**
     * Gets the member info from the server.
     * [May be deleted if not used.]
     * @param button The button
     */
    private void attemptGetMemberInfo(final View button) {
        mMemberInfoModel.connectMemberInfo(mUserInfoModel.getmJwt());
    }

    /**
     * Gets the chat rooms from the server.
     * [May be deleted if not used.]
     * @param button The button
     */
    private void attemptGetChatRooms(final View button) {
        mChatRoomModel.connectChatRoom(mMemberID, mUserInfoModel.getmJwt());
    }

    /**
     * Sets up the member's info by obtaining their ID, then connects their chatrooms.
     * @param response JSONObject the API response
     */
    private void setUpMemberInfo(JSONObject response) {
        System.out.println("setupmemberinfo");
        System.out.println(response + " setupmemberinfo");
        try {
            mMemberID = response.getInt("memberid");
            System.out.println("memberID: " + mMemberID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatRoomModel.connectChatRoom(mMemberID, mUserInfoModel.getmJwt());
    }

    /**
     * Upon receiving the list of chatrooms from the API response, it will set up
     * the chatrooms within the app.
     * @param response JSONObject the API response
     */
    private void setUpChatRoom(JSONObject response) {
        System.out.println("setupchatroom");
        System.out.println(response + " setupchatroom");
        try {
            List<ChatRoom> chatRoomList = new ArrayList<>();
            JSONArray keys = response.names();
            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                System.out.println("key " + key);
                JSONObject obj = response.getJSONObject(key);
                int chatID = obj.getInt("chatid");
                String roomName= obj.getString("name");
                System.out.println("chatid " + chatID);
                chatRoomList.add(new ChatRoom(roomName,chatID, R.drawable.ic_rainychat_launcher_foreground));
            }
            recyclerView.setAdapter(new ChatRoomRecyclerViewAdapter(chatRoomList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Upon receiving a successful response from an API call, set up the member's ID by calling
     * setUpMemberInfo().
     * @param response JSONObject the API response
     */
    private void observeMemberInfo(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.textChat,"Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpMemberInfo(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Upon receiving a successful response from an API call, set up the chatrooms by calling
     * setUpChatRoom().
     * @param response JSONObject the API response
     */
    private void observeChatRoom(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Snackbar snackbar = Snackbar.make(binding.textChat,"Error Authenticating: " +
                            response.getJSONObject("data").getString("message"),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                setUpChatRoom(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}