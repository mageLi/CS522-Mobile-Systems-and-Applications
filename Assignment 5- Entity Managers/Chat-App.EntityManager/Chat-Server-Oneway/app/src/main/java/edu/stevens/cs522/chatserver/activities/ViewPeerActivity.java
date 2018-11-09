package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.async.IContinue;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.managers.PeerManager;

import static android.R.attr.id;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";
    public static TextView peerName, peerAddress, peerPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO init the UI
        peerName = (TextView) findViewById(R.id.view_user_name);
        peerAddress = (TextView) findViewById(R.id.view_address);
        peerPort = (TextView) findViewById(R.id.view_port);
        peerName.setText(peer.getName());
        peerAddress.setText(peer.getAddress().toString());
        peerPort.setText(peer.getPort()+"");
    }

}
