package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private final String mName;
    private final int mChatID;
    private final int mIcon;


    public ChatRoom(String mName, int mChatID, int mIcon) {
        this.mName = mName;
        this.mChatID = mChatID;
        this.mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getChatID() {
        return mChatID;
    }
}
