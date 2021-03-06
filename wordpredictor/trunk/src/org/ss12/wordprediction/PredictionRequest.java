package org.ss12.wordprediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A request for a prediction.
 * 
 * @author Michael Parker
 */
public final class PredictionRequest {
  private final List<String> precedingWords;
  private final String incompleteWord;
  private final int numPredictions;

  /**
   * Creates a new {@code PredictionRequest} that contains an incomplete word
   * without any context.
   * 
   * @param incompleteWord the incomplete word to provide suggestions for
   * @param numPredictions the maximum number of predictions to return
   * @return a new {@code PredictionRequest} instance
   */
  public static PredictionRequest from(String incompleteWord, int numPredictions) {
    return new PredictionRequest(incompleteWord, numPredictions);
  }

  /**
   * Creates a new {@code PredictionRequest} that contains an incomplete word
   * and the two preceding words, either of which could possibly be {@code null}
   * if unknown.
   * 
   * @param incompleteWord the incomplete word to provide suggestions for
   * @param prevWord the first preceding word, possibly {@code null}
   * @param prevPrevWord the second preceding word, possibly {@code null}
   * @param numPredictions the maximum number of predictions to return
   * @return a new {@link PredictionRequest} instance
   */
  public static PredictionRequest from(String incompleteWord, String prevWord,
      String prevPrevWord, int numPredictions) {
    if (prevWord == null) {
      return new PredictionRequest(incompleteWord, numPredictions);
    } else if (prevPrevWord == null) {
      return new PredictionRequest(incompleteWord, Arrays.asList(prevWord),
          numPredictions);
    } else {
      return new PredictionRequest(incompleteWord, Arrays.asList(prevWord,
          prevPrevWord), numPredictions);
    }
  }

  /**
   * Creates a new {@code PredictionRequest} that contains an incomplete word
   * and a list of the words that immediately precede it. If the list is not
   * empty, index {@code 0} is the word that immediately precedes the incomplete
   * word, index {@code 1} is the word that immediately precedes that word, and
   * so forth.
   * 
   * @param incompleteWord the incomplete word to provide suggestions for
   * @param precedingWords a list of the preceding words
   * @param numPredictions the maximum number of predictions to return
   * @return a new {@link PredictionRequest} instance
   */
  public static PredictionRequest from(String incompleteWord,
      List<String> precedingWords, int numPredictions) {
    if (precedingWords.isEmpty()) {
      return new PredictionRequest(incompleteWord, numPredictions);
    } else {
      return new PredictionRequest(incompleteWord, precedingWords,
          numPredictions);
    }
  }

  // Private constructor, use the factory methods instead.
  private PredictionRequest(String incompleteWord, int numPredictions) {
    this.incompleteWord = incompleteWord;
    this.precedingWords = Collections.emptyList();
    this.numPredictions = numPredictions;
  }

  // Private constructor, use the factory methods instead.
  private PredictionRequest(String incompleteWord, List<String> precedingWords,
      int numPredictions) {
    this.incompleteWord = incompleteWord;
    this.precedingWords = Collections.unmodifiableList(new ArrayList<String>(
        precedingWords));
    this.numPredictions = numPredictions;
  }

  /**
   * @return the incomplete word we should provide suggestions for
   */
  public String getIncompleteWord() {
    return incompleteWord;
  }

  /**
   * Returns an immutable list of words that immediately precede the incomplete
   * word, meaning its context. Index {@code 0} is the word that immediately
   * precedes the incomplete word, index {@code 1} is the word that immediately
   * precedes that word, and so forth.
   * 
   * @return the words that precede the incomplete word, possibly empty
   */
  public List<String> getPrecedingWords() {
    return precedingWords;
  }

  /**
   * Returns the first word that precedes the incomplete word, or {@code null}
   * if this context was not provided upon construction. This is a convenience
   * method for returning the word at index {@code 0} of
   * {@link #getPrecedingWords()}.
   * 
   * @return the first preceding word, or {@code null} if this context was not
   *         provided
   */
  public String getPrevWord() {
    return (precedingWords.size() >= 1) ? precedingWords.get(0) : null;
  }

  /**
   * Returns the second word that precedes the incomplete word, or {@code null}
   * if this context was not provided upon construction. This is a convenience
   * method for returning the word at index {@code 1} of
   * {@link #getPrecedingWords()}.
   * 
   * @return the second preceding word, or {@code null} if this context was not
   *         provided
   */
  public String getPrevPrevWord() {
    return (precedingWords.size() >= 2) ? precedingWords.get(1) : null;
  }

  /**
   * @return the maximum number of predictions to return
   */
  public int getNumPredictions() {
    return numPredictions;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("incompleteWord=").append(incompleteWord).append(' ');
    sb.append("precedingWords=").append(precedingWords).append(' ');
    sb.append("numPredictions=").append(numPredictions);
    return sb.toString();
  }
}
