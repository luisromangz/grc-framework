/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons;

import com.greenriver.commons.collections.SortedArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mangelp
 */
public class SortedListTest {

    private class TestElement {

        int x;

        public TestElement() {
        }

        public TestElement(int x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return x + "";
        }
    }

    private class ComparableTestElement implements Comparable<ComparableTestElement> {

        int x;

        public ComparableTestElement() {
        }

        public ComparableTestElement(int x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return x + "";
        }

        public int compareTo(ComparableTestElement o) {
            if (x % 2 == 0 && o.x % 2 == 0) {
                //Comparison sorts descending
                if (x > o.x) {
                    return -1;
                } else if (x < o.x) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (x % 2 != 0 && o.x % 2 != 0) {
                //Comparison sorts ascending
                if (x > o.x) {
                    return 1;
                } else if (x < o.x) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (x % 2 == 0 && o.x % 2 != 0) {
                //Put even numbers in the left
                return -1;
            } else {
                //Put odd numbers on the right
                return 1;
            }
        }
    }

    private class SortByXAscComparator implements Comparator<TestElement> {

        public int compare(TestElement o1, TestElement o2) {
            if (o1.x > o2.x) {
                return 1;
            } else if (o1.x < o2.x) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class SortByXDescComparator extends SortByXAscComparator {

        @Override
        public int compare(TestElement o1, TestElement o2) {
            int result = -1 * super.compare(o1, o2);
            return result;
        }
    }

    public SortedListTest() {
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

    private void printList(List list) {
        System.out.println("-- " + list.size() + " elems --");

        for (int i = 0; i < list.size(); i++) {
            System.out.println("[" + i + "]: " + list.get(i));
        }

        System.out.println("-- end --");
    }

    @Test
    public void sortComparatorAscendingTest() {
        System.out.println("sortComparatorAscendingTest");
        SortedArrayList<TestElement> list =
                new SortedArrayList<TestElement>(new SortByXAscComparator());

        list.add(new TestElement(1));
        list.add(new TestElement(3));
        list.add(new TestElement(2));
        list.add(new TestElement(9));
        list.add(new TestElement(-12));

        TestElement previous = list.get(0);
        TestElement current = null;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);

            assertTrue("Found element not sorted: " + previous.x + " before "
                    + current.x, previous.x < current.x);

            previous = current;
        }
    }

    @Test
    public void sortComparatorDescendingTest() {
        System.out.println("sortComparatorDescendingTest");
        SortedArrayList<TestElement> list =
                new SortedArrayList<TestElement>(new SortByXDescComparator());

        list.add(new TestElement(1));
        list.add(new TestElement(3));
        list.add(new TestElement(2));
        list.add(new TestElement(9));
        list.add(new TestElement(-12));

        TestElement previous = list.get(0);
        TestElement current = null;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);

            assertTrue("Found element not sorted: " + previous.x + " before "
                    + current.x, previous.x > current.x);

            previous = current;
        }
    }

    /**
     *
     */
    @Test(expected = IllegalStateException.class)
    public void throwsExceptionWhenNotComparableAndNoComparatorTest() {
        System.out.println("throwsExceptionWhenNotComparableAndNoComparatorTest");
        //If the element of the list is not comparable and a comparator is not
        //specified an exception is thrown
        SortedArrayList<Object> list = new SortedArrayList<Object>();
        list.add(new Object());
    }

    @Test
    public void sortComparableDescendingTest() {
        System.out.println("sortComparableDescendingTest");
        SortedArrayList<ComparableTestElement> list =
                new SortedArrayList<SortedListTest.ComparableTestElement>();
        list.add(new ComparableTestElement(1));
        list.add(new ComparableTestElement(3));
        list.add(new ComparableTestElement(2));
        list.add(new ComparableTestElement(9));
        list.add(new ComparableTestElement(-12));
        list.add(new ComparableTestElement(0));
        list.add(new ComparableTestElement(6));
        list.add(new ComparableTestElement(14));
        list.add(new ComparableTestElement(7));
        list.add(new ComparableTestElement(13));

        ComparableTestElement previous = list.get(0);
        ComparableTestElement current = null;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);

            if (previous.x % 2 == 0 && current.x % 2 == 0) {
                //Comparison sorts descending
                assertTrue("Found even element not sorted: " + previous.x + " before "
                        + current.x, previous.x > current.x);
            } else if (previous.x % 2 != 0 && current.x % 2 != 0) {
                //Comparison sorts ascending
                assertTrue("Found odd element not sorted: " + previous.x + " before "
                        + current.x, previous.x < current.x);
            } else if (!(previous.x % 2 == 0 && current.x % 2 != 0)) {
                fail("Even elements are not before odd elements");
            }

            previous = current;
        }
    }
}
