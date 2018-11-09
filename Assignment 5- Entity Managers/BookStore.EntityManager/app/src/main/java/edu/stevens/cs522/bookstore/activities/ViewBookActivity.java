package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.R;


public class ViewBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_KEY = "book";

	private ArrayAdapter<String> authorsAdapter;
	private TextView view_title,view_isbn;
	private ListView view_author;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);

		// TODO get book as parcelable intent extra and populate the UI with book details.
		authorsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		bindView();
		Book book = getIntent().getParcelableExtra(BOOK_KEY);
		view_title.setText(book.getTitle());
		Author[] authorsList = Author.authorsList(book.getAuthors());
		for (Author author : authorsList) {
			authorsAdapter.add(author.name);
		}
		view_author.setAdapter(authorsAdapter);
		view_isbn.setText(book.getIsbn());
	}
	private void bindView(){
		view_title = (TextView) findViewById(R.id.view_title);
		view_author = (ListView) findViewById(R.id.view_authors);
		view_isbn = (TextView) findViewById(R.id.view_isbn);
	}

}