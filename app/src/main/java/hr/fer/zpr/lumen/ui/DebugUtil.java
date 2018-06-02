package hr.fer.zpr.lumen.ui;

import android.util.Log;

import hr.fer.zpr.lumen.BuildConfig;

public class DebugUtil {

    public static void LogDebug(Throwable e){
        if(BuildConfig.DEBUG){
            Log.d("error",Log.getStackTraceString(e));
        }
    }
}
