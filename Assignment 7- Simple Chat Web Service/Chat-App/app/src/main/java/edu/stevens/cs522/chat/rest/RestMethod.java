package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.util.Config;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.StringUtils;

/**
 * Created by dduggan.
 */

public class RestMethod {

    public static final String TAG = "***IMPORTENT***";

    private static final boolean DEBUG = true;

    /*
     * Web service methods
     */
    private final static String GET_METHOD = "GET";

    private final static String POST_METHOD = "POST";

    private final static String PUT_METHOD = "PUT";

    private final static String DELETE_METHOD = "DELETE";

    /*
     * HTTP Request headers
     */
    public final static String CONTENT_TYPE = "CONTENT-TYPE";

    public final static String ACCEPT = "ACCEPT";

    public final static String USER_AGENT = "USER-AGENT";

    public final static String CONNECTION = "CONNECTION";

    /*
     * MIME types
     */
    public final static String JSON_TYPE = "application/json";

    /*
     * Timeouts
     */
    public final static int SERVICE_DURATION = 5000;

    public static String DEFAULT_URL = "http://155.246.135.25:8080/chat";


    /*
     * HTTP response
     */
    public static final int HTTP_RESPONSE_CODE_UNKNOWN = 400;

    public static final int HTTP_RESPONSE_STRING_UNKNOWN = R.string.http_response_unknown;

    public static final int HTTP_RESPONSE_CODE_UNAVAILABLE = 503;

    public static final int HTTP_RESPONSE_STRING_UNAVAILABLE = R.string.http_response_unavailable;


    private Context context;

    private HttpURLConnection connection;

    private InputStream downloadConnection;

    private OutputStream uploadConnection;

    private final String baseUri;

    private URL registerUri;

    private URL postMessageUri;

    private URL syncMessagesUri;

    public RestMethod(Context context) {
        this.context = context;
        this.baseUri = "http://155.246.135.25:8080/chat";
    }

    public Response perform(RegisterRequest request) {
        URL url = registerURL(request.chatname);
        if (url == null) {
            throw new IllegalStateException("Missing URL for registering!");
        }
        try {
            getWakeLock(context, SERVICE_DURATION);
            initConnection(url, request);
            return executeOnewayRequest(request);
        } catch (SocketTimeoutException e) {
            return isUnavailable(request);
        } catch (IOException e) {
            Log.i(TAG, "Registration: Web service error.", e);
            return new ErrorResponse(0, ErrorResponse.Status.SYSTEM_ERROR, e.getMessage());
        } finally {
            closeConnection();
            releaseWakeLock();
        }
    }

    public Response perform(PostMessageRequest request) {
        URL url = postMessageURL();
        if (url == null) {
            throw new IllegalStateException("Missing URL for posting messages!");
        }
        try {
            Log.i(TAG, "perform");
            getWakeLock(context, SERVICE_DURATION);
            initConnection(url, request);
            return executeOnewayRequest(request);
        } catch (SocketTimeoutException e) {
            return isUnavailable(request);
        } catch (IOException e) {
            Log.e(TAG, "Post message: Web service error.", e);
            return new ErrorResponse(0, ErrorResponse.Status.SYSTEM_ERROR, e.getMessage());
        } finally {
            closeConnection();
            releaseWakeLock();
        }
    }

    private URL fromURI(String uri) {
        try {
            return new URL(uri);
        } catch (MalformedURLException e) {
            IllegalStateException ex = new IllegalStateException("Illegal state while attempting to register");
            ex.initCause(e);
            throw ex;
        }
    }

    private URL registerURL(String chatName) {

        if (registerUri == null) {
            registerUri = fromURI(baseUri + "?chat-name=" + chatName);
        }
        return registerUri;
    }



    private URL postMessageURL() {

        if (postMessageUri == null) {
            long senderId = Settings.getSenderId(context);
            String uri = String.format("%s/%d/messages", baseUri, senderId);
            postMessageUri = fromURI(uri);
        }
        return postMessageUri;
    }

    private PowerManager.WakeLock wakeLock;

    private void getWakeLock(Context context, int timeout) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        if (wakeLock != null) {
            wakeLock.acquire(timeout);
        }
    }

    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
    /**
     * Build and return a user-agent string that can identify this application to remote servers. Contains the package
     * name and version code.
     */
    private static String buildUserAgent(Context context) {
        String versionName = "unknown";
        int versionCode = 0;

        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return context.getPackageName() + "/" + versionName + " (" + versionCode + ") (gzip)";
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /*
     * Check that we are at least on a local network.
     */
    private void checkOnline() throws SocketTimeoutException {
        if (!isOnline(context)) {
            throw new SocketTimeoutException();
        }
    }

    /*
     * Initialize some generic HTTP headers for the request.
     */
    private void initConnection(URL url, Request request) throws IOException {
		/*
		 * Are we connected to a network?  Not the same as checking if the server is accessible....
		 */
        checkOnline();

        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(USER_AGENT, buildUserAgent(context));
        connection.setUseCaches(false);

        connection.setRequestProperty(CONNECTION,"Close");

		/*
		 * App-specific headers
		 */
        Map<String,String> headers = request.getRequestHeaders();
        for (Map.Entry<String,String> header : headers.entrySet()) {
            if (header.getValue() != null) {
                connection.addRequestProperty(header.getKey(), header.getValue());
            } else {
                Log.w(TAG, "Ignoring empty header value for "+header.getKey());
            }
        }

    }

    private Response executeRequest(Request request) throws SocketTimeoutException, IOException {
        return executeRequest(POST_METHOD, request);
    }

    private Response executeRequest(String method, Request request) throws SocketTimeoutException, IOException {

        connection.setDoInput(true);
        // connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty(CONTENT_TYPE, JSON_TYPE);
        connection.setRequestProperty(ACCEPT, JSON_TYPE);

        connect(request);
        throwErrors(connection);

        downloadConnection = connection.getInputStream();
        JsonReader rd = new JsonReader(new BufferedReader(new InputStreamReader(downloadConnection)));
        Response response = request.getResponse(connection, rd);
        //rd.close(); //TODO close connection?
        return response;
    }

    private Response executeOnewayRequest(Request request) throws SocketTimeoutException, IOException {
        return executeOnewayRequest(POST_METHOD, request);
    }

    private Response executeOnewayRequest(String method, Request request) throws SocketTimeoutException, IOException {
		/*
		 * We do not expect a response entity, but there will still be response headers.
		 */
        connection.setDoInput(false);
        // connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty(CONTENT_TYPE, JSON_TYPE);

        connect(request);
        throwErrors(connection);

        Response response = request.getResponse(connection);
        return response;
    }

    private void connect(Request request) throws IOException {
        String requestEntity = request.getRequestEntity();
        if (requestEntity != null) {
            byte[] outputEntity = StringUtils.toBytes(requestEntity);  // Use StringUtils.CHARSET encoding (UTF-8)
            connection.setFixedLengthStreamingMode(outputEntity.length);
            uploadConnection = connection.getOutputStream();
            OutputStream out = new BufferedOutputStream(uploadConnection);
            out.write(outputEntity);
            out.flush();
            // out.close();
        } else {
            connection.connect();
        }
    }

    /**
     * For SYNC: callback from request processor for streaming upload to server
     */
    public interface StreamingOutput {
        public void write(OutputStream os) throws IOException;
    }

    /**
     * For SYNC: This is the response returned from a streaming download.  The request processor
     * will read the streaming input (from the server) on the download stream, with HTTP response
     * headers in the response field.  It is the responsibility of the request processor to
     * disconnect from the server when done.
     */
    public class StreamingResponse {
        private Response response;
        public StreamingResponse(Response response) {
            this.response = response;
        }
        public InputStream getInputStream() {
            return downloadConnection;
        }
        public Response getResponse() {
            return response;
        }
        public void disconnect() {
            closeConnection();
            releaseWakeLock();
        }
    }

    private StreamingResponse executeStreamingRequest(String method, Request request, StreamingOutput out, PowerManager.WakeLock wakeLock) throws SocketTimeoutException, IOException {
		/*
		 * We will stream output to the server, so the connection will be returned to the request processor
		 * to write any output entity, stream the output, and read any input response.
		 *
		 * "out" is a callback to stream
		 */
        if (DEBUG) Log.d(TAG, "....Executing a streaming request.....");
        connection.setChunkedStreamingMode(0);
        connection.setRequestMethod(method);

        uploadConnection = connection.getOutputStream();

        if (DEBUG) Log.d(TAG, "....No connection errors, writing output entity.....");
        out.write(uploadConnection);
        uploadConnection.flush();

        if (DEBUG) Log.d(TAG, "....Checking the response code.....");
        throwErrors(connection);

        if (DEBUG) Log.d(TAG, "....Returning a streaming response.....");
        downloadConnection = connection.getInputStream();

        StreamingResponse response = new StreamingResponse(request.getResponse(connection));
        return response;
    }

    private StreamingResponse executeStreamingRequest(Request request, StreamingOutput out, PowerManager.WakeLock wakeLock) throws SocketTimeoutException, IOException {
        return executeStreamingRequest(POST_METHOD, request, out, wakeLock);
    }

    private StreamingResponse executeStreamingRequest(Request request, PowerManager.WakeLock wakeLock) throws SocketTimeoutException, IOException {
		/*
		 * We will stream output to the server, so the connection will be returned to the request processor
		 * to write any output entity, stream the output, and read any input response.
		 */
        if (DEBUG) Log.d(TAG, "....Executing a streaming request.....");
        connection.setChunkedStreamingMode(0);
        connection.setRequestMethod(GET_METHOD);

        connection.connect();

        if (DEBUG) Log.d(TAG, "....Checking the response code.....");
        throwErrors(connection);

        if (DEBUG) Log.d(TAG, "....Returning a streaming response.....");
        downloadConnection = connection.getInputStream();

        StreamingResponse response = new StreamingResponse(request.getResponse(connection));
        return response;
    }


    private String getErrorResponseMessage(HttpURLConnection connection) throws IOException {
        // http://docs.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
        final int status = connection.getResponseCode();
        StringBuilder sb = new StringBuilder();
        String exceptionMessage = "Error response " + status + " " + connection.getResponseMessage() + " for "
                + connection.getURL();
        sb.append(exceptionMessage);
        try {
            InputStream es = ((HttpURLConnection)connection).getErrorStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(es, StringUtils.CHARSET));
            String line = rd.readLine();
            while (line != null) {
                Log.w(TAG, "Error response entity: "+line);
                sb.append(line);
                sb.append('\n');
                line = rd.readLine();
            }
            rd.close();
        } catch(IOException ex) {
            Log.e(TAG, "IO Exception while processing error response.", ex);
        }
        return sb.toString();
    }

    private void throwErrors(HttpURLConnection connection) throws IOException {
        final int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IOException(getErrorResponseMessage(connection));
        }
    }

    private void closeConnection() {
        try {
            if (uploadConnection != null) {
                uploadConnection.close();
            }
            if (downloadConnection != null) {
                downloadConnection.close();
            }
            // This closes the socket, which may not be good for persistent HTTP connections.
            // Possible bug in JB suggests not pooling connections: http://stackoverflow.com/q/20367647
            //connection.disconnect();
            if(connection != null) {
                connection.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG, "IO exception while closing HTTP connection.", e);
        } finally {
        }
    }

    private ErrorResponse isUnavailable(Request request) {
        return new ErrorResponse(
                HTTP_RESPONSE_CODE_UNAVAILABLE,
                ErrorResponse.Status.NETWORK_UNAVAILABLE,
                context.getString(R.string.http_response_unavailable),
                context.getString(HTTP_RESPONSE_STRING_UNAVAILABLE));
    }

}
