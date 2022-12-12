package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.delete_chatroom;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChatMembersBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentDeleteChatRoomBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.chat_members.ChatMembersFragmentArgs;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.chat_members.ChatMembersViewModel;

public class DeleteChatRoomFragment extends Fragment {

    private DeleteChatRoomViewModel mDeleteChatRoomModel;
    private FragmentDeleteChatRoomBinding mBinding;
    private UserInfoViewModel mUserInfoModel;
    private static int HARD_CODED_CHAT_ID;

    public DeleteChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeleteChatRoomModel = new ViewModelProvider(getActivity()).get((DeleteChatRoomViewModel.class));
        ChatMembersFragmentArgs args = ChatMembersFragmentArgs.fromBundle(getArguments());
        HARD_CODED_CHAT_ID = args.getChatID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentDeleteChatRoomBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mDeleteChatRoomModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeDeleteRoom);

        mBinding.buttonRemoveChatroom.setOnClickListener(button ->
                attemptRemoveChatroom());

    }

    private void observeDeleteRoom(JSONObject response){
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mBinding.textRemoveChatroomError.setText(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                success();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }


    private void attemptRemoveChatroom() {
        mDeleteChatRoomModel.connectDeleteRoom(Integer.toString(HARD_CODED_CHAT_ID), mUserInfoModel.getmJwt());
    }


    private void success(){
        Toast toast = Toast.makeText(getContext(),"Chatroom deleted.",Toast.LENGTH_SHORT);
        toast.show();
    }




}