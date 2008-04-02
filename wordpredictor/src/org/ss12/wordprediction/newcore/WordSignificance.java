package org.ss12.wordprediction.newcore;

import java.util.Comparator;

/**
 * A pairing of a word and its associated significance.
 * 
 * @author Michael Parker
 */
public class WordSignificance {
  /**
   * A comparator by associated significance.
   */
  public static final Comparator<WordSignificance> COMPARATOR =
      new Comparator<WordSignificance>() {
    public int compare(WordSignificance lhs, WordSignificance rhs) {
      if (lhs.significance == rhs.significance) {
        return 0;
      }
      return (lhs.significance < rhs.significance) ? -1 : 1;
    }
  };

  public final String word;
  public final int significance;

  /**
   * Pairs a word with its significance.
   * 
   * @param word the word
   * @param significance the significance
   */
  public WordSignificance(String word, int significance) {
    this.word = word;
    this.significance = significance;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("word=").append(word).append(", ");
    sb.append("s=").append(significance);
    return sb.toString();
  }
}
