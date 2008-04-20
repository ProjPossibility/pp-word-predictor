package org.ss12.wordprediction.newcore;

/**
 * An immutable collection of {@link WordFrequencyPair} instances, sorted
 * lexicographically by word.
 * 
 * @author Michael Parker
 */
public interface ImmutableLexicon {
  /**
   * Returns the significance of all words lexicographically between the given
   * bounds.
   * 
   * @param lowBound the inclusive lower bound, or {@code null} for no lower
   *          bound
   * @param highBound the exclusive higher bound, or {@code null} for no higher
   *          bound
   * @return an iterable over the significance of all words between the bounds,
   *         possibly empty
   */
  public Iterable<WordFrequencyPair> getSignificance(String lowBound,
      String highBound);
}
