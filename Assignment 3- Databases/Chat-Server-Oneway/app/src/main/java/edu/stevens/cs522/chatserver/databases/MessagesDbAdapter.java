package edu.stevens.cs522.chatserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class MessagesDbAdapter {

    private static final String DATABASE_NAME = "messages.db";

    private static final String MESSAGE_TABLE = "messages";

    private static final String PEER_TABLE = "view_peers";

    private static final int DATABASE_VERSION = 10;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private static final String PEER_NAME = "_name";
    private static final String KEY_TIMESTAMP = "_timestamp";
    private static final String PEER_ADDRESS = "_address";
    private static final String PEER_PORT = "_port";

    private static final String MESSAGE_TEXT= "_message";
    private static final String MESSAGE_SENDER = "_sender";
    private static final String MESSAGE_ID = "_senderid";


    public static class DatabaseHelper extends SQLiteOpenHelper {

        // TODO
        private static final String CREATE_TABLE_MESSAGE = "create table "+ MESSAGE_TABLE +
                "(_id integer primary key autoincrement,_message text,_timestamp text," +
                "_sender text," + "_senderid text);";
        private static final String CREATE_TABLE_PEER = "create table "+ PEER_TABLE +
                "(_id integer primary key autoincrement,_name text,_timestamp text," + "_address text,"+
                "_port text);";

        private static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS " + MESSAGE_TABLE;

        private static final String DROP_TABLE_PEER = "DROP TABLE IF EXISTS " + PEER_TABLE;

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL(CREATE_TABLE_MESSAGE);
            db.execSQL(CREATE_TABLE_PEER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL(DROP_TABLE_MESSAGE);
            db.execSQL(DROP_TABLE_PEER);
            onCreate(db);
        }
    }


    public MessagesDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODO
        db = dbHelper.getWritableDatabase();
    }


    public Cursor queryPeers(){
        return dbHelper.getReadableDatabase().query(true,PEER_TABLE,null,null,null,null,null,null,null);
    }

    public Cursor fetchAllMessages() {
        // TODO
        open();
        return db.query(true,MESSAGE_TABLE,null,null,null,null,null,null,null);
    }

    public Cursor fetchAllPeers() {
        // TODO
        open();
        return db.query(true,PEER_TABLE,null,null,null,null,null,null,null);
    }

    public Peer fetchPeer(long peerId) {
        // TODO
        open();
        String selectQuery = "select * from" + MESSAGE_TABLE + " where _sendid = " + peerId;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c.moveToFirst();
            Peer peer = new Peer(c);
            return peer;
        }
        return null;
    }

    public Cursor fetchMessagesFromPeer(Peer peer) {
        // TODO
        return null;
    }

    public void persist(Message message) throws SQLException {
        // TODO
        open();
        ContentValues values = new ContentValues();
        values.put(MESSAGE_TEXT, message.messageText);
        values.put(KEY_TIMESTAMP, message.timestamp);
        values.put(MESSAGE_SENDER, message.sender);
        values.put(MESSAGE_ID, message.senderId);

        db.insert(MESSAGE_TABLE, null, values);
    }

    /**
     * Add a peer record if it does not already exist; update information if it is already defined.
     * @param peer
     * @return The database key of the (inserted or updated) peer record
     * @throws SQLException
     */

    public long persist(Peer peer) {
        // TODO
        delete(peer);
        open();
        ContentValues values = new ContentValues();
        values.put(PEER_NAME, peer.name);
        values.put(KEY_TIMESTAMP, peer.timestamp);
        values.put(PEER_ADDRESS, peer.address);
        values.put(PEER_PORT, peer.port);

        db.insert(PEER_TABLE, null, values);
        return peer.id;

    }

    //if there are no peer that name equals to the current one, then
    //it couldn't find the peer to delete.
    public boolean delete(Peer peer) {
        // TODO
        db = dbHelper.getWritableDatabase();
        db.delete(PEER_TABLE, PEER_NAME + " = ?",
                new String[]{String.valueOf(peer.name)});
        return false;
    }

    public void close() {
        // TODO
        db.close();
    }
}