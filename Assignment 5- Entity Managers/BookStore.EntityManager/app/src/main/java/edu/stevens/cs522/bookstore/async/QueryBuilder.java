package edu.stevens.cs522.bookstore.async;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.managers.TypedCursor;

/**
 * Created by dduggan.
 */

public class QueryBuilder<T> implements LoaderManager.LoaderCallbacks<Cursor> {

    public static interface IQueryListener<T> {

        public void handleResults(TypedCursor<T> results);

        public void closeResults();

    }

    // TODO complete the implementation of this

    private String tag;
    private Context context;
    private Uri uri;
    private int loaderId;
    private IEntityCreator<T> creator;
    private IQueryListener<T> listener;

    public QueryBuilder(String tag, Context context, Uri uri, int loaderId, IEntityCreator<T> creator, IQueryListener<T> listener) {
        this.tag = tag;
        this.context = context;
        this.uri = uri;
        this.loaderId = loaderId;
        this.creator = creator;
        this.listener = listener;
    }

    public static <T> void executeQuery(String tag,
                                        Activity context,
                                        Uri uri,
                                        int loaderID,
                                        IEntityCreator<T> creator,
                                        IQueryListener<T> listener) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager loaderManager = context.getLoaderManager();
        loaderManager.initLoader(loaderID, null, queryBuilder);
    }

    public static <T> void executeQuery(String tag, Activity context, Uri uri, int loaderID, String[] projection, String selection, String[] selectionArgs, IEntityCreator<T> creator, IQueryListener<T> listener) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager loaderManager = context.getLoaderManager();
        loaderManager.initLoader(loaderID, null, queryBuilder);
    }


    public static <T> void reExecuteQuery(String tag, Activity context, Uri uri, int loaderID, String[] projection, String selection, String[] selectionArgs, IEntityCreator<T> creator, IQueryListener<T> listener) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager loaderManager = context.getLoaderManager();
        loaderManager.destroyLoader(loaderID);
        loaderManager.initLoader(loaderID, null, queryBuilder);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == loaderId) {
            String[] projection = new String[]{
                    BookContract.BOOKS_TABLE + "." + BookContract.ID,
                    BookContract.TITLE,
                    BookContract.ISBN,
                    BookContract.PRICE,
                    BookContract.AUTHORS,
            };
            return new CursorLoader(context,
                    uri,
                    projection,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(context.getContentResolver(), uri);
        if (loader.getId() == loaderId) {
            listener.handleResults(new TypedCursor<T>(data, creator));
        } else {
            throw new IllegalStateException("Unexpected loader callback");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == loaderId) {
            listener.closeResults();
        } else {
            throw new IllegalStateException("Unexpected loader callback");
        }
    }
}
