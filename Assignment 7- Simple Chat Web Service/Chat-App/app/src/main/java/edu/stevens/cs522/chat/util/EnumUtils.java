package edu.stevens.cs522.chat.util;

import android.os.Parcel;

/**
 * Created by dduggan.
 */

public class EnumUtils {

    public static <E extends Enum<E>> int putEnum(E value) {
        if (value == null) {
            return -1;
        } else {
            return value.ordinal();
        }
    }

    public static <E extends Enum<E>> E getEnum(Class<E> _class, Integer value) {
        if (value == null || value < 0) {
            return null;
        } else {
            return _class.getEnumConstants()[value];
        }
    }

    public static <E extends Enum<E>> E getEnum(Class<E> _class, int value) {
        if (value < 0) {
            return null;
        } else {
            return _class.getEnumConstants()[value];
        }
    }

    public static <E extends Enum<E>> E readEnum(Class<E> _class, Parcel in) {
        return getEnum(_class, in.readInt());
    }

    public static <E extends Enum<E>> void writeEnum(Parcel out, E e) {
        out.writeInt(putEnum(e));
    }

}
