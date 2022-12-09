package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.add_friend;

import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator.checkPwdSpecialChar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddFriendBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

/**
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private FragmentAddFriendBinding binding;
    private AddFriendViewModel mAddFriendModel;
    private UserInfoViewModel mUserInfoModel;
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    public AddFriendFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddFriendModel = new ViewModelProvider(getActivity())
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

        mUserInfoModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mAddFriendModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeAddFriends);
        binding.buttonRequest.setOnClickListener(this::sendFriendRequest);
        binding.buttonBack.setOnClickListener(this::navigateToContacts);


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
        binding.editEmail.setError("User not found.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void errorFriendExist(){
        binding.editEmail.setError("Friend request already exist");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    private void sendFriendRequest(final View button) {
        String email = binding.editEmail.getText().toString().trim();
        mAddFriendModel.connectAddFriends(email,mUserInfoModel.getmJwt());
    }

    private void success(){
        Snackbar snackbar = Snackbar.make(binding.buttonRequest,"Friend Request Sent to "
                + binding.editEmail.getText().toString().trim(),Snackbar.LENGTH_LONG);
        snackbar.show();
    };

    private void navigateToContacts(final View button){
        Navigation.findNavController(getView()).navigate(
                AddFriendFragmentDirections.actionAddFriendFragmentToNavigationContacts());
    }


}