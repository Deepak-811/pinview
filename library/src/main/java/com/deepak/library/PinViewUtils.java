package com.deepak.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.atomic.AtomicInteger;

public class PinViewUtils {

    private static final String LOG_TAG = PinViewUtils.class.getSimpleName();
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private static int generateCompatViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            /**
             * ID number larger than 0x00FFFFFF is reserved for static views defined in the /res xml files.
             * Android doesn't want you to use 0 as a view's id, and it needs to be flipped before 0x01000000 to avoid
             * the conflicts with static resource IDs
             */
            if (newValue > 0x00FFFFFF) {
                newValue = 1; // Roll over to 1, not 0.
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return generateCompatViewId();
        } else {
            return View.generateViewId();
        }
    }

    public static float convertPixelToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = ((Activity) context).getCurrentFocus();
            if (imm != null && currentFocus != null) {
                IBinder windowToken = currentFocus.getWindowToken();
                if (windowToken != null) {
                    imm.hideSoftInputFromWindow(windowToken, 0);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can't even hide keyboard " + e.getMessage());
        }
    }
}
