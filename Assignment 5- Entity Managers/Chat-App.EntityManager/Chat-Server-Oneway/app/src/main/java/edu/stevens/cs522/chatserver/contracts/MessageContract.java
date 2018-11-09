package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class MessageContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Message");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));

    public static final String ID = _ID;

    public static final String MESSAGE_TEXT = "message_text";

    public static final String SENDER = "sender";

    // TODO remaining columns in Messages table

    public static final String CONTENT_TYPE = "vnd.android.cursor/vnd."
            + AUTHORITY + "."
            + "messages";
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "."
            + "message";

    private static int messageTextColumn = -1;

    public static String getMessageText(Cursor cursor) {
        if (messageTextColumn < 0) {
            messageTextColumn = cursor.getColumnIndexOrThrow(MESSAGE_TEXT);
        }
        return cursor.getString(messageTextColumn);
    }

    public static void putMessageText(ContentValues out, String messageText) {
        out.put(MESSAGE_TEXT, messageText);
    }
    // TODO remaining getter and putter operations for other columns

    public static String getId(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ID));
    }

    public static void putId(ContentValues values, String id) {
        values.put(ID, id);
    }

    public static String getSender(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(SENDER));
    }

    public static void putSender(ContentValues values, String sender) {
        values.put(SENDER, sender);
    }

}
