package edu.stevens.cs522.chat.rest;

import android.os.Parcel;
import android.os.Parcelable;

import edu.stevens.cs522.chat.util.EnumUtils;

/**
 * Created by dduggan on 3/25/17.
 */

public class ErrorResponse extends Response implements Parcelable {

    public Status status;

    public enum Status {
        NETWORK_UNAVAILABLE,
        SERVER_ERROR,
        SYSTEM_ERROR,
        APPLICATION_ERROR
    }

    public boolean isValid() {
        return false;
    }

    public ErrorResponse(int responseCode, Status status, String message) {
        this(responseCode, status, message, "");
        this.status = status;
    }

    public ErrorResponse(int responseCode, Status status, String message, String httpMessage) {
        super(message, responseCode, httpMessage);
        this.status = status;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        EnumUtils.writeEnum(out, ResponseType.REGISTER);
        super.writeToParcel(out, flags);
        EnumUtils.writeEnum(out, status);
    }

    public ErrorResponse(Parcel in) {
        super(in);
        status = EnumUtils.readEnum(Status.class, in);
    }

    public static final Parcelable.Creator<ErrorResponse> CREATOR = new Parcelable.Creator<ErrorResponse>() {
        public ErrorResponse createFromParcel(Parcel in) {
            EnumUtils.readEnum(ResponseType.class, in);
            return new ErrorResponse(in);
        }

        public ErrorResponse[] newArray(int size) {
            return new ErrorResponse[size];
        }
    };

}

