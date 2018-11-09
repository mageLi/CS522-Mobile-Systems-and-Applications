package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.entities.Author;
import edu.stevens.cs522.bookstoredatabase.entities.Book;


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
		// TODO provide ADD and CANCEL options finished
		menu.add(0,100,1,"ADD");
		menu.add(0,200,2,"CANCEL");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO finished
		switch (item.getItemId()){
			//case R.id.ADD:
			// ADD: return the book details to the BookStore activity
			case 100:
				Log.i("TAG", "case 100");
				Intent intent = new Intent();
				Book book = addBook();
				//Log.i("TAG", "addbook.title="+book.getTitle());
			//	Log.i("TAG", "addbook.author="+book.getFirstAuthor());
				//Log.i("TAG", "addbook.ibsn="+book.getIsbn());
				intent.putExtra(BOOK_RESULT_KEY,book);
				setResult(Activity.RESULT_OK,intent);
				finish();
				return true;
			//case R.id.CANCEL:
			// CANCEL: cancel the request
			case 200:
				finish();
				return true;
		}
		// ADD: return the book details to the BookStore activity
		
		// CANCEL: cancel the request
		return false;
	}
	
	public Book addBook(){
		// TODO Just build a Book object with the search criteria and return that. finished
		String [] str = author.getText().toString().split(",");
		Author[] authorsList = new Author[str.length];
		for(int i= 0; i<str.length;i++) {
			String [] name = str[i].split("\\s");
			//	Log.i("TAG", "name.length= "+name.length);
			//	Log.i("TAG", "name= "+name[0]);
			switch (name.length) {
				case 1:
					authorsList[i] = new Author(name[0]);
					break;
				case 2:
					authorsList[i] = new Author(name[0],name[1]);
					break;
				case 3:
					authorsList[i] = new Author(name[0],name[1],name[2]);
					break;
			}
		}
		Book book= new Book(bookId++,title.getText().toString(),authorsList,isbn.getText().toString(),"$100");
		return book;
	}

}