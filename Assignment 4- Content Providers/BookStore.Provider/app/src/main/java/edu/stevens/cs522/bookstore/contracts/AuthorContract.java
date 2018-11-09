package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by dduggan.
 */

public class AuthorContract implements BaseColumns {

    public static final String ID = _ID;

    public static final String FIRST_NAME = "firstName";

    public static final String MIDDLE_INITIAL = "initialName";

    public static final String LAST_NAME = "lastName";

    public static final String BOOK_FK = "foreign_key";

    /*
     * NAME column
     */

    private static int nameColumn = -1;

    public static String getFirstName(Cursor cursor) {
        if (nameColumn < 0) {
            nameColumn =  cursor.getColumnIndexOrThrow(FIRST_NAME);;
        }
        return cursor.getString(nameColumn);
    }

    public static void putFirstName(ContentValues values, String firstName) {
        values.put(FIRST_NAME, firstName);
    }

    // TODO complete the definitions of the operations for Parcelable, cursors and contentvalues

    private static int lastNameColumn = -1;

    public static String getLastName(Cursor cursor) {
        if (lastNameColumn < 0)
            lastNameColumn = cursor.getColumnIndexOrThrow(LAST_NAME);
        return cursor.getString(lastNameColumn);
    }

    public static void putLastName(ContentValues values, String lastName) {
        values.put(LAST_NAME, lastName);
    }

    private static int middleInitialColumn = -1;

    public static String getMiddleInitial(Cursor c) {
        if (middleInitialColumn < 0)
            middleInitialColumn = c.getColumnIndexOrThrow(MIDDLE_INITIAL);
        return c.getString(middleInitialColumn);
    }

    public static void putMiddleInitial(ContentValues values, String middleInitial) {
        values.put(MIDDLE_INITIAL, middleInitial);
    }

    public static int getId(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(ID));
    }

    public static void putId(ContentValues values, int id) {
        values.put(ID, id);
    }
}
