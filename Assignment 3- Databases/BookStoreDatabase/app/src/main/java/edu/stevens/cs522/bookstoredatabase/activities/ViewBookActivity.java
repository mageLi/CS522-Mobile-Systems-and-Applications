package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.entities.Book;


public class ViewBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_KEY = "Book_Details";
	private TextView view_title, view_author, view_isbn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);

		// TODO get book as parcelable intent extra and populate the UI with book details.
		bindView();
		Book book = getIntent().getParcelableExtra(BOOK_KEY);
		Log.i("TAG", "settitle= "+book.toString());
		Log.i("TAG", "settitle= "+book.getTitle());
		Log.i("TAG", "author= "+book.getFirstAuthor());
		view_title.setText(book.getTitle());
		view_isbn.setText(book.getIsbn());
		view_author.setText(book.getFirstAuthor());

	}
	private void bindView(){
		view_title = (TextView) findViewById(R.id.view_title);
		view_author = (TextView) findViewById(R.id.view_author);
		view_isbn = (TextView) findViewById(R.id.view_isbn);
	}

}