package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.add_to_chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddFriendBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddToChatBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.add_friend.AddFriendViewModel;

public class AddToChatFragment extends Fragment {

    private FragmentAddToChatBinding mBinding;
    private AddToChatViewModel mAddToChatModel;
    private UserInfoViewModel mUserInfoModel;

    public AddToChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddToChatModel = new ViewModelProvider(getActivity()).get((AddToChatViewModel.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddToChatBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mAddToChatModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeAddFriends);
    }

    private void observeAddFriends(JSONObject response){
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String code = response.getString("code");
                    System.out.println("Error Code " + code);
                    if(code.equals("409")){
                        errorFriendExist();
                    }else if(code.equals("404")){
                        errorNotFound();
                    }else{

                    }
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

    private void errorNotFound(){
        mBinding.editChatName.setError("User not found.");
        mBinding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void errorFriendExist(){
        mBinding.editChatName.setError("Person is already in chat");
        mBinding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void success(){
        mBinding.textAddFriend.setText("Friend request successful");
    }

//    private void addToChatRoom() {
//        String email = mBinding.editChatName.getText().toString().trim();
//        //Get memberID and chatID and send to connectAddToChatroom
//        mAddToChatModel.connectAddToChatroom(,mUserInfoModel.getmJwt());
//    }

}