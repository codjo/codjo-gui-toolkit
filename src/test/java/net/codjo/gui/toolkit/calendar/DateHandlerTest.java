/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.util.Date;
import junit.framework.TestCase;
/**
 * Classe de test.
 *
 * @version $Revision: 1.3 $
 */
public class DateHandlerTest extends TestCase {
    public void test_message() throws Exception {
        DateHandler root = newHandlerFalse();
        root.setSuccessor(newHandlerTrue());

        assertEquals("newHandlerTrue", root.message(new Date()));
    }


    public void test_isWeekEnd() throws Exception {
        DateHandler handler = newHandlerTrue();
        assertTrue("samedi", handler.isWeekEnd(java.sql.Date.valueOf("2004-03-06")));
        assertTrue("dimanche", handler.isWeekEnd(java.sql.Date.valueOf("2004-03-07")));
        assertFalse("vendredi", handler.isWeekEnd(java.sql.Date.valueOf("2004-03-05")));
    }


    public void test_isSameDate() throws Exception {
        DateHandler handler = newHandlerTrue();

        final java.sql.Date differentDate = java.sql.Date.valueOf("2004-03-05");
        final java.sql.Date dateNoTime = java.sql.Date.valueOf("2004-03-06");
        Date dateWithTime = java.sql.Timestamp.valueOf("2004-03-06 10:20:00.0");

        assertTrue("identique", handler.isSameDate(dateNoTime, dateNoTime));
        assertTrue("heure dif", handler.isSameDate(dateNoTime, dateWithTime));
        assertTrue("heure dif", handler.isSameDate(dateWithTime, dateNoTime));

        assertFalse("non identique", handler.isSameDate(dateNoTime, differentDate));
    }


    private DateHandler newHandlerTrue() {
        return new DateHandler() {
            protected boolean handle(Date input) {
                return true;
            }


            protected String handleMessage(Date input) {
                return "newHandlerTrue";
            }
        };
    }


    private DateHandler newHandlerFalse() {
        return new DateHandler() {
            protected boolean handle(Date input) {
                return false;
            }


            protected String handleMessage(Date input) {
                return "newHandlerFalse";
            }
        };
    }
}
