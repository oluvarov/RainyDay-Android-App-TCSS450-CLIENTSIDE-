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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentAddFriendBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list.IncomingRequestListViewAdapter;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list.IncomingRequestViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.utils.PasswordValidator;

/**
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private FragmentAddFriendBinding binding;
    private AddFriendViewModel mAddFriendModel;
    private UserInfoViewModel mUserInfoModel;
    private boolean isButtonPressed = false;
    private IncomingRequestViewModel mIncomingRequestModel;
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Required empty public constructor.
     */
    public AddFriendFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddFriendModel = new ViewModelProvider(getActivity())
                .get(AddFriendViewModel.class);
        mIncomingRequestModel = new ViewModelProvider(getActivity())
                .get(IncomingRequestViewModel.class);
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

    /**
     *
     * @param response JSONObject the API response
     */
    private void observeAddFriends(JSONObject response){
        System.out.println(isButtonPressed + " Observe before");
        if(isButtonPressed == true){
            if (response.length() > 0) {
                if (response.has("code")) {
                    try {
                        String code = response.getString("code");
                        System.out.println("Error Code " + code);
                        if(code.equals("409")){
                            errorFriendExist();
                        }else if(code.equals("404")){
                            errorNotFound();
                        }else if(code.equals("408")){
                            errorCannotAddYourself();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Parse Error", e.getMessage());
                    }
                }else{
                    success();
                }
            } else {
                Log.d("JSON Response", "No Response");
            }
        }
        isButtonPressed = false;
        System.out.println(isButtonPressed + " Observe after");

    }

    /**
     * Error to handle user's attempt at adding themselves.
     */
    private void errorCannotAddYourself() {
        binding.editEmail.setError("Cannot add yourself.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Handles error of user not being found.
     */
    private void errorNotFound(){
        binding.editEmail.setError("User not found.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Handles error of user trying to make a friend request again.
     */
    private void errorFriendExist(){
        binding.editEmail.setError("Friend request already exist.");
        binding.layoutWait.setVisibility(View.INVISIBLE);
    }

    /**
     * Upon user tapping the button, attempt to send the friend request.
     * @param button the Request button
     */
    private void sendFriendRequest(final View button) {
        isButtonPressed = true;
        System.out.println(isButtonPressed + " button top");
        String email = binding.editEmail.getText().toString().trim();
        mAddFriendModel.connectAddFriends(email,mUserInfoModel.getmJwt());
    }

    /**
     * Handles success of sending a friend request.
     */
    private void success(){
        Toast toast = Toast.makeText(getContext(),"Friend Request Sent to "
                + binding.editEmail.getText().toString().trim(),Toast.LENGTH_SHORT);
        toast.show();
//        Snackbar snackbar = Snackbar.make(binding.buttonRequest,"Friend Request Sent to "
//                + binding.editEmail.getText().toString().trim(),Snackbar.LENGTH_LONG);
//        snackbar.show();
    };

    /**
     * The back button to navigate back to the Contacts screen.
     * @param button Back
     */
    private void navigateToContacts(final View button){
        Navigation.findNavController(getView()).navigate(
                AddFriendFragmentDirections.actionAddFriendFragmentToNavigationContacts());
    }


}