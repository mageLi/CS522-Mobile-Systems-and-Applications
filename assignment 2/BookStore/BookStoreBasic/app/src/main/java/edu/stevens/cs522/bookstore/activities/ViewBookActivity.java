package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class ViewBookActivity extends AppCompatActivity {

    // Use this as the key to return the book details as a Parcelable extra in the result intent.
    public static final String BOOK_KEY = "book";
    private TextView view_title, view_author, view_isbn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        // TODO get book as parcelable intent extra and populate the UI with book details.
        view_title = findViewById(R.id.view_title);
        view_author = findViewById(R.id.view_author);
        view_isbn = findViewById(R.id.view_isbn);

        Book book = getIntent().getExtras().getParcelable(BOOK_KEY);
        Log.i("TAG", "settitle= "+book.getTitle());
        Log.i("TAG", "author= "+book.getFirstAuthor());
        view_title.setText(book.getTitle());
        view_isbn.setText(book.getIsbn());
        view_author.setText(book.getFirstAuthor());
    }

}