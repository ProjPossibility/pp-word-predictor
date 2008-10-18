package org.ss12.wordprediction.newcore;

import java.util.Comparator;

/**
 * A pairing of a word and its associated significance.
 * 
 * @author Michael Parker
 */
public class WordFrequencyPair {
  /**
   * A comparator by associated significance.
   */
  public static final Comparator<WordFrequencyPair> COMPARATOR =
      new Comparator<WordFrequencyPair>() {
    public int compare(WordFrequencyPair lhs, WordFrequencyPair rhs) {
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
  public WordFrequencyPair(String word, int significance) {
    this.word = word;
    this.significance = significance;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (obj instanceof WordFrequencyPair) {
      WordFrequencyPair ws = (WordFrequencyPair) obj;
      return ((word.equals(ws.word)) && (significance == ws.significance));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return WordPredictorUtil.hashCode(word, significance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("word=").append(word).append(", ");
    sb.append("s=").append(significance);
    return sb.toString();
  }
}
