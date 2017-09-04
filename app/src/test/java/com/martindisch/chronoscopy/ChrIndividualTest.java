package com.martindisch.chronoscopy;

import com.martindisch.chronoscopy.logic.ChrIndividual;

import org.junit.Test;

import java.util.Calendar;
import java.util.Random;

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
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            int regret = r.nextInt(9) + 1;
            int skill = r.nextInt(9) + 1;
            int fun = r.nextInt(9) + 1;
            int responsibility = r.nextInt(9) + 1;
            int age = r.nextInt(100) + 1;
            double leisure = r.nextInt(1191) / 10;
            ChrIndividual individual = new ChrIndividual(
                    regret, skill, fun, responsibility, age, leisure);
            assertEquals(Math.log10((responsibility * age) / leisure) + 3,
                    individual.getTimeValue(), 0.000001);
        }
    }
}
