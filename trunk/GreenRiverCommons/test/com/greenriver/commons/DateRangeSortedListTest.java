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

    @Test
    public void testAdd() {
        System.out.println("testAdd");
        DateRangeSortedList list = new DateRangeSortedList(DatePart.DATE);
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
        assertTrue(result.equals(list.get(0), DatePart.DATE));

        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 20);
        tmp.setMax(cal.getTime());
        result.setMax(cal.getTime());

        list.add(tmp);
        assertTrue(list.size() == 1);
        assertTrue(result.equals(list.get(0), DatePart.DATE));

        cal.add(GregorianCalendar.DAY_OF_MONTH, -51);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 18);
        tmp.setMax(cal.getTime());

        list.add(tmp);

        assertTrue(list.size() == 2);
        assertTrue(tmp.equals(list.get(0), DatePart.DATE));
        assertTrue(result.equals(list.get(1), DatePart.DATE));

        cal.add(GregorianCalendar.DAY_OF_MONTH, 35);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 15);
        tmp.setMax(cal.getTime());

        list.add(tmp);
        assertTrue(list.size() == 3);
        assertTrue(tmp.equals(list.get(2), DatePart.DATE));
        assertTrue(result.equals(list.get(1), DatePart.DATE));

        cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
        tmp.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 3);
        tmp.setMax(cal.getTime());

        result.setMin(list.get(2).getMin());
        result.setMax(tmp.getMax());

        list.add(tmp);
        assertTrue(list.size() == 3);
        assertTrue(result.equals(list.get(2), DatePart.DATE));

        tmp.setMin(list.get(0).getMax());
        tmp.setMax(list.get(1).getMax());
        result.setMin(list.get(0).getMin());
        result.setMax(list.get(1).getMax());

        list.add(tmp);
        assertTrue(list.size() == 2);
        assertTrue(result.equals(list.get(0), DatePart.DATE));
    }

    @Test
    public void testIntersects() {
        System.out.println("testIntersects");
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        DateRange range1 = new DateRange();
        DateRangeSortedList list1 = new DateRangeSortedList(1);
        DateRange range2 = new DateRange();
        DateRangeSortedList list2 = new DateRangeSortedList(1);
        DateRangeSortedList resultList = null;
        DateRangeSortedList expectedResult = new DateRangeSortedList(1);

        cal.add(GregorianCalendar.DAY_OF_MONTH, 2);
        range1.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 6);
        range1.setMax(cal.getTime());
        list1.add(range1);

        cal.add(GregorianCalendar.DAY_OF_MONTH, -4);
        range2.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 8);
        range2.setMax(cal.getTime());
        list2.add(range2);

        expectedResult.add(new DateRange(
                    range1.getMax(),
                    range2.getMin()
                ));

        resultList = list1.getIntersection(list2);
        assertArrayEquals(expectedResult.toArray(), resultList.toArray());
    }

    @Test
    public void remove() {
        System.out.println("testRemove");
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        DateRange range1 = new DateRange();
        DateRange range2 = new DateRange();
        DateRange range3 = new DateRange();
        DateRange range0 = new DateRange();
        DateRange removalRange = new DateRange();
        DateRangeSortedList list = new DateRangeSortedList(4, DatePart.DATE);
        DateRangeSortedList expectedResult = new DateRangeSortedList(4, DatePart.DATE);

        range1.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 4);
        removalRange.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 2);
        range1.setMax(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 3);
        range2.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 2);
        removalRange.setMax(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 4);
        range2.setMax(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 10);
        range3.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 4);
        range3.setMax(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, -100);
        range0.setMin(cal.getTime());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 10);
        range0.setMax(cal.getTime());

        list.add(range0);
        list.add(range1);
        list.add(range2);
        list.add(range3);

        range1 = new DateRange(range1.getMin(), range1.getMax());
        cal.setTime(range1.getMax());
        cal.add(GregorianCalendar.DAY_OF_MONTH, -3);
        range1.setMax(cal.getTime());
        

        range2 = new DateRange(range2.getMin(), range2.getMax());
        cal.setTime(range2.getMin());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 3);
        range2.setMin(cal.getTime());

        expectedResult.add(range0);
        expectedResult.add(range1);
        expectedResult.add(range2);
        expectedResult.add(range3);

        list.remove(removalRange);
        
        assertArrayEquals(expectedResult.toArray(), list.toArray());

        expectedResult.remove(0);
        list.remove(range0);

        assertArrayEquals(expectedResult.toArray(), list.toArray());

        list.add(range0);

        cal.setTime(range0.getMin());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
        removalRange.setMin(cal.getTime());
        cal.setTime(range0.getMax());
        cal.add(GregorianCalendar.DAY_OF_MONTH, -1);
        removalRange.setMax(cal.getTime());

        expectedResult.add(new DateRange(range0.getMin()));
        expectedResult.add(new DateRange(range0.getMax()));

        list.remove(removalRange);

        assertArrayEquals(expectedResult.toArray(), list.toArray());
    }
}