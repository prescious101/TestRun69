package com.prototype.project.testrun69.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private Context mContext;

    public AppData() {
    }

    public AppData(Context mContext) {
        this.mContext = mContext;

    }

    public AppData(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }
}
