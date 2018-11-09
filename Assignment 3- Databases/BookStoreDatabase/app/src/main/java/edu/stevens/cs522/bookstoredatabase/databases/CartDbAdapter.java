package edu.stevens.cs522.bookstoredatabase.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.stevens.cs522.bookstoredatabase.contracts.BookContract;
import edu.stevens.cs522.bookstoredatabase.entities.Author;
import edu.stevens.cs522.bookstoredatabase.entities.Book;

/**
 * Created by dduggan.
 */

public class CartDbAdapter {

    private static final String DATABASE_NAME = "books.db";

    private static final String BOOK_TABLE = "books";

    private static final String AUTHOR_TABLE = "authors";

    private static final int DATABASE_VERSION = 1;

    private static final String BOOK_FK = "foreign_key";

    private static final String ID = "_id";

    private static final String TITLE = "title";

    private static final String ISBN = "isbn";

    private static final String PRICE = "price";

    private static final String FIRST_NAME = "first_name";

    private static final String MIDDLE_NAME = "middle_name";

    private static final String LAST_NAME = "last_name";

    public static String lastRowID = "select last_insert_rowid() from " + BOOK_TABLE;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private static final String DATABASE_CREATE_BOOK_TABLE = "create table " + BOOK_TABLE + " (" + ID + " integer primary key autoincrement," +
            TITLE + " text," + ISBN + " text," + PRICE + " text);";

    private static final String DATABASE_CREATE_AUTHOR_TABLE = "create table " + AUTHOR_TABLE + " (" + ID + " integer primary key autoincrement," +
            FIRST_NAME + " text," + MIDDLE_NAME + " text," + LAST_NAME + " text," + BOOK_FK + " integer not null,foreign key (" + BOOK_FK + ") references " +
            BOOK_TABLE + "(" + ID + ")on delete cascade );";

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = null; // TODO hold

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL(DATABASE_CREATE_BOOK_TABLE);
            db.execSQL(DATABASE_CREATE_AUTHOR_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
            onCreate(db);
        }
    }


    public CartDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODO
        db = dbHelper.getWritableDatabase();
    }

    public Cursor fetchAllBooks() {
        // TODO
        db = dbHelper.getReadableDatabase();
        final String query =
                "SELECT Books._id, title, price, isbn, GROUP_CONCAT(last_name,', ') as authors " +
                        "FROM Books LEFT OUTER JOIN Authors ON Books._id = Authors.foreign_key " +
                        "GROUP BY Books._id, title, price, isbn ";
        return db.rawQuery(query, null);

    }

    public Book fetchBook(long rowId) {
        // TODO
        db = dbHelper.getReadableDatabase();
        String[] vac = {String.valueOf(rowId)};
        final String query =
                "SELECT Books._id, title, price, isbn, GROUP_CONCAT(last_name,', ') as authors " +
                        "FROM Books LEFT OUTER JOIN Authors " +
                        "ON Books._id = Authors.foreign_key AND Books._id = ? " +
                        "GROUP BY Books._id, title, price, isbn";
        Cursor cursor = db.rawQuery(query, vac);
        return new Book(cursor);
    }

    public void persist(Book book) throws SQLException {
        // TODO
        open();
        ContentValues values = new ContentValues();
        values.put(TITLE, book.title);
        values.put(ISBN, book.isbn);
        values.put(PRICE, book.price);
        db.insert(BOOK_TABLE, null, values);
        values.clear();
        Cursor cursor = db.rawQuery(lastRowID, null);
        cursor.moveToFirst();

        for (Author a : book.authors) {
            values.put(FIRST_NAME, a.firstName);
            values.put(MIDDLE_NAME, a.middleInitial);
            values.put(LAST_NAME, a.lastName);

            if (cursor != null)
                values.put(BOOK_FK, cursor.getInt(0));
            db.insert(AUTHOR_TABLE, null, values);
            values.clear();
        }
        cursor.close();
    }

    public boolean delete(Book book) {
        // TODO
        open();
        String whereClause = ISBN+"=?";
        String[] whereArgs = new String[]{String.valueOf(book.isbn)};

        return db.delete(BOOK_TABLE, whereClause, whereArgs) > 0;
    }

    public boolean deleteAll() {
        // TODO
        open();
        String whereClause = null;
        String[] whereArgs = null;
        return db.delete(BOOK_TABLE, whereClause, whereArgs) > 0;
    }

    public void close() {
        // TODO
        db.close();
    }

}
