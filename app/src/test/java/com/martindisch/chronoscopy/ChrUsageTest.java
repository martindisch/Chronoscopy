package com.martindisch.chronoscopy;

import com.martindisch.chronoscopy.logic.ChrUsage;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChrUsageTest {

    @Test
    public void singleMinute() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "1");
        assertEquals("1 minute", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "0:01");
        assertEquals("1 minute", usage.getTimeWorded());
    }

    @Test
    public void someMinutes() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "2");
        assertEquals("2 minutes", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "0:02");
        assertEquals("2 minutes", usage.getTimeWorded());
    }

    @Test
    public void singleHour() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "1:00");
        assertEquals("1 hour", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "01:00");
        assertEquals("1 hour", usage.getTimeWorded());
    }

    @Test
    public void someHours() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "2:00");
        assertEquals("2 hours", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "02:00");
        assertEquals("2 hours", usage.getTimeWorded());
    }

    @Test
    public void singleHourSingleMinute() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "1:01");
        assertEquals("1 hour, 1 minute", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "01:01");
        assertEquals("1 hour, 1 minute", usage.getTimeWorded());
    }

    @Test
    public void singleHourSomeMinutes() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "1:02");
        assertEquals("1 hour, 2 minutes", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "01:02");
        assertEquals("1 hour, 2 minutes", usage.getTimeWorded());
    }

    @Test
    public void someHoursSingleMinute() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "2:01");
        assertEquals("2 hours, 1 minute", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "02:01");
        assertEquals("2 hours, 1 minute", usage.getTimeWorded());
    }

    @Test
    public void someHoursSomeMinutes() throws Exception {
        ChrUsage usage = new ChrUsage(0, "2017-09-03", "2:02");
        assertEquals("2 hours, 2 minutes", usage.getTimeWorded());
        usage = new ChrUsage(0, "2017-09-03", "02:02");
        assertEquals("2 hours, 2 minutes", usage.getTimeWorded());
    }

    @Test
    public void noTime() throws Exception {
        ChrUsage usage1 = new ChrUsage(0, "2017-09-03", null);
        ChrUsage usage2 = new ChrUsage(0, "2017-09-03", "");
        ChrUsage usage3 = new ChrUsage(0, "2017-09-03", "0");
        ChrUsage usage4 = new ChrUsage(0, "2017-09-03", "0:00");
        assertEquals("No time", usage1.getTimeWorded());
        assertEquals("No time", usage2.getTimeWorded());
        assertEquals("No time", usage3.getTimeWorded());
        assertEquals("No time", usage4.getTimeWorded());
    }
}
