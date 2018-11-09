package edu.stevens.cs522.bookstore.managers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;
import java.util.Set;

import edu.stevens.cs522.bookstore.async.AsyncContentResolver;
import edu.stevens.cs522.bookstore.async.IContinue;
import edu.stevens.cs522.bookstore.async.IEntityCreator;
import edu.stevens.cs522.bookstore.async.QueryBuilder;
import edu.stevens.cs522.bookstore.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.bookstore.async.SimpleQueryBuilder;
import edu.stevens.cs522.bookstore.contracts.AuthorContract;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;

import static edu.stevens.cs522.bookstore.contracts.BookContract.CONTENT_URI;

/**
 * Created by dduggan.
 */

public class BookManager extends Manager<Book> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<Book> creator = new IEntityCreator<Book>() {
        @Override
        public Book create(Cursor cursor) {
            return new Book(cursor);
        }
    };

    private AsyncContentResolver contentResolver;

    public BookManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllBooksAsync(IQueryListener<Book> listener) {
        // TODO use QueryBuilder to complete this
        QueryBuilder.executeQuery(
                tag,
                (Activity) context,
                BookContract.CONTENT_URI, LOADER_ID,
                new IEntityCreator<Book>() {
                    @Override
                    public Book create(Cursor cursor) {
                        return null;
                    }
                },
                listener
        );
    }

    public void getBookAsync(long id, final IContinue<Book> callback) {
        // TODO
        SimpleQueryBuilder.executeQuery((Activity)context, BookContract.CONTENT_URI(id),
                creator,  new SimpleQueryBuilder.ISimpleQueryListener<Book>() {
                    public void handleResults(List<Book> books) {
                        callback.kontinue(books.get(0));
                    }
                });
    }

    public void persistAsync(final Book book) {
        // TODO
        ContentValues values=new ContentValues();
        book.writeToProvider(values);
        contentResolver.insertAsync(CONTENT_URI,values,new IContinue<Uri>() {
                    public void kontinue(Uri uri) {
                        book.setId(Integer.parseInt(String.valueOf(BookContract.getId(uri))));
                        getSyncResolver().notifyChange(uri, null);
                    }
                }
        );
    }

    public void deleteBooksAsync(Set<Long> toBeDeleted) {
        Long[] ids = new Long[toBeDeleted.size()];
        toBeDeleted.toArray(ids);
        String[] args = new String[ids.length];

        StringBuilder sb = new StringBuilder();
        if (ids.length > 0) {
            sb.append(AuthorContract.ID);
            sb.append("=?");
            args[0] = ids[0].toString();
            for (int ix=1; ix<ids.length; ix++) {
                sb.append(" or ");
                sb.append(AuthorContract.ID);
                sb.append("=?");
                args[ix] = ids[ix].toString();
            }
        }
        String select = sb.toString();

        contentResolver.deleteAsync(CONTENT_URI, select, args);
    }
    public void deleteBooksAsync(IContinue<Integer> callback) {
        contentResolver.deleteAsync(BookContract.CONTENT_URI, null, null, callback);
    }
}
