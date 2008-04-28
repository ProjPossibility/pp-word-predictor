package org.ss12.wordprediction;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Utility methods to efficiently find the {@code k} smallest or largest
 * elements in an array containing {@code n} elements, where {@code k} is less
 * than {@code n}. The {@code k} elements are returned in sorted order. The code
 * here is derived from the pseudocode in the
 * <a href="http://en.wikipedia.org/wiki/Selection_algorithm">Selection algorithm
 * wikipedia article</a>.
 * 
 * @author Michael Parker
 */
public class TopElements {
  private TopElements() {
    // Static utility methods only, do not allow instantation.
  }

  /**
   * A comparator that orders {@link Comparable} elements by their natural
   * ordering.
   */
  @SuppressWarnings("unchecked")
  static final Comparator<Comparable> naturalComparator = new Comparator<Comparable>() {
    public int compare(Comparable lhs, Comparable rhs) {
      return lhs.compareTo(rhs);
    }
  };

  /**
   * A comparator that orders {@link Comparable} elements by the reverse of
   * their natural ordering.
   */
  @SuppressWarnings("unchecked")
  static final Comparator<Comparable> reverseNaturalComparator = new Comparator<Comparable>() {
    public int compare(Comparable lhs, Comparable rhs) {
      return rhs.compareTo(lhs);
    }
  };

  /**
   * Modifies the given array so that the first {@code k} elements are the
   * smallest elements in the array in sorted order, where the natural ordering
   * of the elements is used.
   * 
   * @param elements the elements to partially sort
   * @param k the number of elements to return as sorted
   */
  public static <T extends Comparable<T>> void selectSmallest(T[] elements,
      int k) {
    selectSmallest(elements, k, naturalComparator);
  }

  /**
   * Modifies the given array so that the first {@code k} elements are the
   * largest elements in the array in sorted order, where the natural ordering
   * of the elements is used.
   * 
   * @param elements the elements to partially sort
   * @param k the number of elements to return as sorted
   */
  public static <T extends Comparable<T>> void selectLargest(T[] elements, int k) {
    selectSmallest(elements, k, reverseNaturalComparator);
  }

  /**
   * Modifies the given array so that the first {@link k} elements are the
   * smallest elements in the array in sorted order, where ordering is determined
   * by the given comparator. To select the largest, reverse the comparator using
   * {@link java.util.Collections#reverseOrder(Comparator)}.
   * 
   * @param elements the elements to partially sort
   * @param k the number of elements to return as sorted
   * @param comparator the comparator to sort elements
   */
  public static <T> void selectSmallest(T[] elements, int k, Comparator<T> comparator) {
    if (k == 0) {
      return;
    } else if (elements.length <= k) {
      Arrays.sort(elements, comparator);
    } else {
      partition(elements, comparator, k - 1);
      Arrays.sort(elements, 0, k);
    }
  }

  private static <T> void partition(T[] elements, Comparator<T> comparator,
      int k) {
    int left = 0;
    int right = elements.length - 1;
    while (true) {
      int pivotIndex = getPivotIndex(elements, comparator, left, right);
      int lastStoreIndex = partition(elements, comparator, left, right,
          pivotIndex);
      if (k == lastStoreIndex) {
        return;
      } else if (k < lastStoreIndex) {
        right = lastStoreIndex - 1;
      } else {
        left = lastStoreIndex + 1;
      }
    }
  }

  private static <T> int partition(T[] elements, Comparator<T> comparator,
      int left, int right, int pivotIndex) {
    T temp = null;
    T pivotValue = elements[pivotIndex];

    temp = elements[pivotIndex];
    elements[pivotIndex] = elements[right];
    elements[right] = temp;

    int storeIndex = left;
    for (int i = left; i < right; ++i) {
      if (comparator.compare(elements[i], pivotValue) < 0) {
        temp = elements[storeIndex];
        elements[storeIndex] = elements[i];
        elements[i] = temp;

        ++storeIndex;
      }
    }

    temp = elements[right];
    elements[right] = elements[storeIndex];
    elements[storeIndex] = temp;

    return storeIndex;
  }

  static <T> int getPivotIndex(T[] elements, Comparator<T> comparator,
      int left, int right) {
    int pivotIndex = (left + right) / 2;
    int boundaryCmp = comparator.compare(elements[left], elements[right]);
    if (boundaryCmp < 0) {
      if (comparator.compare(elements[pivotIndex], elements[left]) < 0) {
        pivotIndex = left;
      } else if (comparator.compare(elements[right], elements[pivotIndex]) < 0) {
        pivotIndex = right;
      }
    } else {
      if (comparator.compare(elements[pivotIndex], elements[right]) < 0) {
        pivotIndex = right;
      } else if (comparator.compare(elements[left], elements[pivotIndex]) < 0) {
        pivotIndex = left;
      }
    }
    return pivotIndex;
  }
}
