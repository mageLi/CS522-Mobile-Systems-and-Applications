/*********************************************************************

 Chat server: accept chat messages from clients.

 Sender chatName and GPS coordinates are encoded
 in the messages, and stripped off upon receipt.

 Copyright (c) 2017 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

public class RegisterActivity extends Activity implements OnClickListener, ResultReceiverWrapper.IReceive {

    final static public String TAG = RegisterActivity.class.getCanonicalName();

    /*
     * Widgets for dest address, message text, send button.
     */
    //private TextView clientIdText;

    private EditText userNameText;

    private Button registerButton;

    /*
     * Helper for Web service
     */
    private ChatHelper helper;

    /*
     * For receiving ack when registered.
     */
    private ResultReceiverWrapper registerResultReceiver;

    private void bindView() {
        userNameText = (EditText) findViewById(R.id.chat_name_text);

        registerButton = (Button) findViewById(R.id.register_button);
    }

    /*
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Initialize settings to default values.
         */
        if (Settings.isRegistered(this)) {
            finish();
            return;
        }

        setContentView(R.layout.register);

        // TODO initialize registerResultReceiver
        registerResultReceiver = new ResultReceiverWrapper(new Handler());

        // TODO instantiate helper for service
        helper = new ChatHelper(this, registerResultReceiver);

        // TODO get references to views

        bindView();

        registerButton.setOnClickListener(this);

    }

    public void onResume() {
        super.onResume();
        registerResultReceiver.setReceiver(this);
    }

    public void onPause() {
        super.onPause();
        registerResultReceiver.setReceiver(null);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Callback for the REGISTER button.
     */
    public void onClick(View v) {
        if (helper != null) {

            String userName = null;

            String message;

            // TODO get userName from UI, and use helper to register
            userName = userNameText.getText().toString();
            Settings.saveChatName(this, userName);

            // End todo
            helper.register(userName);

            Log.i(TAG, "Registered: " + userName);

            // finish();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                // TODO show a success toast message
                Toast.makeText(this, "Registered!", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                // TODO show a failure toast message
                Toast.makeText(this, "Registered Fail", Toast.LENGTH_LONG).show();
                break;
        }
    }

}