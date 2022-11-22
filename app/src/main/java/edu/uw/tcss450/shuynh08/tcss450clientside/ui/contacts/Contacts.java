package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import java.io.Serializable;

public class Contacts implements Serializable {

    private final String mEmail;
    private final String mName;
    private final int mImage;


    public Contacts(String mEmail, String mName, int mImage) {
        this.mEmail = mEmail;
        this.mName = mName;
        this.mImage = mImage;
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
}
