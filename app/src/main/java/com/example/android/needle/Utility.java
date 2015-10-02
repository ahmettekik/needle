package com.example.android.needle;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jonfisk on 28/09/15.
 */
public class Utility {

    public static String[] getLocation(Context context) {
        String[] location = new String[2];
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        location[0] = prefs.getString(context.getString(R.string.pref_country_code_key),
                null
        );
        location[1] = prefs.getString(context.getString(R.string.pref_zip_code_key),
                null
        );

        return location;
    }
}
