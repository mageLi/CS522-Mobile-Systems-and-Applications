package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class Message {

    public long id;

    public String messageText;

    //public Date timestamp;

    public String sender;

    public long senderId;

    public Message(){}

    // TODO add operations for parcels (Parcelable), cursors and contentvalues


    protected Message(Parcel in) {
        this.id = in.readLong();
        this.messageText = in.readString();
        this.sender = in.readString();
        this.senderId=in.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.messageText);
        dest.writeString(this.sender);
        dest.writeLong(this.senderId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public Message(Cursor cursor){
        // TODO
        this.id = Long.parseLong(MessageContract.getId(cursor));
        this.messageText = MessageContract.getMessageText(cursor);
        this.sender = MessageContract.getSender(cursor);
        //this.senderId=
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public void writeToProvider(ContentValues values) {
        MessageContract.putSender(values,sender);
        MessageContract.putMessageText(values,messageText);
    }

}