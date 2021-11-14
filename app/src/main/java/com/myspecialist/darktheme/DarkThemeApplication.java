package com.myspecialist.darktheme;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class DarkThemeApplication extends Application {

    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);
    }
}
