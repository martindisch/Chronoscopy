package com.martindisch.chronoscopy.logic;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Represents an activity with a name and values for regret, skill and fun.
 */
public class ChrActivity extends SugarRecord {

    @Unique
    private String name;
    private double regret;
    private double skill;
    private double fun;

    /**
     * Default constructor, necessary for SugarRecord.
     */
    public ChrActivity() {}

    /**
     * Instantiate a new activity.
     *
     * @param name The activity's unique name
     * @param regret The regret value in [1.0, 5.0]
     * @param skill The skill value in [1.0, 5.0]
     * @param fun The fun value in [1.0, 5.0]
     */
    public ChrActivity(String name, double regret, double skill, double fun) {
        this.name = name;
        this.regret = regret;
        this.skill = skill;
        this.fun = fun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRegret() {
        return regret;
    }

    public void setRegret(double regret) {
        this.regret = regret;
    }

    public double getSkill() {
        return skill;
    }

    public void setSkill(double skill) {
        this.skill = skill;
    }

    public double getFun() {
        return fun;
    }

    public void setFun(double fun) {
        this.fun = fun;
    }
}
