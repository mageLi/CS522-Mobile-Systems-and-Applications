package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.util.DateUtils;
import edu.stevens.cs522.chatserver.util.InetAddressUtils;

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

    public Peer() {
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Cursor cursor){
        // TODO
        this.name = PeerContract.getName(cursor);
        this.timestamp = PeerContract.getTimestamp(cursor);
        this.address = InetAddressUtils.getAddress(cursor, 3);
        this.port = Integer.parseInt(PeerContract.getPort(cursor));
    }

    protected Peer(Parcel in) {
        this.id = in.readLong();
        name = in.readString();
        timestamp = DateUtils.readDate(in);
        address = InetAddressUtils.readAddress(in);
        port = in.readInt();
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
        dest.writeLong(this.id);
        dest.writeString(name);
        DateUtils.writeDate(dest, this.timestamp);
        InetAddressUtils.writeAddress(dest, this.address);
        dest.writeInt(port);
    }

    public void writeToProvider(ContentValues values) {
        PeerContract.putName(values, name);
        PeerContract.putTimestamp(values, timestamp.toString());
        PeerContract.putAddress(values, address.toString());
        PeerContract.putPort(values, port);
    }
}
