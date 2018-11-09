package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;

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

    public InetAddress address;

    public int port;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Cursor cursor) {
        // TODO
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        this.id = Long.parseLong(PeerContract.getId(cursor));
        this.name = PeerContract.getName(cursor);
        String temp = PeerContract.getAddress(cursor).substring(1);
        this.address = InetAddresses.forString(temp);
        this.port = Integer.parseInt(PeerContract.getPort(cursor));
        this.timestamp = DateUtils.getDate(cursor,4);
    }

    public Peer() {
    }

    public int describeContents() {
        return 0;
    }

    public Peer(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.address = InetAddressUtils.readAddress(in);
        this.port = in.readInt();
        this.timestamp = DateUtils.readDate(in);
    }

    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(this.id);
        dest.writeString(this.name);
        InetAddressUtils.writeAddress(dest, this.address);
        dest.writeInt(this.port);
        DateUtils.writeDate(dest,this.timestamp);
    }

    public static final Parcelable.Creator<Peer> CREATOR = new Parcelable.Creator<Peer>() {
        public Peer createFromParcel(Parcel source) {
            return new Peer(source);
        }

        public Peer[] newArray(int size) {
            return new Peer[size];
        }
    };
    public void writeToProvider(ContentValues values) {
        PeerContract.putName(values, name);
        PeerContract.putAddress(values, address.toString());
        PeerContract.putPort(values, String.valueOf(port));
        PeerContract.putTimestamp(values, timestamp);
    }
}
