package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import java.io.Serializable;

/**
 * Encapsulates a contact.
 */
public class Contacts implements Serializable {

    private final String mEmail;
    private final String mName;
    private final int mImage;
    private final int mMemberID;

    /**
     * Initializes the fields for a Contact.
     * @param mEmail of the contact's email address
     * @param mName String of the contact's name
     * @param mImage integer of the contact's profile image
     * @param memberID int of the contact's member ID
     */
    public Contacts(String mEmail, String mName, int mImage, int memberID) {
        this.mEmail = mEmail;
        this.mName = mName;
        this.mImage = mImage;
        this.mMemberID = memberID;
    }

    /**
     * @return the contact's email address as a String.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return the contact's name as a String.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the contact's image as its representative integer.
     */
    public int getImage() {
        return mImage;
    }

    /**
     * @return the contact's member ID as an integer.
     */
    public int getMemberID() {
        return mMemberID;
    }
}
