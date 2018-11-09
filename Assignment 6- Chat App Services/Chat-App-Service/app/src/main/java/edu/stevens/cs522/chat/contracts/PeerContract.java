package edu.stevens.cs522.chat.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class PeerContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Peer");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));


    // TODO define column names, getters for cursors, setters for contentvalues

    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PORT = "port";
    public static final String TIMESTAMP = "timestamp";

    public static String getTimestamp(Cursor c) {
        String sd[] = c.getColumnNames();
        for (String i : sd)
            Log.i(PeerContract.class.getCanonicalName(), i);
        return c.getString(c.getColumnIndexOrThrow(TIMESTAMP));
    }

    public static void putTimestamp(ContentValues values, Date timestamp) {
        values.put(TIMESTAMP, timestamp.toString());
    }

    public static String getId(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(ID));
    }

    public static void putId(ContentValues values, String id) {
        values.put(ID, id);
    }

    public static String getName(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(NAME));
    }

    public static void putName(ContentValues values, String name) {
        values.put(NAME, name);
    }

    public static String getAddress(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(ADDRESS));
    }

    public static void putAddress(ContentValues values, String address) {
        values.put(ADDRESS, address);
    }

    public static String getPort(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(PORT));
    }

    public static void putPort(ContentValues values, String port) {
        values.put(PORT, port);
    }

}
