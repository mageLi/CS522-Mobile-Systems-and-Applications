package edu.stevens.cs522.bookstore.providers;

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
import android.text.TextUtils;

import java.util.HashMap;

import edu.stevens.cs522.bookstore.contracts.AuthorContract;
import edu.stevens.cs522.bookstore.contracts.BookContract;

import static android.drm.DrmStore.DrmObjectType.CONTENT;

public class BookProvider extends ContentProvider {
    public BookProvider() {
    }

    private static final String AUTHORITY = BookContract.AUTHORITY;

    private static final String CONTENT_PATH = BookContract.CONTENT_PATH;

    private static final String CONTENT_PATH_ITEM = BookContract.CONTENT_PATH_ITEM;


    private static final String DATABASE_NAME = "books.db";

    private static final int DATABASE_VERSION = 5;

    private static final String BOOKS_TABLE = "books";

    private static final String AUTHORS_TABLE = "authors";

    private static final String CREATE_BOOKS_TABLE ="create table "+BookContract.BOOKS_TABLE+" ("+BookContract.ID +" integer primary key autoincrement,"+
            BookContract.TITLE+" text,"+BookContract.AUTHORS +" text,"+BookContract.ISBN+" text,"+BookContract.PRICE+" text);";

    private static final String CREATE_AUTHORS_TABLE ="create table "+ AuthorContract.AUTHORS_TABLE+" ("+AuthorContract.ID +" integer primary key autoincrement,"+
            AuthorContract.NAME+" text,"+AuthorContract.BOOK_FK+
            " integer not null,foreign key ("+AuthorContract.BOOK_FK+") references "+BookContract.BOOKS_TABLE+"("+ BookContract.ID +")on delete cascade );";

    private static final String DROP_BOOKS_TABLE = "DROP TABLE IF EXISTS " + BOOKS_TABLE;

    private static final String DROP_AUTHORS_TABLE = "DROP TABLE IF EXISTS " + AUTHORS_TABLE;

    public static String CREATE_INDEX="Create index AuthorsBookIndex on "+AuthorContract.AUTHORS_TABLE+"("+ AuthorContract.BOOK_FK+");";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int ALL_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    public static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO initialize database tables finished
            db.execSQL(CREATE_BOOKS_TABLE);
            db.execSQL(CREATE_AUTHORS_TABLE);
            db.execSQL(CREATE_INDEX);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
            db.execSQL(DROP_BOOKS_TABLE);
            db.execSQL(DROP_AUTHORS_TABLE);
            onCreate(db);
        }
    }
    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup.
        dbHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        if(sqLiteDatabase !=null)
            return true;
        else
            return false;
    }

    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;

    // uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH_ITEM, SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor/vnd."
                        + AUTHORITY + "."
                        + CONTENT + "s";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd."
                        + AUTHORITY + "."
                        + CONTENT;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new row.
                // Make sure to notify any observers
                long rowId= sqLiteDatabase.insert(BookContract.BOOKS_TABLE, null, values);
                if (rowId > 0){
                    Uri appendedId = ContentUris.withAppendedId(BookContract.CONTENT_URI,rowId);
                    getContext().getContentResolver().notifyChange(appendedId , null);
                    return appendedId;
                }
            case SINGLE_ROW:
                throw new IllegalArgumentException("insert expects a whole-table URI");
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                // TODO: Implement this to handle query of all books.
                String inTables = BookContract.BOOKS_TABLE
                        + " LEFT OUTER JOIN " + AuthorContract.AUTHORS_TABLE
                        + " ON (" + BookContract.BOOKS_TABLE + "." + BookContract.ID
                        + " = " + AuthorContract.AUTHORS_TABLE + "." + AuthorContract.BOOK_FK + ")";
                sqLiteQueryBuilder.setTables(inTables);
                HashMap<String, String> map = new HashMap<>();
                for (String field :
                        projection) {
                    if (!map.containsKey(field)) map.put(field, field);
                }
                sqLiteQueryBuilder.setProjectionMap(map);
                break;
            case SINGLE_ROW:
                // TODO: Implement this to handle query of a specific book.
                sqLiteQueryBuilder.setTables(BOOKS_TABLE);
                sqLiteQueryBuilder.appendWhere(BookContract.ID + " = " + BookContract.getId(uri));
                break;
            default:
                throw new IllegalStateException("insert: bad case");
        }
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new IllegalStateException("Update of books not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        int rowId=0;
        switch (uriMatcher.match(uri)){
            case ALL_ROWS:
                rowId= sqLiteDatabase.delete(BookContract.BOOKS_TABLE, selection, selectionArgs);
                sqLiteDatabase.delete(AuthorContract.AUTHORS_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case SINGLE_ROW:
                String id = uri.getPathSegments().get(1);
                rowId= sqLiteDatabase.delete(BookContract.BOOKS_TABLE, BookContract.BOOKS_TABLE+"."+BookContract.ID +" = "+Integer.parseInt(id)+(!TextUtils.isEmpty(selection) ? " AND (" + selection+ ')':""),selectionArgs);
                sqLiteDatabase.delete(AuthorContract.AUTHORS_TABLE, AuthorContract.AUTHORS_TABLE + "." + AuthorContract.BOOK_FK + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return rowId;
    }

}
