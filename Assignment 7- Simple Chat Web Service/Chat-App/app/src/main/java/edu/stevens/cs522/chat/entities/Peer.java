package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.util.DateUtils;
import edu.stevens.cs522.chat.util.InetAddressUtils;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable {

    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    public Double longitude;

    public Double latitude;

    public Peer() {
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Cursor cursor) {
        // TODO
        this.id = Long.parseLong(PeerContract.getId(cursor));
        this.name = PeerContract.getName(cursor);
        //this.timestamp = DateUtils.getDate(cursor, 4);
        this.timestamp = PeerContract.getTimestamp(cursor);
        this.longitude = PeerContract.getLongitude(cursor);
        this.latitude = PeerContract.getLatitude(cursor);
    }

    protected Peer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        //timestamp = new Date(in.readLong());
        timestamp = (Date) in.readSerializable();
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
    }

    public static final Creator<Peer> CREATOR = new Creator<Peer>() {
        @Override
        public Peer createFromParcel(Parcel in) {
            return new Peer(in);
        }

        @Override
        public Peer[] newArray(int size) {
            return new Peer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(id);
        dest.writeString(name);
        //dest.writeLong(timestamp.getTime());
        dest.writeSerializable(this.timestamp);
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
    }

    public void writeToProvider(ContentValues values) {
        //PeerContract.putId(values, id);
        PeerContract.putName(values, name);
        PeerContract.putTimestamp(values, timestamp);
        PeerContract.putLatitude(values, latitude);
        PeerContract.putLongitude(values, longitude);
    }
}
