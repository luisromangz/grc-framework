/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import com.greenriver.commons.collections.specialized.DateRangeSortedList;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Angel
 */
public class DateRangeSortedListTest {

    public DateRangeSortedListTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void testAdd() {
        DateRangeSortedList list = new DateRangeSortedList(DatePart.Date);
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();

        DateRange result = new DateRange();
        DateRange tmp = new DateRange();
        cal.add(GregorianCalendar.DAY_OF_MONTH, -20);
        result.setMin(cal.getTime());
        
        for (int i=0; i<10; i++) {
            result.setMax(cal.getTime());
            list.add(new DateRange(cal.getTime(), cal.getTime()));
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }

        assertTrue(list.size() == 1);
        assertTrue(result.equals(list.get(0), DatePart.Date));

        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 20);
        tmp.setMax(cal.getTime());
        result.setMax(cal.getTime());

        list.add(tmp);
        assertTrue(list.size() == 1);
        assertTrue(result.equals(list.get(0), DatePart.Date));

        cal.add(GregorianCalendar.DAY_OF_MONTH, -51);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 18);
        tmp.setMax(cal.getTime());

        list.add(tmp);

        assertTrue(list.size() == 2);
        assertTrue(tmp.equals(list.get(0), DatePart.Date));
        assertTrue(result.equals(list.get(1), DatePart.Date));

        cal.add(GregorianCalendar.DAY_OF_MONTH, 35);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 15);
        tmp.setMax(cal.getTime());

        list.add(tmp);
        assertTrue(list.size() == 3);
        assertTrue(tmp.equals(list.get(2), DatePart.Date));
        assertTrue(result.equals(list.get(1), DatePart.Date));

        cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 3);
        tmp.setMax(cal.getTime());

        result.setMin(list.get(2).getMin());
        result.setMax(tmp.getMax());

        list.add(tmp);
        assertTrue(list.size() == 3);
        assertTrue(result.equals(list.get(2), DatePart.Date));

        tmp.setMin(list.get(0).getMax());
        tmp.setMax(list.get(1).getMax());
        result.setMin(list.get(0).getMin());
        result.setMax(list.get(1).getMax());

        list.add(tmp);
        assertTrue(list.size() == 2);
        assertTrue(result.equals(list.get(0), DatePart.Date));
    }
}