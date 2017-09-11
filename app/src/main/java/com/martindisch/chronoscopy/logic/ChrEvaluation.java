package com.martindisch.chronoscopy.logic;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Represents a daily evaluation of how satisfied the individual is with their day.
 */
public class ChrEvaluation extends SugarRecord {

    private int rating;
    @Unique
    private String date;

    /**
     * Default constructor, necessary for SugarRecord
     */
    public ChrEvaluation() {
    }

    /**
     * Instantiate a new evaluation.
     *
     * @param rating the rating of the day
     * @param date   the date in ISO 8601 (yyyy-MM-dd)
     */
    public ChrEvaluation(int rating, String date) {
        this.rating = rating;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format(
                "ChrEvaluation[id=%d, date=%s, rating=%d]", getId(), date, rating);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
