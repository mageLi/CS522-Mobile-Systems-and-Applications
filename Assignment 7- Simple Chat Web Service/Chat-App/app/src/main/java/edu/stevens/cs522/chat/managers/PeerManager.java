package edu.stevens.cs522.chat.managers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chat.async.SimpleQueryBuilder;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;


/**
 * Created by dduggan.
 */

public class PeerManager extends Manager<Peer> {

    public static final int LOADER_ID = 2;

    private static final IEntityCreator<Peer> creator = new IEntityCreator<Peer>() {
        @Override
        public Peer create(Cursor cursor) {
            return new Peer(cursor);
        }
    };

    private AsyncContentResolver contentResolver;

    public PeerManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllPeersAsync(IQueryListener<Peer> listener) {
        // TODO use QueryBuilder to complete this
        QueryBuilder.executeQuery(tag, (Activity) context, PeerContract.CONTENT_URI, loaderID, creator, listener);
    }

    public void getPeerAsync(long id, final IContinue<Peer> callback) {
        // TODO need to check that peer is not null (not in database)
        SimpleQueryBuilder.executeQuery((Activity) context,PeerContract.CONTENT_URI(id),creator,new SimpleQueryBuilder.ISimpleQueryListener<Peer>(){
            public void handleResults(List<Peer> peers) {
                if(callback!=null || peers.size()>0) {
                    callback.kontinue(peers.get(0));
                } else {
                    throw new IllegalStateException("not found");
                }
            }
        });
    }

    public void persistAsync(Peer peer, final IContinue<Long> callback) {
        // TODO need to ensure the peer is not already in the database
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        contentResolver.insertAsync(PeerContract.CONTENT_URI, values, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri value) {
                //peer.setId(PeerContract.getId(value));
                getSyncResolver().notifyChange(value, null);
                if (callback != null) {
                    callback.kontinue(PeerContract.getId(value));
                } else {
                    throw new IllegalStateException("not found");
                }
            }
        });
    }

    public void persistAsync(final Peer peer) {
        // TODO
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        contentResolver.insertAsync(PeerContract.CONTENT_URI, values, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri value) {
                //message.setId(MessageContract.getId(value));
                getSyncResolver().notifyChange(value, null);
            }
        });
    }

}
