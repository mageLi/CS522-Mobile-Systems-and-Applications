package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by dduggan.
 */

public class PeerContract implements BaseColumns {

    // TODO define column names, getters for cursors, setters for contentvalues

    public static final String ID = "_id";

    public static final String NAME = "_name";

    public static final String TIMESTAMP = "_timestamp";

    public static final String ADDRESS = "_address";

    public static final String PORT = "_port";


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


    private static int timestampColumn = -1;

    public static String getTimestamp (Cursor cursor) {
        if (timestampColumn < 0) {
            timestampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        return cursor.getString(timestampColumn);
    }

    public static void putTimestamp (ContentValues out, String timestamp) {
        out.put(TIMESTAMP, timestamp);
    }

    private static int addressColumn = -1;

    public static String getAddress(Cursor cursor) {
        if (addressColumn < 0) {
            addressColumn = cursor.getColumnIndexOrThrow(ADDRESS);
        }
        return cursor.getString(addressColumn);
    }

    public static void putAddress(ContentValues out, String add) {
        out.put(ADDRESS, add);
    }

    private static int portColumn = -1;

    public static String getPort(Cursor cursor) {
        if (portColumn < 0) {
            portColumn = cursor.getColumnIndexOrThrow(PORT);
        }
        return cursor.getString(portColumn);
    }

    public static void putPort(ContentValues out, int port) {
        out.put(PORT, port);
    }



}
