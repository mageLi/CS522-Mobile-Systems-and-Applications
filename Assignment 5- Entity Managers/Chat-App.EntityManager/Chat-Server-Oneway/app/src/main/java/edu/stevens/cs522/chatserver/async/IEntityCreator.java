package edu.stevens.cs522.chatserver.async;

import android.database.Cursor;

/**
 * Created by dduggan.
 */

public interface IEntityCreator<T> {

    public T create(Cursor cursor);

}

