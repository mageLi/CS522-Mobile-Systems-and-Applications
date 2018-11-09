package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;


public class AddBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";
	private EditText title, author, isbn;
	static int bookId = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);
		bindViews();
	}

	private void bindViews(){
		title = (EditText) findViewById(R.id.search_title);
		author = (EditText)findViewById(R.id.search_author);
		isbn =(EditText) findViewById(R.id.search_isbn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO provide ADD and CANCEL options
		menu.add(0,100,1,"ADD");
		menu.add(0,200,2,"CANCEL");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		switch (item.getItemId()){
			// ADD: return the book details to the BookStore activity
			case 100:
				Log.i("TAG", "case 100");
				Intent intent = new Intent();
				Book book = addBook();
			//	Log.i("TAG", "addbook.ibsn="+book.getIsbn());
				intent.putExtra(BOOK_RESULT_KEY,book);
				Toast.makeText(this, "ADD SUCCESS", Toast.LENGTH_SHORT).show();
				setResult(Activity.RESULT_OK,intent);
				finish();
				return true;
			// CANCEL: cancel the request
			case 200:
				Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
				finish();
				return true;
		}
		return false;
	}
	
	public Book addBook(){
		// TODO Just build a Book object with the search criteria and return that.
		String [] str = author.getText().toString().split(",");
		Author[] authorsList = new Author[str.length];
		for(int i= 0; i<str.length;i++) {
			authorsList[i] = new Author(str[i]);
		}
		Book book= new Book(bookId++,title.getText().toString(),authorsList,isbn.getText().toString(),"$100");
		return book;
	}

}