package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.async.IContinue;
import edu.stevens.cs522.bookstore.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.managers.BookManager;
import edu.stevens.cs522.bookstore.managers.TypedCursor;
import edu.stevens.cs522.bookstore.util.BookAdapter;

public class MainActivity extends Activity implements OnItemClickListener, AbsListView.MultiChoiceModeListener, IQueryListener {
	
	// Use this when logging errors and warnings.
	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getCanonicalName();
	
	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;
	
	@SuppressWarnings("unused")
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

    private BookManager bookManager;

    private BookAdapter bookAdapter;

    ListView lv;

    ArrayList<Book> shoppingCart;

    Set<Long> selected;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        shoppingCart = new ArrayList<>();
        getContentResolver();
		// TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);
        // Use a custom cursor adapter to display an empty (null) cursor.
        bookAdapter = new BookAdapter(this, null);
        lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(bookAdapter);

        // TODO Set listeners for item selection and multi-choice CAB
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(this);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewBookActivity.class);
                Book book = new Book((Cursor) lv.getAdapter().getItem(position));
                intent.putExtra(ViewBookActivity.BOOK_KEY, book);
                startActivity(intent);
            }
        });
        registerForContextMenu(lv);
        // Initialize the book manager and query for all books
        bookManager = new BookManager(this);
        bookManager.getAllBooksAsync(this);
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
                Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
                startActivityForResult(checkoutIntent, CHECKOUT_REQUEST);
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
        Book book;
        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        switch (requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.
                // It is okay to do this on the main thread for BookStoreWithContentProvider
                if (resultCode == RESULT_OK) {
                    book = intent.getExtras().getParcelable(AddBookActivity.BOOK_RESULT_KEY);
                    bookManager.persistAsync(book);
                    bookManager.getAllBooksAsync(this);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.
                // It is okay to do this on the main thread for BookStoreWithContentProvider
                if (resultCode == RESULT_OK) {
                    bookManager.deleteBooksAsync(new IContinue<Integer>() {
                        @Override
                        public void kontinue(Integer value) {
                            Toast.makeText(getApplicationContext(), "checking out",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    //getContentResolver().delete(BookContract.CONTENT_URI, null, null);
                    break;
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Checkout Cancelled", Toast.LENGTH_SHORT).show();
                }
        }
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables).
        savedInstanceState.putParcelableArrayList("list", shoppingCart);
        super.onSaveInstanceState(savedInstanceState);
	}

    /*
     * TODO Query listener callbacks
     */

    @Override
    public void handleResults(TypedCursor results) {
        // TODO update the adapter
        bookAdapter.swapCursor(results.getCursor());
    }

    @Override
    public void closeResults() {
        // TODO update the adapter
        bookAdapter.swapCursor(null);
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

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // TODO inflate the menu for the CAB
        mode.getMenuInflater().inflate(R.menu.books_cab, menu);
        selected = new TreeSet<Long>();
        return true;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            selected.add(id);
        } else {
            selected.remove(id);
        }
        mode.setTitle("Select "+selected.size()+" books delete");
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete:
                // TODO delete the selected books
                delete_book();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

    public void delete_book() {
        Cursor c = null;
        BookManager manager = new BookManager(this);
        manager.deleteBooksAsync(selected);
    }
}