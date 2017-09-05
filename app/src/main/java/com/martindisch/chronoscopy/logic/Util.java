package com.martindisch.chronoscopy.logic;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Util {

    /**
     * Build and return the individual from the settings in SharedPreferences.
     *
     * @param context the application context
     * @return {@link ChrIndividual} instance
     */
    public static ChrIndividual getIndividual(Context context) {
        // Get shared preferences
        SharedPreferences prefs = context.getSharedPreferences("settings", MODE_PRIVATE);
        // Build individual
        return new ChrIndividual(
                prefs.getInt("regret", 6),
                prefs.getInt("skill", 6),
                prefs.getInt("fun", 6),
                prefs.getInt("responsibility", 6),
                prefs.getString("date", "1970-01-01"),
                prefs.getFloat("leisure", 31));
    }
}
