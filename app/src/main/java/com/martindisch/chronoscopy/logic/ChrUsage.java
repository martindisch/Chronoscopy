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
