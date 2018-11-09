package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;

/**
 * Created by dduggan.
 *
 * The API used by the Web service for synchronizing messages with server.
 * It is assumed that all operations are invoked on the background thread for Web services.
 */

public class RequestManager extends Manager<ChatMessage> {

    private static final int LOADER_ID = 1;

    private static final String MAX_SEQNO_COLUMN = "max_seq_num";

    private static final IEntityCreator<ChatMessage> creator = new IEntityCreator<ChatMessage>() {
        @Override
        public ChatMessage create(Cursor cursor) {
            return new ChatMessage(cursor);
        }
    };

    public RequestManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void persist(ChatMessage message) {
        ContentValues values = new ContentValues();
        message.writeToProvider(values);
        Uri uri = getSyncResolver().insert(MessageContract.CONTENT_URI, values);
        message.id = MessageContract.getId(uri);
    }

    /**
     * Get the last sequence number in the messages database.
     * @return
     */
    public long getLastSequenceNumber() {
        String selection = MessageContract.SEQUENCE_NUMBER + "<>0";
        String[] selectionArgs = { };
        String[] columns = { String.format("MAX(%s) as %s", MessageContract.SEQUENCE_NUMBER, MAX_SEQNO_COLUMN) };
        Cursor cursor = getSyncResolver().query(MessageContract.CONTENT_URI, columns, selection, selectionArgs, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            } else {
                // Empty database
                return 0;
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Get all unsent messages, identified by sequence number = 0.
     * @return
     */
    public TypedCursor<ChatMessage> getUnsentMessages() {
        String selection = MessageContract.SEQUENCE_NUMBER + "=0";
        String[] selectionArgs = { };
        String[] columns = MessageContract.COLUMNS;
        Cursor cursor = getSyncResolver().query(MessageContract.CONTENT_URI, columns, selection, selectionArgs, MessageContract.TIMESTAMP);
        try {
            return new TypedCursor(cursor, creator);
        } finally {
            cursor.close();
        }
    }

    /**
     * After syncing with server, update the sequence numbers of uploaded messages
     * @param id
     * @param seqNum
     */
    public void updateSeqNum(long id, long seqNum) {
        Uri uri = MessageContract.CONTENT_URI(id);
        ContentValues values = new ContentValues();
        values.put(MessageContract.SEQUENCE_NUMBER, seqNum);
        if (getSyncResolver().update(uri, values, null, null) != 1) {
            throw new IllegalStateException("Unexpected result from update of message seq num!");
        }
    }

}
