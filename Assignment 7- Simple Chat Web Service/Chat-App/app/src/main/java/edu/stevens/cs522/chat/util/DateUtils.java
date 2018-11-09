package edu.stevens.cs522.chat.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class DateUtils {

    public static Date readDate(Parcel in) {
        return new Date(in.readLong());
    }

    public static void writeDate(Parcel out, Date date) {
        out.writeLong(date.getTime());
    }

    public static Date getDate(Cursor cursor, int key) {
        return new Date(cursor.getLong(key));
    }

    public static void putDate(ContentValues out, String key, Date date) {
        out.put(key, date.getTime());
    }

    public static Date now() {
        return new Date(System.currentTimeMillis());
    }
}
