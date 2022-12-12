package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Encapsulate chat message details.
 */
public final class ChatMessage implements Serializable {

    private final int mMessageId;
    private final String mMessage;
    private final String mSender;
    private final String mTimeStamp;
    private final String mFirstName;
    private final String mLastName;

    /**
     * Initializes the fields related to an individual chat message.
     * @param messageId Integer of the message ID
     * @param message String of the message
     * @param sender String of the message sender
     * @param timeStamp String of the message timestamp
     * @param firstName String of the sender's first name
     * @param lastName String of the sender's last name
     */
    public ChatMessage(int messageId, String message, String sender, String timeStamp, String firstName, String lastName) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
        mFirstName = firstName;
        mLastName = lastName;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        System.out.println(msg.getInt("messageid"));
        System.out.println(msg.getString("message"));
        System.out.println(msg.getString("email"));
        System.out.println( msg.getString("timestamp"));

        return new ChatMessage(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("email"),
                msg.getString("timestamp"),
                msg.getString("firstname"),
                msg.getString("lastname"));
    }

    /**
     * @return the chat message as a String.
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * @return the sender as a String.
     */
    public String getSender() {
        return mSender;
    }

    /**
     * @return the timestamp as a String.
     */
    public String getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * @return the message ID as an integer.
     */
    public int getMessageId() {
        return mMessageId;
    }

    /**
     * @return the sender's first name as a String.
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * @return the sender's last name as a String.
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageId == ((ChatMessage) other).mMessageId;
        }
        return result;
    }
}
