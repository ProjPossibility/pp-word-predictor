package org.ss12.wordprediction.newcore;

/**
 * Colleciton of helpful static utility methods.
 * 
 * @author Michael Parker
 */
class WordPredictorUtil {
  private WordPredictorUtil() {
    // Only static utility methods, so do not allow instantiation.
  }
  
  public static int hashCode(Object... objs) {
    int hashCode = 1;
    for (Object obj : objs) {
        hashCode = (31 * hashCode) + ((obj == null) ? 0 : obj.hashCode());
    }
    return hashCode;
  }
}
