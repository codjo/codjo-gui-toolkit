/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import java.sql.Timestamp;
import junit.framework.TestCase;
/**
 * Classe de test de {@link TimestampDateField}
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
public class TimestampDateFieldTest extends TestCase {
    public void test_setDate() throws Exception {
        TimestampDateField comp = new TimestampDateField();
        Timestamp date = Timestamp.valueOf("1970-01-06 12:05:00.25");
        comp.setDate(date);

        assertEquals("06/01/1970 à 12:05:00", comp.getText());

        assertEquals("1970-01-06 12:05:00.25", comp.getDate().toString());
    }


    public void test_setEnable() throws Exception {
        TimestampDateField comp = new TimestampDateField();
        assertTrue(!comp.isEditable());
        comp.setEditable(true);
        assertTrue(!comp.isEditable());
    }


    public void test_setText() throws Exception {
        TimestampDateField comp = new TimestampDateField();

        Timestamp date = Timestamp.valueOf("1970-01-06 12:05:00.25");
        comp.setDate(date);
        assertEquals("06/01/1970 à 12:05:00", comp.getText());

        comp.setText("toto");
        assertEquals("06/01/1970 à 12:05:00", comp.getText());
    }


    protected void setUp() {
    }


    protected void tearDown() {
    }
}
