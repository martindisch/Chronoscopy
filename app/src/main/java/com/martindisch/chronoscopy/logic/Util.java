package com.martindisch.chronoscopy.logic;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

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

    /**
     * Return the string representation of the usages' cumulated time.
     *
     * @param usages List of usages
     * @return String representation of the usages' cumulated time
     */
    public static String getTimeTotal(List<ChrUsage> usages) {
        // Cumulate hours and minutes of usages
        int totalHours = 0, totalMinutes = 0;
        for (ChrUsage usage : usages) {
            String time = usage.getTime();
            // Make sure time is not 0, or any variation of 0, 0:00, etc.
            if (time == null || time.length() == 0 || time.matches("^(0*)(:0*)?$")) {
                continue;
            }
            String[] components = time.split(":");
            int hours, minutes;
            if (components.length == 1) {
                // We only have minutes
                hours = 0;
                minutes = Integer.parseInt(components[0]);
            } else {
                // We have hours and minutes
                hours = Integer.parseInt(components[0]);
                minutes = Integer.parseInt(components[1]);
            }
            totalHours += hours;
            totalMinutes += minutes;
        }
        // Turn extra minutes into hours
        int remainingMinutes = totalMinutes % 60;
        totalMinutes -= remainingMinutes;
        totalHours += totalMinutes / 60;
        return String.format("%d:%02d", totalHours, remainingMinutes);
    }
}
