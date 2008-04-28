package org.ss12.wordprediction;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Unit test for {@link TopElements}.
 * 
 * @author Michael Parker
 */
public class TopElementsTest extends TestCase {
  public void testGetPivot() {
    assertEquals(0, TopElements.getPivotIndex(new Integer[] {
        5, 1, 8
    }, TopElements.naturalComparator, 0, 2));
    assertEquals(0, TopElements.getPivotIndex(new Integer[] {
        5, 8, 1
    }, TopElements.naturalComparator, 0, 2));
    assertEquals(1, TopElements.getPivotIndex(new Integer[] {
        1, 5, 8
    }, TopElements.naturalComparator, 0, 2));
    assertEquals(1, TopElements.getPivotIndex(new Integer[] {
        8, 5, 1
    }, TopElements.naturalComparator, 0, 2));
    assertEquals(2, TopElements.getPivotIndex(new Integer[] {
        1, 8, 5
    }, TopElements.naturalComparator, 0, 2));
    assertEquals(2, TopElements.getPivotIndex(new Integer[] {
        8, 1, 5
    }, TopElements.naturalComparator, 0, 2));
  }

  public void testEmptyArray() {
    Integer[] elements = new Integer[0];
    TopElements.selectSmallest(elements, 0);
    TopElements.selectSmallest(elements, getOutOfBoundsIndex(elements));
  }

  public void testSingletonArray() {
    Integer[] elements = {
      5
    };
    // Test sorting none.
    TopElements.selectSmallest(elements, 0);
    assertEquals(5, elements[0].intValue());
    // Test sorting all elements.
    TopElements.selectSmallest(elements, 1);
    assertEquals(5, elements[0].intValue());
    // Test sorting all elements with an invalid max index.
    TopElements.selectSmallest(elements, getOutOfBoundsIndex(elements));
    assertEquals(5, elements[0].intValue());
  }

  public void testThreeElementArray() {
    Integer[] unsorted = null;
    Integer[] unsortedPrototype = {
        5, 1, 8
    };
    Integer[] sorted = {
        1, 5, 8
    };

    // Test sorting one element.
    unsorted = unsortedPrototype.clone();
    TopElements.selectSmallest(unsorted, 1);
    assertSorted(unsorted, sorted, 1);
    // Test sorting all elements with an invalid max index.
    unsorted = unsortedPrototype.clone();
    TopElements.selectSmallest(unsorted, 3);
    assertSorted(unsorted, sorted, 3);
  }

  public void testFiveElementArray() {
    Integer[] unsorted = null;
    Integer[] unsortedPrototype = {
        5, 1, 3, 4, 2
    };
    Integer[] sorted = {
        1, 2, 3, 4, 5
    };

    // Test all partial sorts.
    for (int i = 1; i <= unsortedPrototype.length; ++i) {
      unsorted = unsortedPrototype.clone();
      TopElements.selectSmallest(unsorted, i);
      assertSorted(unsorted, sorted, i);
    }
  }

  public void testVeryLargeArray() {
    final int partialSortSize = 200;
    final int arraySize = 10000;

    Integer[] unsorted = new Integer[arraySize];
    Random rng = new Random(42);
    for (int i = 0; i < arraySize; ++i) {
      unsorted[i] = rng.nextInt();
    }
    Integer[] sorted = unsorted.clone();
    Arrays.sort(sorted);

    TopElements.selectSmallest(unsorted, partialSortSize);
    assertSorted(unsorted, sorted, partialSortSize);
  }

  private int getOutOfBoundsIndex(Integer[] elements) {
    return elements.length + 1;
  }

  private void assertSorted(Integer[] elements, Integer[] sorted, int k) {
    for (int i = 0; i < k; ++i) {
      assertEquals(sorted[i], elements[i]);
    }
  }
}
