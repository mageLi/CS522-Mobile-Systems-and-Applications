package edu.stevens.cs522.chatserver.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import edu.stevens.cs522.chatserver.contracts.BaseContract;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;

import static android.drm.DrmStore.DrmObjectType.CONTENT;

public class ChatProvider extends ContentProvider {

    public ChatProvider() {
    }

    private static final String AUTHORITY = BaseContract.AUTHORITY;

    private static final String MESSAGE_CONTENT_PATH = MessageContract.CONTENT_PATH;

    private static final String MESSAGE_CONTENT_PATH_ITEM = MessageContract.CONTENT_PATH_ITEM;

    private static final String PEER_CONTENT_PATH = PeerContract.CONTENT_PATH;

    private static final String PEER_CONTENT_PATH_ITEM = PeerContract.CONTENT_PATH_ITEM;


    private static final String DATABASE_NAME = "message.db";

    private static final int DATABASE_VERSION = 37;

    private static final String MESSAGES_TABLE = "messages";

    private static final String PEERS_TABLE = "peers";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int MESSAGES_ALL_ROWS = 1;
    private static final int MESSAGES_SINGLE_ROW = 2;
    private static final int PEERS_ALL_ROWS = 3;
    private static final int PEERS_SINGLE_ROW = 4;

    public static class DbHelper extends SQLiteOpenHelper {
        private static final String CREATE_PEERS_TABLE = "create table "+ PEERS_TABLE +
                "(_id integer primary key autoincrement,name text not null,timestamp text not null," +
                "address text not null," + "port text not null);";

        private static final String CREATE_MESSAGES_TABLE = "create table "+ MESSAGES_TABLE +
                "(_id integer primary key autoincrement,message text not null,timestamp text not null," +
                "sender text not null," + "senderId text);";

        private static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS " + MESSAGES_TABLE;

        private static final String DROP_TABLE_PEER = "DROP TABLE IF EXISTS " + PEERS_TABLE;
        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO initialize database tables
            db.execSQL(CREATE_PEERS_TABLE);
            db.execSQL(CREATE_MESSAGES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
            db.execSQL(DROP_TABLE_MESSAGE);
            db.execSQL(DROP_TABLE_PEER);
            onCreate(db);
        }
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup.
        dbHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;

    // uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH, MESSAGES_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH_ITEM, MESSAGES_SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH, PEERS_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH_ITEM, PEERS_SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case PEERS_ALL_ROWS:
                return "vnd.android.cursor/vnd." + AUTHORITY + "." + CONTENT + "s";
            case PEERS_SINGLE_ROW:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTENT;
            case MESSAGES_ALL_ROWS:
                return "vnd.android.cursor/vnd." + AUTHORITY + "." + CONTENT + "s";
            case MESSAGES_SINGLE_ROW:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTENT;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri insertUri;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new message.
                // Make sure to notify any observers
                long rowsId = db.insert(MESSAGES_TABLE, null, values);
                insertUri = ContentUris.withAppendedId(MessageContract.CONTENT_URI, rowsId);
                break;
            //throw new UnsupportedOperationException("Not yet implemented");
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new peer.
                // Make sure to notify any observers
                rowsId = db.insert(PEERS_TABLE, null, values);
                insertUri = ContentUris.withAppendedId(PeerContract.CONTENT_URI, rowsId);
                break;
            //throw new UnsupportedOperationException("Not yet implemented");
            case MESSAGES_SINGLE_ROW:
                throw new IllegalArgumentException("insert expects a whole-table URI");
            default:
                throw new IllegalStateException("insert: bad case");
        }
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle query of all messages.
                cursor = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle query of all peers.
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                cursor = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case MESSAGES_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific message.
                sqLiteQueryBuilder.setTables(MESSAGES_TABLE);
                sqLiteQueryBuilder.appendWhere(MessageContract.ID + " = " + MessageContract.getId(uri));
                break;
            case PEERS_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific peer.
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                sqLiteQueryBuilder.appendWhere(PeerContract.ID + " = " + PeerContract.getId(uri));
                break;
            default:
                throw new IllegalStateException("insert: bad case");
        }
        cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Implement this to handle requests to update one or more rows.
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case PEERS_ALL_ROWS:
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                break;
            case PEERS_SINGLE_ROW:
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                sqLiteQueryBuilder.appendWhere(PeerContract.ID + " = " + PeerContract.getId(uri));
                break;
            case MESSAGES_ALL_ROWS:
                sqLiteQueryBuilder.setTables(MESSAGES_TABLE);
                break;
            case MESSAGES_SINGLE_ROW:
                sqLiteQueryBuilder.setTables(MESSAGES_TABLE);
                sqLiteQueryBuilder.appendWhere(MessageContract.ID + " = " + MessageContract.getId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        Cursor cursor = sqLiteQueryBuilder.query(db, null, selection, selectionArgs, null, null, null);
        return cursor.getCount();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        int rowsDeleted ;
        Uri deleteUri ;
        switch (uriMatcher.match(uri)) {
            case PEERS_ALL_ROWS:
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                rowsDeleted = db.delete(PEERS_TABLE, selection, selectionArgs);
                deleteUri = PeerContract.CONTENT_URI;
                break;
            case PEERS_SINGLE_ROW:
                sqLiteQueryBuilder.setTables(PEERS_TABLE);
                selection = PeerContract.ID + " = ?";
                selectionArgs = new String[]{
                        uri.getLastPathSegment()
                };
                rowsDeleted = db.delete(PEERS_TABLE, selection, selectionArgs);
                deleteUri = PeerContract.CONTENT_URI;
                break;
            case MESSAGES_ALL_ROWS:
                sqLiteQueryBuilder.setTables(MESSAGES_TABLE);
                rowsDeleted = db.delete(MESSAGES_TABLE, selection, selectionArgs);
                deleteUri = MessageContract.CONTENT_URI;
                break;
            case MESSAGES_SINGLE_ROW:
                sqLiteQueryBuilder.setTables(MESSAGES_TABLE);
                selection = MessageContract.ID + " = ?";
                selectionArgs = new String[]{
                        uri.getLastPathSegment()
                };
                rowsDeleted = db.delete(MESSAGES_TABLE, selection, selectionArgs);
                deleteUri = MessageContract.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(deleteUri, null);
        return rowsDeleted;
    }

}
