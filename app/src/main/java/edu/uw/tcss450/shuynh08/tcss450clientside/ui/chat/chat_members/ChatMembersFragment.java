package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.chat_members;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkExcludeWhiteSpace;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddToChatBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChatMembersBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.add_to_chat.AddToChatFragmentArgs;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat.add_to_chat.AddToChatViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

public class ChatMembersFragment extends Fragment {

    private FragmentChatMembersBinding mBinding;
    private ChatMembersViewModel mChatMembersViewModel;
    private UserInfoViewModel mUserInfoModel;
    private static int HARD_CODED_CHAT_ID;

    public ChatMembersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatMembersViewModel = new ViewModelProvider(getActivity()).get((ChatMembersViewModel.class));
        ChatMembersFragmentArgs args = ChatMembersFragmentArgs.fromBundle(getArguments());
        HARD_CODED_CHAT_ID = args.getChatID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatMembersBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mChatMembersViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeAddFriends);
        mBinding.buttonAddToChat.setOnClickListener(button ->
                removeFromChatRoom());

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
        mBinding.editChatName.setError("Person is not in chat");
        mBinding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void success(){
        Toast toast = Toast.makeText(getContext(),"User removed from chat.",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void removeFromChatRoom() {
        String email = mBinding.editChatName.getText().toString().trim();
        int chatID = HARD_CODED_CHAT_ID;
        mChatMembersViewModel.connectRemoveMember(email, String.valueOf(chatID), mUserInfoModel.getmJwt());
    }

}