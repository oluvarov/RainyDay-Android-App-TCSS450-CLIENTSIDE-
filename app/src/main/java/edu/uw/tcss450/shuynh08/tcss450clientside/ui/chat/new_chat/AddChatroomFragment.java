package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.new_chat;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdSpecialChar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddChatroomBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;

public class AddChatroomFragment extends Fragment {

    private FragmentAddChatroomBinding mBinding;
    private AddChatroomViewModel mAddChatroomModel;
    private UserInfoViewModel mUserInfoModel;

    public AddChatroomFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddChatroomModel = new ViewModelProvider(getActivity())
                .get(AddChatroomViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddChatroomBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mAddChatroomModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeNewChatroom);
        mBinding.buttonNewChatroom.setOnClickListener(button->createNewChatroom());
    }

    private void observeNewChatroom(JSONObject response){
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String code = response.getString("code");
                    if(code.equals("409")){
                        errorFriendExist();
                    }else{
                        errorNotFound();
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Snackbar snackbar = Snackbar.make(mBinding.editChatName,"New chat made",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Handles error of user not being found.
     */
    private void errorNotFound(){
        mBinding.editChatName.setError("Chat not found.");
        mBinding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Handles error of user trying to add friend to the same chat again.
     */
    private void errorFriendExist(){
        mBinding.editChatName.setError("Chatroom already exist");
        mBinding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void createNewChatroom() {
        String name = mBinding.editChatName.getText().toString().trim();
        Log.i("info", name);
        mAddChatroomModel.connectNewChatroom(name, mUserInfoModel.getmJwt());
    }

}