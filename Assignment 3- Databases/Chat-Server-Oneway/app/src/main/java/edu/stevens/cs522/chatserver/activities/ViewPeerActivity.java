package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.MessagesDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_ID_KEY = "peer_id";

    public TextView name;
    public TextView timestamp;
    public TextView address;
    public TextView port;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        name = (TextView) findViewById(R.id.view_user_name);
        timestamp = (TextView) findViewById(R.id.view_timestamp);
        address = (TextView) findViewById(R.id.view_address);
        port = (TextView) findViewById(R.id.view_port);

        //Peer curr = messagesDbAdapter.fetchPeer(Long.parseLong(getIntent().getStringExtra(PEER_ID_KEY)));
        Peer curr = getIntent().getParcelableExtra(PEER_ID_KEY);
        Log.i("current peer2:", curr.toString());

        name.setText(curr.name);
        timestamp.setText(curr.timestamp);
        address.setText(curr.address);
        port.setText(curr.port + "");

    }

}
