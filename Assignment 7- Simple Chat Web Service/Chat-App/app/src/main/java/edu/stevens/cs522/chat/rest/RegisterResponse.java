package edu.stevens.cs522.chat.rest;

import android.net.Uri;
import android.os.Parcel;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by dduggan.
 */

public class RegisterResponse extends Response {

    private final static String LOCATION = "Location";

    private long senderId;

    public RegisterResponse(HttpURLConnection connection) throws IOException {
        super(connection);
        // TODO String location = connection.getHeaderField(LOCATION);
        String location = connection.getHeaderField(LOCATION);
        if (location != null) {
            Uri uri = Uri.parse(location);
            senderId = Long.parseLong((uri.getLastPathSegment()));
        }
    }

    @Override
    public boolean isValid() { return true; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
    }

    public RegisterResponse(Parcel in) {
        super(in);
        senderId = in.readLong();
    }

    public static Creator<RegisterResponse> CREATOR = new Creator<RegisterResponse>() {
        @Override
        public RegisterResponse createFromParcel(Parcel source) {
            return new RegisterResponse(source);
        }

        @Override
        public RegisterResponse[] newArray(int size) {
            return new RegisterResponse[size];
        }
    };
}
