package edu.stevens.cs522.chatserver.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.databases.MessagesDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;


public class ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener {

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private MessagesDbAdapter messagesDbAdapter;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        messagesDbAdapter = new MessagesDbAdapter(this);
        c = messagesDbAdapter.queryPeers();

        listView = (ListView) findViewById(R.id.peerList);
        adapter = new SimpleCursorAdapter(this, R.layout.peers, c, new String[]{"_name"},
                new int[]{R.id.peer});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        c.moveToFirst();
        while(position > 0) {
            c.moveToNext();
            position--;
        }
        Peer curr = new Peer(c);
        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_ID_KEY, curr);
        startActivity(intent);
    }
}
