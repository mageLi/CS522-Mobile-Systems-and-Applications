package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";
    public TextView name;
    public TextView timestamp;
    public TextView address;
    public TextView port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO init the UI
        bindView();
        name.setText(peer.name);
        timestamp.setText(peer.timestamp.toString());
        address.setText(peer.address.toString());
        port.setText(peer.port + "");

    }
    private void bindView(){
        name = (TextView) findViewById(R.id.view_user_name);
        timestamp = (TextView) findViewById(R.id.view_timestamp);
        address = (TextView) findViewById(R.id.view_address);
        port = (TextView) findViewById(R.id.view_port);
    }

}
