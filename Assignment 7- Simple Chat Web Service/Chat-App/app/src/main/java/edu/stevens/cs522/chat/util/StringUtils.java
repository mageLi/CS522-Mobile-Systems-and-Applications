package edu.stevens.cs522.chat.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by dduggan.
 */

public class StringUtils {

    public static final String CHARSET = "UTF-8";

    public static String toString(byte[] b) {
        return toString(b, b.length);
    }

    public static String toString(byte[] b, int len) {
        try {
            return new String(b, 0, len, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unsupported UTF-8 encoding!");
        }
    }

    public static byte[] toBytes(String s) {
        try {
            return s.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unsupported UTF-8 encoding!");
        }
    }

    public static byte[] toBytes(char[] s) {
        return toBytes(s, s.length);
    }

    public static byte[] toBytes(char[] s, int len) {
//		Charset charset = Charset.forName(CHARSET);
//		return charset.encode(CharBuffer.wrap(s, 0, len)).array();
        return toBytes(new String(s));
    }


}
