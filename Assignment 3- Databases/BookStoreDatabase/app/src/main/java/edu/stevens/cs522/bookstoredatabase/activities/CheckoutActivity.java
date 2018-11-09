package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.stevens.cs522.bookstoredatabase.R;


public class CheckoutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO display ORDER and CANCEL options. finished
		menu.add(0, 100, 1, "ORDER");
		menu.add(0, 200, 1, "CANCEL");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO finished
		

		switch (item.getItemId()) {
			// ORDER: display a toast message of how many books have been ordered and return
			case 100:
				Toast.makeText(this, "ORDER SUCCESS", Toast.LENGTH_SHORT).show();
				setResult(Activity.RESULT_OK);
				finish();
				return true;
			// CANCEL: just return with REQUEST_CANCELED as the result code
			case 200:
				Toast.makeText(this,"ORDER CANCEL",Toast.LENGTH_SHORT).show();
				setResult(Activity.RESULT_CANCELED);
				finish();
				return true;
		}


		return false;
	}
	
}