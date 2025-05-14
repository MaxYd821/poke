package com.example.poke.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

public class PermissionUtils {
    public static boolean checkGpsPermission(Activity activity) {
        return activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestGPSPermission(Activity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
    }
}
