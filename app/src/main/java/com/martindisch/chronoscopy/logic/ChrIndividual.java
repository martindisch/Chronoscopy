package com.martindisch.chronoscopy.logic;

import java.util.Calendar;

/**
 * Represents a person with their parameters.
 */
public class ChrIndividual {

    private int regret;
    private int skill;
    private int fun;
    private int responsibility;
    private int age;
    private double leisure;

    /**
     * Create a new instance of ChrIndividual.
     *
     * @param regret         how important it is not to regret wasted time in [1, 10]
     * @param skill          how important it is to gain skills in [1, 10]
     * @param fun            how important it is to have fun in [1, 10]
     * @param responsibility how responsible the individual is in [1, 10]
     * @param age            the individual's age in years
     * @param leisure        number of hours of free time per week
     */
    public ChrIndividual(int regret, int skill, int fun, int responsibility,
                         int age, double leisure) {
        this.regret = regret;
        this.skill = skill;
        this.fun = fun;
        this.responsibility = responsibility;
        this.age = age;
        this.leisure = leisure;
    }

    /**
     * Create a new instance of ChrIndividual.
     *
     * @param regret         how important it is not to regret wasted time in [1, 10]
     * @param skill          how important it is to gain skills in [1, 10]
     * @param fun            how important it is to have fun in [1, 10]
     * @param responsibility how responsible the individual is in [1, 10]
     * @param dateOfBirth    the individual's date of birth as yyyy-MM-dd
     * @param leisure        number of hours of free time per week
     */
    public ChrIndividual(int regret, int skill, int fun, int responsibility,
                         String dateOfBirth, double leisure) {
        this.regret = regret;
        this.skill = skill;
        this.fun = fun;
        this.responsibility = responsibility;
        this.leisure = leisure;

        // Create Calendar from date of birth String
        String[] dobComponents = dateOfBirth.split("-");
        Calendar dob = Calendar.getInstance();
        dob.clear();
        dob.set(Integer.parseInt(dobComponents[0]),
                Integer.parseInt(dobComponents[1]) - 1,
                Integer.parseInt(dobComponents[2]));
        // Get current calendar
        Calendar now = Calendar.getInstance();
        this.age = -1;
        // Add years while we're not past now
        while (!dob.after(now)) {
            dob.add(Calendar.YEAR, 1);
            this.age++;
        }
    }

    /**
     * Returns the value of time for this individual
     *
     * @return the value of time
     */
    public double getTimeValue() {
        return Math.log10((responsibility * age) / leisure) + 3;
    }

    public int getRegret() {
        return regret;
    }

    public int getSkill() {
        return skill;
    }

    public int getFun() {
        return fun;
    }

    public int getResponsibility() {
        return responsibility;
    }

    public int getAge() {
        return age;
    }

    public double getLeisure() {
        return leisure;
    }
}
