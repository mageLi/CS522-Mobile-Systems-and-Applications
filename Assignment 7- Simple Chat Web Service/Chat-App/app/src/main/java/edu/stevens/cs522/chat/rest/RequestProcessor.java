package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Created by dduggan.
 */

public class RequestProcessor {

    public static final String TAG = "chat.RequestProcessor";

    private Context context;

    private RestMethod restMethod;

    public RequestProcessor(Context context) {
        this.context = context;
        this.restMethod = new RestMethod(context);
    }

    public Response process(Request request) {
        return request.process(this);
    }

    public Response perform(RegisterRequest request) {
        Response response = restMethod.perform(request);
        if (response instanceof RegisterResponse) {
            // TODO update the sender senderId in settings, updated peer record PK

        }
        return response;
    }

    public Response perform(PostMessageRequest request) {
        // TODO insert the message into the local database
        Response response = restMethod.perform(request);
        if (response instanceof PostMessageResponse) {
            // TODO update the message in the database with the sequence number
        }
        return response;
    }

}
