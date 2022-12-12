package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import java.io.Serializable;

public class Contacts implements Serializable {

    private final String mEmail;
    private final String mName;
    private final int mImage;
    private final int mMemberID;


    public Contacts(String mEmail, String mName, int mImage, int memberID) {
        this.mEmail = mEmail;
        this.mName = mName;
        this.mImage = mImage;
        this.mMemberID = memberID;
    }

    public String getName() {
        return mName;
    }

    public int getImage() {
        return mImage;
    }

    public String getEmail() {
        return mEmail;
    }

    public int getMemberID() {
        return mMemberID;
    }
}
