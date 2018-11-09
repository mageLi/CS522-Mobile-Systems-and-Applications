package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.stevens.cs522.chat.activities.RegisterActivity;
import edu.stevens.cs522.chat.settings.Settings;

/**
 * Created by dduggan.
 */

public class RegisterRequest extends Request {

    public static final String TAG = "chat.RegisterRequest";

    public String chatname;

    public RegisterRequest(String chatname, UUID clientID) {
        super(chatname, clientID);
        this.chatname = chatname;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String,String> headers = new HashMap<>();
        // TODO add headers
        headers.put(Request.CHAT_NAME_HEADER, this.chatName);
        headers.put(Request.CLIENT_ID_HEADER, this.clientID.toString());
        headers.put(Request.TIMESTAMP_HEADER, String.valueOf(this.timestamp.getTime()));
        headers.put(Request.LATITUDE_HEADER, String.valueOf(this.latitude));
        headers.put(Request.LONGITUDE_HEADER, String.valueOf(this.longitude));
        return headers;
    }

    @Override
    public String getRequestEntity() throws IOException {
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException{
        return new RegisterResponse(connection);
    }

    @Override
    public Response process(RequestProcessor processor) {
        return processor.perform(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(chatname);
    }

    public RegisterRequest(Parcel in) {
        super(in);
        this.chatname = in.readString();
    }

    public static Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {
        @Override
        public RegisterRequest createFromParcel(Parcel source) {
            return new RegisterRequest(source);
        }

        @Override
        public RegisterRequest[] newArray(int size) {
            return new RegisterRequest[size];
        }
    };

}
