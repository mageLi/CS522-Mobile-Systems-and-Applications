package edu.stevens.cs522.bookstore.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.util.BooksAdapter;

public class MainActivity extends AppCompatActivity {

    // Use this when logging errors and warnings.
    private static final String TAG = MainActivity.class.getCanonicalName();

    // These are request codes for subactivity request calls
    static final private int ADD_REQUEST = 1;

    static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

    // There is a reason this must be an ArrayList instead of a List.
    private ArrayList<Book> shoppingCart;
    private ListView bookstoreList;
    BooksAdapter booksAdapter;
    public static final String BOOK_KEY = "book";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        if (savedInstanceState != null)
            shoppingCart = savedInstanceState.getParcelableArrayList("list");
        // TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);
        shoppingCart = new ArrayList<Book>();
        // TODO use an array adapter to display the cart contents.
        booksAdapter = new BooksAdapter(this,shoppingCart);
        bookstoreList = findViewById(android.R.id.list);
        bookstoreList.setAdapter(booksAdapter);
        bookstoreList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        bookstoreList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                actionMode.setTitle(bookstoreList.getCheckedItemCount() + " Selected Items");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                menu.add(0,100,1,"delete");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                //correct
                if(menuItem.getItemId()==100)
                {
                    SparseBooleanArray array = bookstoreList.getCheckedItemPositions();
                    ArrayList<Book> books = new ArrayList<Book>();
                    for (int x = 0; x < array.size(); x++) {
                        int key = array.keyAt(x);
                        boolean b = array.get(key);
                        if(b){
                            Book selectBook = (Book) bookstoreList.getItemAtPosition(array.keyAt(x));
                            books.add(selectBook);
                            booksAdapter.remove(selectBook);
                            booksAdapter.notifyDataSetChanged();
                        }
                    }
                    for(Book b: books){
                        shoppingCart.remove(b);
                    }
                    booksAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        bookstoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ViewBookActivity.class);
                Log.i("TAG", "click");
                intent.putExtra(BOOK_KEY,shoppingCart.get(i));
                startActivity(intent);
            }
        });

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
        //correct
        Intent addIntent;
        switch (item.getItemId()) {
            // TODO ADD provide the UI for adding a book
            case R.id.add:
                addIntent = new Intent(this, AddBookActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;

            // TODO CHECKOUT provide the UI for checking out
            case R.id.checkout:

                if(shoppingCart.size()<0) {

                    return true;
                }
                else{
                    //Log.i("TAG", "checkout ");
                    addIntent = new Intent(this,CheckoutActivity.class);
                    startActivityForResult(addIntent,CHECKOUT_REQUEST);
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
        // TODO Handle results from the Search and Checkout activities.

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        //correct
        switch (requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.

                if (resultCode == RESULT_OK) {
                    Log.i("TAG", "add request ");
                    Log.i("TAG", "request code="+requestCode);
                    Log.i("TAG", "result_ok= "+RESULT_OK);
                    Log.i("TAG", "result_cancel= "+RESULT_CANCELED);
                   // Log.i("TAG", "result_ok = "+requestCode);
                    Book result = intent.getExtras().getParcelable(AddBookActivity.BOOK_RESULT_KEY);
                    //Log.i("TAG", "result = "+result.toString());
                   // Log.i("TAG", "result.title = "+result.getTitle());
                    Toast.makeText(this, "ADD BOOK Success", Toast.LENGTH_SHORT).show();
                    shoppingCart.add(result);
                    booksAdapter.notifyDataSetChanged();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
                }
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.

                if(resultCode == RESULT_OK){
                    Log.i("TAG", "checkout request ");
                    Log.i("TAG", "request code="+resultCode);
                    Log.i("TAG", "result_ok= "+RESULT_OK);
                    Log.i("TAG", "result_cancel= "+RESULT_CANCELED);
                    Log.i("TAG", "result = "+RESULT_OK);
                    Toast.makeText(this,"CHECKOUT Success",Toast.LENGTH_SHORT).show();
                    shoppingCart.clear();
                    booksAdapter.notifyDataSetChanged();
                } else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"CANCEL",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO save the shopping cart contents (which should be a list of parcelables).

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("list", shoppingCart);
    }

}