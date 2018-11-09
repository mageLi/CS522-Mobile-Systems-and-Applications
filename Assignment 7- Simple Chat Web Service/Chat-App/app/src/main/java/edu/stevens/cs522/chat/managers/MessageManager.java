package edu.stevens.cs522.chat.managers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;


/**
 * Created by dduggan. Updated by X.Liu
 */

public class MessageManager extends Manager<ChatMessage> {

    public static final int LOADER_ID = 1;

    private static final IEntityCreator<ChatMessage> creator = new IEntityCreator<ChatMessage>() {
        @Override
        public ChatMessage create(Cursor cursor) {
            return new ChatMessage(cursor);
        }
    };

    private AsyncContentResolver contentResolver;

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllMessagesAsync(IQueryListener<ChatMessage> listener) {
        // TODO use QueryBuilder to complete this
        QueryBuilder.executeQuery(tag, (Activity) context, MessageContract.CONTENT_URI, loaderID, creator, listener);
    }

    public void persistAsync(final ChatMessage Message) {
        // TODO
        ContentValues val = new ContentValues();
        Message.writeToProvider(val);
        contentResolver.insertAsync(MessageContract.CONTENT_URI, val, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri value) {
                //message.setId(MessageContract.getId(value));
                getSyncResolver().notifyChange(value, null);
            }
        });
    }

}
