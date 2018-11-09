package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chat.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class ChatMessage implements Parcelable {

    public long id;

    public String messageText;

    public Date timestamp;

    public Double longitude;

    public Double latitude;

    public String sender;

   // public long senderId;

    public ChatMessage() {
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public ChatMessage(Cursor cursor) {
        // TODO
        this.id = Long.parseLong(MessageContract.getId(cursor));
        this.sender = MessageContract.getSender(cursor);
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimestamp(cursor);
        this.longitude = MessageContract.getLongitude(cursor);
        this.latitude = MessageContract.getLatitude(cursor);
    }

    protected ChatMessage(Parcel in) {
        id = in.readLong();
        sender = in.readString();
        messageText = in.readString();
        timestamp = new Date(in.readLong());
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(sender);
        parcel.writeString(messageText);
        parcel.writeLong(timestamp.getTime());
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }

    public void writeToProvider(ContentValues values) {
        // TODO
        MessageContract.putSender(values, sender);
        MessageContract.putTimestamp(values, timestamp);
        MessageContract.putMessageText(values, messageText);
        MessageContract.putLongitude(values, longitude);
        MessageContract.putLatitude(values, latitude);
    }
}
