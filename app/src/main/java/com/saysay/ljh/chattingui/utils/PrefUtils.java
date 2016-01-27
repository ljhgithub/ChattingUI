package com.saysay.ljh.chattingui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by ljh on 2016/1/27.
 */
public class PrefUtils {

    private final static String KEYBOARD_HEIGHT = "preference_keyboard_height";

    private PrefUtils() {
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    public static void setKeyboardHeight(Context context, int height) {
        getPreferences(context).edit().putInt(KEYBOARD_HEIGHT, height).commit();
    }

    public static int getKeyboardHeight(Context context) {
        return getPreferences(context).getInt(KEYBOARD_HEIGHT, 0);
    }
}
