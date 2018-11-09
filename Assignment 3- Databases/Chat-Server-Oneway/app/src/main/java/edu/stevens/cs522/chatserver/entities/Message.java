package edu.stevens.cs522.chatserver.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Message implements Parcelable{

    public long id;

    public String messageText;

    public String timestamp;

    public String sender;

    public long senderId;

    public long getId() {
        return id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message() {
    }

    protected Message(Parcel in) {
        id = in.readLong();
        messageText = in.readString();
        sender = in.readString();
        senderId = in.readLong();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.messageText);
        parcel.writeString(this.sender);
        parcel.writeLong(this.senderId);
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues
    public Message(Cursor cursor){
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimestamp(cursor);
        this.sender = MessageContract.getSender(cursor);
        this.senderId=Long.parseLong(MessageContract.getSenderId(cursor));
    }
}

