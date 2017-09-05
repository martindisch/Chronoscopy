package com.martindisch.chronoscopy;

import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrIndividual;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class ChrIndividualTest {

    @Test
    public void ageTest() throws Exception {
        Calendar now = Calendar.getInstance();
        ChrIndividual individual = new ChrIndividual(9, 8, 5, 8,
                        (now.get(Calendar.YEAR) - 10) + "-" +
                        (now.get(Calendar.MONTH) + 1) + "-" +
                        (now.get(Calendar.DAY_OF_MONTH) - 1), 43.5);
        assertEquals(10, individual.getAge());
        individual = new ChrIndividual(9, 8, 5, 8,
                        (now.get(Calendar.YEAR) - 10) + "-" +
                        (now.get(Calendar.MONTH) + 1) + "-" +
                        (now.get(Calendar.DAY_OF_MONTH) + 1), 43.5);
        assertEquals(9, individual.getAge());
    }

    @Test
    public void valueTest() throws Exception {
        int regret = 5;
        int age = 62;
        double leisure = 98.8;
        ChrIndividual individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.4966047492466448, individual.getTimeValue(), 0.000001);
        regret = 2;
        age = 39;
        leisure = 28.3;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.44030816716619, individual.getTimeValue(), 0.000001);
        regret = 3;
        age = 56;
        leisure = 7.1;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(4.374050933006788, individual.getTimeValue(), 0.000001);
        regret = 5;
        age = 44;
        leisure = 14.7;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(4.17510534607403, individual.getTimeValue(), 0.000001);
        regret = 8;
        age = 35;
        leisure = 118.0;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.375276024036094, individual.getTimeValue(), 0.000001);
        regret = 8;
        age = 32;
        leisure = 24.3;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(4.022633691713537, individual.getTimeValue(), 0.000001);
        regret = 1;
        age = 56;
        leisure = 10.2;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.7395878552442827, individual.getTimeValue(), 0.000001);
        regret = 7;
        age = 9;
        leisure = 56.7;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.0457574905606752, individual.getTimeValue(), 0.000001);
        regret = 7;
        age = 23;
        leisure = 104.9;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.186050387838292, individual.getTimeValue(), 0.000001);
        regret = 7;
        age = 84;
        leisure = 11.4;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(4.712472474739666, individual.getTimeValue(), 0.000001);
        regret = 1;
        age = 1;
        leisure = 1;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3, individual.getTimeValue(), 0.000001);
        regret = 10;
        age = 100;
        leisure = 119;
        individual = new ChrIndividual(
                9, 8, 5, regret, age, leisure);
        assertEquals(3.9244530386074694, individual.getTimeValue(), 0.000001);
    }

    @Test
    public void scorePerHourTest() throws Exception {
        ChrIndividual individual = new ChrIndividual(9, 8, 5, 8, 22, 43.5);
        ChrActivity activity = new ChrActivity("Gaming", 2, 1, 4);
        assertEquals(0.5, individual.getScorePerHour(activity), 0.000001);
        activity = new ChrActivity("Learning", 4, 5, 2);
        assertEquals(23.3, individual.getScorePerHour(activity), 0.000001);
        activity = new ChrActivity("Being sick", 1, 1, 1);
        assertEquals(0, individual.getScorePerHour(activity), 0.000001);
        activity = new ChrActivity("Something impossible", 5, 5, 5);
        assertEquals(67.8, individual.getScorePerHour(activity), 0.000001);
    }

    @Test
    public void scoreTest() throws Exception {
        ChrIndividual individual = new ChrIndividual(9, 8, 5, 8, 22, 43.5);
        ChrActivity activity = new ChrActivity("Gaming", 2, 1, 4);
        assertEquals(1.8, individual.getScore(activity, 3.582), 0.000001);
        activity = new ChrActivity("Learning", 4, 5, 2);
        assertEquals(2.8, individual.getScore(activity, 0.12), 0.000001);
        activity = new ChrActivity("Being sick", 1, 1, 1);
        assertEquals(0, individual.getScore(activity, 8), 0.000001);
        activity = new ChrActivity("Something impossible", 5, 5, 5);
        assertEquals(291.2, individual.getScore(activity, 4.29461239), 0.000001);
    }
}
