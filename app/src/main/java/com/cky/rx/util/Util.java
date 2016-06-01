package com.cky.rx.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cky.rx.R;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

/**
 * Created by cuikangyuan on 16/5/22.
 */
public class Util {

    public static String getVerion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.version_not_found);
        }
    }




}
