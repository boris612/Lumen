package hr.fer.zpr.lumen;

import android.util.Log;

/**
 * Created by boris on 1/29/18.
 * Služi kao Wrapper za Log.* metode kako bi se izbacile iz RELEASE verzije
 * Alternativno, može se koristiti ProGuard i minifyEnable
 * -assumenosideeffects class Android.util.Log {
 * public static *** d(...)
 * }
 */

public class LogUtil {
    public static void d(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void w(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
