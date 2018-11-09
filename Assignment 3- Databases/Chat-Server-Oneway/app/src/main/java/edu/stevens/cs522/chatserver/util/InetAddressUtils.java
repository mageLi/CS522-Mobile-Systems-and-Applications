package edu.stevens.cs522.chatserver.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.google.common.net.InetAddresses;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by dduggan.
 */

public class InetAddressUtils {

    public static String sourceAddress(DatagramPacket pkt) {
        return toIpAddress(pkt.getAddress().getAddress());
    }

    public static String toIpAddress(byte[] rawBytes) {
        int i = 4;
        StringBuffer ipAddress = new StringBuffer();
        for (byte raw : rawBytes) {
            ipAddress.append(raw & 0xFF);
            if (--i > 0) {
                ipAddress.append('.');
            }
        }
        return ipAddress.toString();
    }

    public static InetAddress readAddress(Parcel in) {
        return InetAddresses.forString(in.readString());
    }

    public static void writeAddress(Parcel out, InetAddress address) {
        out.writeString(InetAddresses.toAddrString(address));
    }

    public static InetAddress getAddress(Cursor cursor, int key) {
        return InetAddresses.forString(cursor.getString(key));
    }

    public static void putAddress(ContentValues out, String key, InetAddress address) {
        out.put(key, InetAddresses.toAddrString(address));
    }

}
