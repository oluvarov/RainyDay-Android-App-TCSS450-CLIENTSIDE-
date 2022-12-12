package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import java.io.Serializable;

/**
 * Encapsulate chat room details.
 */
public class ChatRoom implements Serializable {

    private final String mName;
    private final int mChatID;
    private final int mIcon;

    /**
     * Constructor to initialize the fields for a ChatRoom object.
     * @param mName String of the chatroom's name
     * @param mChatID int of the chatroom's ID
     * @param mIcon int for the chatroom's icon
     */
    public ChatRoom(String mName, int mChatID, int mIcon) {
        this.mName = mName;
        this.mChatID = mChatID;
        this.mIcon = mIcon;
    }

    /**
     * @return the chatroom's name as a String.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the chatroom's icon as its representative integer.
     */
    public int getIcon() {
        return mIcon;
    }

    /**
     * @return the chatroom's ID as an integer.
     */
    public int getChatID() {
        return mChatID;
    }
}
