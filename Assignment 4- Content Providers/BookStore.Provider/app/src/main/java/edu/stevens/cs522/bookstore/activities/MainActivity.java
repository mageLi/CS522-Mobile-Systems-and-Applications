package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.util.BookAdapter;

public class MainActivity extends Activity implements OnItemClickListener, AbsListView.MultiChoiceModeListener, LoaderManager.LoaderCallbacks {

	// Use this when logging errors and warnings.
	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getCanonicalName();

	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;

	@SuppressWarnings("unused")
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

    static final private int LOADER_ID = 1;

    BookAdapter bookAdapter;

    public static final String BOOK_RESULT_KEY = "book_result";

    private ArrayList<Book> shoppingCart = new ArrayList<Book>();

    ContentResolver contentResolver;

    TextView emptyTextView;

    Set<Integer> selected;

    ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        contentResolver = getContentResolver();

		// TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);
        // Use a custom cursor adapter to display an empty (null) cursor.
        bookAdapter = new BookAdapter(this, null);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(bookAdapter);

        // TODO set listeners for item selection and multi-choice CAB

        getLoaderManager().initLoader(LOADER_ID, null, this);
        emptyTextView = (TextView)findViewById(android.R.id.empty);
        lv.setEmptyView(emptyTextView);
        registerForContextMenu(lv);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewBookActivity.class);
                Book book = new Book((Cursor) lv.getAdapter().getItem(position));
                intent.putExtra(ViewBookActivity.BOOK_KEY, book);
                startActivity(intent);
            }
        });

        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selected.add(position);
                } else {
                    selected.remove(position);
                }
                mode.setTitle("Select "+lv.getCheckedItemCount() + " books to delete");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.books_cab, menu);
                selected = new TreeSet<>();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        try {
                            delete_book();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        registerForContextMenu(lv);

        // TODO use loader manager to initiate a query of the database

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO inflate a menu with ADD and CHECKOUT options
        getMenuInflater().inflate(R.menu.bookstore_menu, menu);

        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            // TODO ADD provide the UI for adding a book
            case R.id.add:
                Intent addIntent = new Intent(this, AddBookActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;

            // TODO CHECKOUT provide the UI for checking out
            case R.id.checkout:
                Intent checkOutIntent = new Intent(this, CheckoutActivity.class);
                startActivityForResult(checkOutIntent, CHECKOUT_REQUEST);
                return true;

            default:
        }
        return false;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// TODO Handle results from the Search and Checkout activities.

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        switch(requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.
                // It is okay to do this on the main thread for BookStoreWithContentProvider
                if (resultCode == RESULT_OK) {
                    Book book = intent.getExtras().getParcelable(BOOK_RESULT_KEY);
                    ContentValues values = new ContentValues();
                    book.writeToProvider(values);
                    this.getContentResolver().insert(BookContract.CONTENT_URI, values);
                } else if(resultCode == RESULT_CANCELED) {
                    Toast.makeText(this,"ADD CANCEL",Toast.LENGTH_SHORT).show();
                }
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.
                // It is okay to do this on the main thread for BookStoreWithContentProvider
                if (resultCode == RESULT_OK) {
                    getContentResolver().delete(BookContract.CONTENT_URI, null, null);
                    break;
                } else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"CHECKOUT CANCEL",Toast.LENGTH_SHORT).show();
                }
                break;
        }

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables).
        savedInstanceState.putParcelableArrayList("list", shoppingCart);
        super.onSaveInstanceState(savedInstanceState);
	}

    /*
     * Loader callbacks
     */

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		// TODO use a CursorLoader to initiate a query on the database
        CursorLoader cursorLoader;
        switch (id){
            case LOADER_ID:
                String[] projection = {"books."+BookContract.ID, BookContract.TITLE, BookContract.AUTHORS, BookContract.ISBN, BookContract.PRICE};
                cursorLoader = new CursorLoader(this, BookContract.CONTENT_URI, projection, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader loader, Object data) {
        // TODO populate the UI with the result of querying the provider ？？
        switch (loader.getId()){
            case LOADER_ID:
                this.bookAdapter.changeCursor((Cursor) data);
                break;
            default:
                throw new IllegalArgumentException("Unexpected onLoadFinished id: " + loader.getId());
        }
	}

	@Override
	public void onLoaderReset(Loader loader) {
        // TODO reset the UI when the cursor is empty
        switch (loader.getId()){
            case LOADER_ID:
                this.bookAdapter.changeCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Unexpected onLoaderReset id: " + loader.getId());
        }
	}


    /*
     * Selection of a book from the list view
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO query for this book's details, and send to ViewBookActivity
        // ok to do on main thread for BookStoreWithContentProvider
        Intent intent = new Intent(getApplicationContext(), ViewBookActivity.class);
        Book book = new Book((Cursor) lv.getAdapter().getItem(position));
        intent.putExtra(ViewBookActivity.BOOK_KEY, book);
        startActivity(intent);
    }


    /*
     * Handle multi-choice action mode for deletion of several books at once
     */
    private void delete_book() throws SQLException {
        // TODO delete the selected books
        Cursor c = bookAdapter.getCursor();;
        Iterator iterator = selected.iterator();
        while (iterator.hasNext()) {
            int position = (int) iterator.next();
            c.moveToFirst();
            while (position > 0) {
                c.moveToNext();
                position--;
            }
            String _id = bookAdapter.getCursor().getString(c.getColumnIndex(BookContract.ID));
            Uri uri = Uri.parse(BookContract.CONTENT_URI + "/" + _id);
            getContentResolver().delete(uri, null, null);
            bookAdapter.changeCursor(c);
        }
        Toast.makeText(this, "DELETE " + selected.size() + " BOOKS SUCCESS", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // TODO inflate the menu for the CAB
        return true;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
       return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

}