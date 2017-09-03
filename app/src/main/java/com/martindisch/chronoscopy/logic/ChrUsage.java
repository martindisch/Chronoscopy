package com.martindisch.chronoscopy.logic;

import com.orm.SugarRecord;

/**
 * Represents a usage of an activity, with a date and duration.
 */
public class ChrUsage extends SugarRecord {

    private long activityId;
    private String date;
    private String time;

    /**
     * Default constructor, necessary for SugarRecord.
     */
    public ChrUsage() {}

    /**
     * Instantiate a new activity usage.
     *
     * @param activityId Id of the activity
     * @param date The date in ISO 8601 (YYYY-MM-DD)
     * @param time The duration in minutes or h:mm/hh:mm
     */
    public ChrUsage(long activityId, String date, String time) {
        this.activityId = activityId;
        this.date = date;
        this.time = time;
    }

    /**
     * Returns the usage's time in language form, e.g. 7 hours, 23 minutes.
     *
     * @return The time in language form
     */
    public String getTimeWorded() {
        String time = getTime();
        // Make sure time is not 0, or any variation of 0, 0:00, etc.
        if (time == null || time.length() == 0 || time.matches("^(0*)(:0*)?$")) {
            return "No time";
        }
        StringBuilder builder = new StringBuilder();
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
        if (hours > 0) {
            builder.append(hours);
            builder.append(hours == 1 ? " hour" : " hours");
        }
        if (minutes > 0) {
            if (hours > 0) builder.append(", ");
            builder.append(minutes);
            builder.append(minutes == 1 ? " minute" : " minutes");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format(
                "ChrUsage[id=%d, activityId=%d, date=%s, time=%s]",
                getId(), activityId, date, time
        );
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivity(long activityId) {
        this.activityId = activityId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
