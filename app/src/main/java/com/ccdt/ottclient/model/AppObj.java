package com.ccdt.ottclient.model;

import android.graphics.drawable.Drawable;

/**
 * Created by iSun on 2015/11/16.
 */
public class AppObj {
    private String name;
    private String packageName;
    private Drawable icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppObj{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", icon=" + icon +
                '}';
    }
}
