package edu.stevens.cs522.bookstore.async;

import android.database.Cursor;

/**
 * Created by dduggan.
 */

public interface IEntityCreator<T> {

    public T create(Cursor cursor);

}

