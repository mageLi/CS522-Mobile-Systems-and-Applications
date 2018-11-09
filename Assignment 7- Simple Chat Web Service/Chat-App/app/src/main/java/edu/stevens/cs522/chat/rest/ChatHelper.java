package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

import static edu.stevens.cs522.chat.rest.RestMethod.TAG;


/**
 * Created by dduggan.
 */

public class ChatHelper {

    public static final String TAG = "chat.ChatHelper";

    public static final String DEFAULT_CHAT_ROOM = "_default";

    private Context context;

    // Provided by server when we register
    private long senderId;

    public String chatName;

    // Installation senderId created when the app is installed (and provided with every request for sanity check)
    private UUID clientID;

    private ResultReceiverWrapper resultReceiverWrapper;

    public ChatHelper(Context context, ResultReceiverWrapper resultReceiverWrapper) {
        this.resultReceiverWrapper = resultReceiverWrapper;
        this.context = context;
        this.chatName = Settings.getChatName(context);
        this.clientID = Settings.getClientId(context);
    }

    public void register (String chatName, ResultReceiver resultReceiver) {
        if (Settings.isRegistered(context)) {
            Toast.makeText(context, "Error! Already Registered!", Toast.LENGTH_LONG).show();
            return;
        }
        if (chatName != null && !chatName.isEmpty()) {
            RegisterRequest request = new RegisterRequest(chatName, clientID);
            this.senderId = Settings.getSenderId(context);
            if (senderId > 0) {
                Toast.makeText(context, "Error! Already Registerd!", Toast.LENGTH_LONG).show();
                return;
            }
            Settings.saveChatName(context, chatName);
            Toast.makeText(context, "Save Name Successful!", Toast.LENGTH_LONG).show();
            addRequest(request, resultReceiverWrapper);
        }
    }

    public void register (String chatName) {
        if (Settings.isRegistered(context)) {
            Toast.makeText(context, "Error! Already Registered!", Toast.LENGTH_LONG).show();
            return;
        }
        if (chatName != null && !chatName.isEmpty()) {
            // TODO save the chat name and add a registration request to the request queue
            RegisterRequest request = new RegisterRequest(chatName, clientID);
            this.senderId = Settings.getSenderId(context);
            if (senderId > 0) {
                Toast.makeText(context, "Error! Already Registerd!", Toast.LENGTH_LONG).show();
                return;
            }
            Settings.saveChatName(context, chatName);
            Toast.makeText(context, "Save Name Successful!", Toast.LENGTH_LONG).show();
            addRequest(request, resultReceiverWrapper);
        }
    }

    public void postMessage (String chatRoom, String text, ResultReceiver receiver) {
        this.chatName = Settings.getChatName(context);
        if (text != null && !text.isEmpty()) {
            if (chatRoom == null || chatRoom.isEmpty()) {
                chatRoom = DEFAULT_CHAT_ROOM;
            }
            // TODO send the message (add to request queue)
            PostMessageRequest request = new PostMessageRequest(chatName, clientID, chatRoom, text);
            addRequest(request, resultReceiverWrapper);
        }
    }

    public void postMessage (String chatRoom, String text) {
        this.chatName = Settings.getChatName(context);
        if (text != null && !text.isEmpty()) {
            if (chatRoom == null || chatRoom.isEmpty()) {
                chatRoom = DEFAULT_CHAT_ROOM;
            }
            PostMessageRequest request = new PostMessageRequest(chatName, clientID, chatRoom, text);
            Settings.saveSenderId(context, request.senderId);
            addRequest(request, resultReceiverWrapper);
        }
    }

    private void addRequest(Request request, ResultReceiver receiver) {
        context.startService(createIntent(context, request, receiver));
    }

    private void addRequest(Request request) {
        addRequest(request, null);
    }

    /**
     * Use an intent to send the request to a background service. The request is included as a Parcelable extra in
     * the intent. The key for the intent extra is in the RequestService class.
     */
    public static Intent createIntent(Context context, Request request, ResultReceiver receiver) {
        Intent requestIntent = new Intent(context, RequestService.class);
        requestIntent.putExtra(RequestService.SERVICE_REQUEST_KEY, request);
        if (receiver != null) {
            Log.e(ChatHelper.class.getCanonicalName(),"receiver is not null");
            requestIntent.putExtra(RequestService.RESULT_RECEIVER_KEY, receiver);
        } else {
            Log.e(ChatHelper.class.getCanonicalName(),"receiver is null");
        }
        return requestIntent;
    }

    public static Intent createIntent(Context context, Request request) {
        return createIntent(context, request, null);
    }

}
