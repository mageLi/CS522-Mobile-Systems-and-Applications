package edu.stevens.cs522.bookstore.util;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by dduggan.
 */

public class BookAdapter extends ResourceCursorAdapter {

    protected final static int ROW_LAYOUT = R.layout.cart_row;

    public BookAdapter(Context context, Cursor cursor) {
        super(context, ROW_LAYOUT, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO
        TextView title=(TextView) view.findViewById(R.id.cart_row_title);
        TextView author=(TextView) view.findViewById(R.id.cart_row_author);
        Book book=new Book(cursor);
        Author[] authorsList = Author.authorsList(book.getAuthors());
        title.setText(book.getTitle());
        author.setText(authorsList[0].getName());
    }
}