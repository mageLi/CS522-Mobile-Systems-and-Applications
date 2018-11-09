package edu.stevens.cs522.chat.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import static android.drm.DrmStore.DrmObjectType.CONTENT;

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

    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String TIMESTAMP = "timestamp";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    // TODO define column names, getters for cursors, setters for contentvalues

    private static int idColumn = -1;

    public static String getId(Cursor cursor) {
        if (idColumn < 0) {
            idColumn = cursor.getColumnIndexOrThrow(ID);
        }
        return cursor.getString(idColumn);
    }

    public static void putId(ContentValues out, Long id) {
        out.put(ID, id);
    }

    private static int nameColumn = -1;

    public static String getName(Cursor cursor) {
        if (nameColumn < 0) {
            nameColumn = cursor.getColumnIndexOrThrow(NAME);
        }
        return cursor.getString(nameColumn);
    }

    public static void putName (ContentValues out, String name) {
        out.put(NAME, name);
    }

    public static Date getTimestamp(Cursor cursor) {
        return new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TIMESTAMP)));
    }

    public static void putTimestamp(ContentValues values, Date timeStamp) {
        values.put(TIMESTAMP, timeStamp.getTime());
    }

    public static Double getLatitude(Cursor c) {
        return c.getDouble(c.getColumnIndexOrThrow(LATITUDE));
    }

    public static void putLatitude(ContentValues values, Double latitude) {
        values.put(LATITUDE, latitude);
    }

    public static Double getLongitude(Cursor c) {
        return c.getDouble(c.getColumnIndexOrThrow(LONGITUDE));
    }

    public static void putLongitude(ContentValues values, Double longitude) {
        values.put(LONGITUDE, longitude);
    }

}
