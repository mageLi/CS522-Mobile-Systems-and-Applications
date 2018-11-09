package edu.stevens.cs522.chat.async;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.TypedCursor;

/**
 * Created by dduggan.
 */

public class QueryBuilder<T> implements LoaderManager.LoaderCallbacks<Cursor> {

    public static interface IQueryListener<T> {

        public void handleResults(TypedCursor<T> results);

        public void closeResults();

    }

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
        QueryBuilder<T> qb = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager lm = context.getLoaderManager();
        lm.initLoader(loaderID, null, qb);
    }

    public static <T> void executeQuery(String tag, Activity context, Uri uri, int loaderID, String[] projection, String selection, String[] selectionArgs, IEntityCreator<T> creator, IQueryListener<T> listener) {
        QueryBuilder<T> qb = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager lm = context.getLoaderManager();
        lm.initLoader(loaderID, null, qb);
    }


    public static <T> void reexecuteQuery(String tag, Activity context, Uri uri, int loaderID, String[] projection, String selection, String[] selectionArgs, IEntityCreator<T> creator, IQueryListener<T> listener) {
        QueryBuilder<T> qb = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager lm = context.getLoaderManager();
        lm.destroyLoader(loaderID);
        lm.initLoader(loaderID, null, qb);
    }

    // TODO complete the implementation of this

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == loaderId) {
            String[] projection = null;
            switch (id) {
                case PeerManager.LOADER_ID:
                    projection = new String[]{
                            PeerContract.ID,
                            PeerContract.NAME,
                            PeerContract.TIMESTAMP,
                            PeerContract.LATITUDE,
                            PeerContract.LONGITUDE
                    };
                    break;
                case MessageManager.LOADER_ID:
                    projection = new String[]{
                            MessageContract.ID,
                            MessageContract.MESSAGE_TEXT,
                            MessageContract.SENDER,
                            MessageContract.TIMESTAMP,
                            MessageContract.LATITUDE,
                            MessageContract.LONGITUDE
                    };
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected loader id: " + id);
            }
            return new CursorLoader(context,
                    uri,
                    projection,
                    null,
                    null,
                    null);
        }
        throw new IllegalArgumentException("Unexpected loader id: " + id);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == loaderId) {
            listener.handleResults(new TypedCursor<T>(cursor, creator));
        } else {
            throw new IllegalStateException("Unexpected loader callback");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        if (loader.getId() == loaderId) {
            listener.closeResults();
        } else {
            throw new IllegalStateException("Unexpected loader callback");
        }
    }
}
