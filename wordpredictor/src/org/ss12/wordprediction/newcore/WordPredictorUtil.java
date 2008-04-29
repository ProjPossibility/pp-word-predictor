package org.ss12.wordprediction.newcore;

import java.util.Arrays;

/**
 * Colleciton of helpful static utility methods.
 * 
 * @author Michael Parker
 */
public class WordPredictorUtil {
  private WordPredictorUtil() {
    // Only static utility methods, so do not allow instantiation.
  }

  /**
   * Returns whether the two objects are equal to one another, allowing either
   * or both of the given objects to be {@code null}.
   * 
   * @param lhs the first argument
   * @param rhs the second argument
   * @return {@code true} if the two objects are equal, {@code false} otherwise
   */
  public static boolean equals(Object lhs, Object rhs) {
    return (lhs == null) ? (rhs == null) : lhs.equals(rhs);
  }

  /**
   * Convenient varargs form of {@link Arrays#hashCode(Object[])}.
   * 
   * @param objs the objects to hash
   * @return the hash over all the objects
   */
  public static int hashCode(Object... objs) {
    return Arrays.hashCode(objs);
  }

  /**
   * Convenient varargs form of {@link Arrays#toString(Object[])}.
   * 
   * @param objs the objects to convert to strings
   * @return the string representation of the objects
   */
  public static String toString(Object... objs) {
    return Arrays.toString(objs);
  }
}
