package edu.stevens.cs522.chatserver.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable{

    public long id;

    public String name;

    // Last time we heard from this peer.
    public String timestamp;

    public String address;

    public int port;

    protected Peer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        port = in.readInt();
    }

    public Peer() {
    }

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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(port);
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues
    public Peer(Cursor cursor){
        this.name = PeerContract.getName(cursor);
        this.timestamp = PeerContract.getTimestamp(cursor);
        this.address = PeerContract.getAddress(cursor);
        this.port = Integer.parseInt(PeerContract.getPort(cursor));
    }
}