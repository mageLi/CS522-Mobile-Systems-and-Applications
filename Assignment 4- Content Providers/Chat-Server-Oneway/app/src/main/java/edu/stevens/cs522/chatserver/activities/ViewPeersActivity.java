package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Peer;


public class ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object> {

    // TODO add loader callbacks

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */

    private ListView listView;

    private SimpleCursorAdapter peerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        // TODO initialize peerAdapter with empty cursor (null)
        listView = (ListView) findViewById(R.id.peerList);
        peerAdapter = new SimpleCursorAdapter(this, R.layout.peers, null, new String[]{"name"},
                new int[]{R.id.peer});
        listView.setAdapter(peerAdapter);
        listView.setOnItemClickListener(this);

        getLoaderManager().initLoader(1, null, this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        Cursor cursor = peerAdapter.getCursor();
        if (cursor.moveToPosition(position)) {
            Intent intent = new Intent(this, ViewPeerActivity.class);
            Peer peer = new Peer(cursor);
            intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
            startActivity(intent);
        } else {
            throw new IllegalStateException("Unable to move to position in cursor: "+position);
        }
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader;
        switch (i){
            case 1:
                String[] projection = {"peers" + "." + PeerContract._ID, PeerContract.NAME,
                        PeerContract.TIMESTAMP, PeerContract.ADDRESS, PeerContract.PORT};
                cursorLoader = new CursorLoader(this, PeerContract.CONTENT_URI, projection,
                        null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        switch (loader.getId()){
            case 1:
                this.peerAdapter.changeCursor((Cursor)o);
                break;
            default:
                throw new IllegalArgumentException("Unexpected loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()){
            case 1:
                this.peerAdapter.changeCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Unexpected loader id: " + loader.getId());
        }
    }
}
