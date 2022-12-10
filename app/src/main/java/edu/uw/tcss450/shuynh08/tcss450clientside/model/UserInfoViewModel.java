package edu.uw.tcss450.shuynh08.tcss450clientside.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * A ViewModel representing and holding the state of the current user.
 */
public class UserInfoViewModel extends ViewModel {

    /**
     * Field representing user's email address.
     */
    private final String mEmail;

    /**
     * Field representing user's JSON web token (JWT)
     */
    private final String mJwt;

    /**
     * Field representing user's member id
     */
    private final int mMemberID;

    /**
     * Constructor for the ViewModel. Initializes our fields based on email address
     * and JWT as specified in parameters.
     * @param email String email of the user
     * @param jwt the user's signed JWT
     */
    private UserInfoViewModel(String email, String jwt, int memberid) {
        mEmail = email;
        mJwt = jwt;
        mMemberID = memberid;
    }

    /**
     * @return the user's email address as String
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return the user's JWT as String
     */
    public String getmJwt() {
        return mJwt;
    }

    /**
     * @return the user's member id as int
     */
    public int getMemberID() {
        return mMemberID;
    }

    /**
     * Factory method to instantiate UserInfoViewModels.
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        /**
         * Field representing user's email address.
         */
        private final String email;

        /**
         * Field representing user's JWT.
         */
        private final String jwt;

        /**
         * Field representing user's member id.
         */
        private final int memberID;

        /**
         * Constructor for the ViewModel factory.
         * @param email String email of the user
         * @param jwt the user's signed JWT
         */
        public UserInfoViewModelFactory(String email, String jwt, int memberID) {
            this.email = email;
            this.jwt = jwt;
            this.memberID = memberID;
        }

        /**
         * Method to create an instance based on a given Class.
         * @param modelClass Class whose instance is requested
         * @param <T> Type
         * @return a UserInfoViewModel of type T
         */
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, memberID);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}

