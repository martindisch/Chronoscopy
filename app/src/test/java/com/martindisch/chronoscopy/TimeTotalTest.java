package com.martindisch.chronoscopy;

import com.martindisch.chronoscopy.logic.ChrUsage;
import com.martindisch.chronoscopy.logic.Util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TimeTotalTest {

    @Test
    public void emtpy() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        assertEquals("0:00", Util.getTimeTotal(usages));
    }

    @Test
    public void singleDigitMinutes() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "4"));
        assertEquals("0:04", Util.getTimeTotal(usages));
    }

    @Test
    public void doubleDigitMinutes() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "34"));
        assertEquals("0:34", Util.getTimeTotal(usages));
    }

    @Test
    public void singleDigitHours() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "4:00"));
        assertEquals("4:00", Util.getTimeTotal(usages));
    }

    @Test
    public void doubleDigitHours() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "14:00"));
        assertEquals("14:00", Util.getTimeTotal(usages));
    }

    @Test
    public void calculationNoOverflow() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "0:18"));
        usages.add(new ChrUsage(0, "2017-09-10", "0:34"));
        assertEquals("0:52", Util.getTimeTotal(usages));
    }

    @Test
    public void calculationCleanOverflow() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "0:52"));
        usages.add(new ChrUsage(0, "2017-09-10", "0:08"));
        assertEquals("1:00", Util.getTimeTotal(usages));
    }

    @Test
    public void calculationOverOverflow() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "0:52"));
        usages.add(new ChrUsage(0, "2017-09-10", "0:09"));
        assertEquals("1:01", Util.getTimeTotal(usages));
    }

    @Test
    public void calculationMultiple() throws Exception {
        ArrayList<ChrUsage> usages = new ArrayList<>();
        usages.add(new ChrUsage(0, "2017-09-10", "0:52"));
        usages.add(new ChrUsage(0, "2017-09-10", "0:08"));
        usages.add(new ChrUsage(0, "2017-09-10", "2:59"));
        usages.add(new ChrUsage(0, "2017-09-10", "2:05"));
        usages.add(new ChrUsage(0, "2017-09-10", "1:00"));
        usages.add(new ChrUsage(0, "2017-09-10", "23"));
        usages.add(new ChrUsage(0, "2017-09-10", "1"));
        usages.add(new ChrUsage(0, "2017-09-10", "0:08"));
        assertEquals("7:36", Util.getTimeTotal(usages));
    }
}
