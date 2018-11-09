package edu.stevens.cs522.bookstoredatabase.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.contracts.BookContract;
import edu.stevens.cs522.bookstoredatabase.databases.CartDbAdapter;
import edu.stevens.cs522.bookstoredatabase.entities.Book;

public class MainActivity extends ListActivity {

	// Use this when logging errors and warnings.
	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getCanonicalName();

	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;

	@SuppressWarnings("unused")
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

	private ArrayList<Book> shoppingCart = new ArrayList<Book>();	//data source
	private ListView bookStoreListView;	//ListView
	private static final String BOOK_DETAILS = "Book_Details";


	Cursor cursor;
	private SimpleCursorAdapter simpleAdapter;
	// The database adapter
	private CartDbAdapter dba;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)  finished
		if (dba != null) {
			cursor = dba.fetchAllBooks();
		} else {
			try {
				dba = new CartDbAdapter(this);
				cursor = dba.fetchAllBooks();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// TODO Set the layout (use cart.xml layout) finished
		setContentView(R.layout.cart);
		shoppingCart = new ArrayList<Book>();
		// TODO open the database using the database adapter finished

		bookStoreListView = (ListView) findViewById(android.R.id.list);
		this.dba = new CartDbAdapter(this);
		simpleAdapter = getSimpleCursorAdapter(null);
		this.setListAdapter(simpleAdapter);
		try {
			Cursor c = this.dba.fetchAllBooks();
			if (c.moveToNext()) {
				this.simpleAdapter.changeCursor(c);
				bookStoreListView.setVisibility(View.GONE);
			}
			this.dba.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bookStoreListView.setAdapter(simpleAdapter);
		bookStoreListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		bookStoreListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				mode.setTitle("Select "+bookStoreListView.getCheckedItemCount() + " delete item");
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				if (item.getItemId() == R.id.delete_menu) {
					SparseBooleanArray checked = bookStoreListView.getCheckedItemPositions();
					for (int i = 0; i < checked.size(); i++) {
						Log.i("TAG", "i = "+checked.valueAt(i));
						if (checked.valueAt(i)!=false) {
							cursor.moveToPosition(i);
							Book book = new Book(cursor);
							Log.i("TAG", "delete= "+book.getTitle());
							dba.delete(book);
							cursor=dba.fetchAllBooks();
							simpleAdapter.changeCursor(cursor);
							simpleAdapter.notifyDataSetChanged();
						}
					}
					simpleAdapter.changeCursor(cursor);
					simpleAdapter.notifyDataSetChanged();
					dba.close();
					mode.finish();
					return true;
				}
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}
		});

		bookStoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
				Log.i("TAG", "position= "+position);
				cursor.moveToPosition(position);
				Book bookSelected = new Book(cursor);
				Log.i("TAG", "bookSelected "+bookSelected.toString());
				intent.putExtra(BOOK_DETAILS, bookSelected);
				startActivity(intent);
			}
		});


	}



        // TODO query the database using the database adapter, and manage the cursor on the main thread

        // TODO use SimpleCursorAdapter to display the cart contents.
		private SimpleCursorAdapter getSimpleCursorAdapter(Cursor cursor) {

			String[] from = new String[]{BookContract.TITLE,
					BookContract.AUTHORS};
			int[] to = new int[]{R.id.cart_row_title,
					R.id.cart_row_author};
			return new SimpleCursorAdapter(this, R.layout.cart_row, cursor, from, to);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO inflate a menu with ADD and CHECKOUT options finished
		getMenuInflater().inflate(R.menu.bookstore_menu, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            // TODO ADD provide the UI for adding a book finished
            case R.id.add:
                 Intent addIntent = new Intent(this, AddBookActivity.class);
                 startActivityForResult(addIntent, ADD_REQUEST);
                return true;

            // TODO CHECKOUT provide the UI for checking out finished
            case R.id.checkout:
				if(shoppingCart.size()<0) {

					return true;
				}else {
					addIntent = new Intent(this, CheckoutActivity.class);
					startActivityForResult(addIntent, CHECKOUT_REQUEST);
				}
                return true;

            default:
        }
        return false;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// TODO Handle results from the Search and Checkout activities. hold

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        switch(requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.
				if (resultCode == RESULT_OK) {
					Toast.makeText(this, "ADD success", Toast.LENGTH_LONG).show();
					try {
						Book b = intent.getExtras().getParcelable(AddBookActivity.BOOK_RESULT_KEY);
						this.dba.persist(b);
						this.simpleAdapter.changeCursor(this.dba.fetchAllBooks());
						this.dba.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
				}
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "checking out", Toast.LENGTH_LONG).show();
					try {
						this.dba.deleteAll();
						this.simpleAdapter.changeCursor(this.dba.fetchAllBooks());
						this.dba.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if(resultCode == RESULT_CANCELED){
					Toast.makeText(this,"CANCEL",Toast.LENGTH_SHORT).show();
				}
                break;
        }



	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables). finished
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("list", shoppingCart);
	}

}