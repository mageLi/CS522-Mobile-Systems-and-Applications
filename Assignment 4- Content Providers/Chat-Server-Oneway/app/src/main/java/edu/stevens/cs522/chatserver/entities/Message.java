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

public class Message implements Parcelable{

    public long id;

    public String messageText;

    public Date timestamp;

    public String sender;

    public long senderId;

    public Message() {
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    protected Message(Parcel in) {
        id = in.readLong();
        messageText = in.readString();
        timestamp=new Date(in.readLong());
        sender = in.readString();
        senderId = in.readLong();
    }

    public Message(Cursor cursor) {
        // TODO
        this.id = Long.parseLong(MessageContract.getId(cursor));
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimestamp(cursor);
        this.sender = MessageContract.getSender(cursor);
        this.senderId = Long.parseLong(MessageContract.getSenderId(cursor));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(messageText);
        parcel.writeLong(timestamp.getTime());
        parcel.writeString(sender);
        parcel.writeLong(senderId);
    }

    public void writeToProvider(ContentValues values) {
        MessageContract.putSender(values, sender);
        MessageContract.putTimestamp(values, timestamp.toString());
        MessageContract.putMessageText(values,messageText);
    }

}
