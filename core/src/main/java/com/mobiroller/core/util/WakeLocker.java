package com.mobiroller.core.util;

import android.content.Context;
import android.os.PowerManager;

import com.mobiroller.core.BuildConfig;

public abstract class WakeLocker {

    private static PowerManager.WakeLock wakeLock;
 
    public static void acquire(Context context) {
        if (wakeLock != null) wakeLock.release();
 
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //TODO
        String appId =" BuildConfig.APPLICATION_ID";
        if(appId.length()>50)
        {
            appId = appId.substring(0,50);
        }
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock:"+ appId);
        wakeLock.acquire();
    }
 
    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}