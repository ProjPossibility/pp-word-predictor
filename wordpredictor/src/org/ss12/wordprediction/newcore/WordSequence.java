package org.ss12.wordprediction.newcore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class WordSequence implements Comparable<WordSequence>, Serializable {
  private final List<String> words;

  /**
   * Returns the next word.
   * 
   * @param word the word to return the successor of
   * @return the next lexicographically ordered word, or {@code null} if no such
   *         word exists
   */
  public static String getUpperBound(String word) {
    for (int i = word.length() - 1; i >= 0; --i) {
      char c = word.charAt(i);
      if (c != 'z') {
        StringBuilder sb = new StringBuilder(i + 1);
        sb.append(word.subSequence(0, i));
        sb.append(++c);
        return sb.toString();
      }
    }
    return null;
  }

  /**
   * Returns the next lexicographically ordered {@link WordSequence}.
   * 
   * @param wordSequence the sequence to return the successor of
   * @return the next lexicographically ordered sequence, or {@code null} if no
   *         such sequence exists
   */
  public static WordSequence getNextSequence(WordSequence wordSequence) {
    List<String> words = wordSequence.getWords();
    for (int i = words.size() - 1; i >= 0; --i) {
      String word = words.get(i);
      String upperBound = getUpperBound(word);
      if (upperBound != null) {
        List<String> wordsList = new ArrayList<String>(i + 1);
        wordsList.addAll(words.subList(0, i));
        wordsList.add(upperBound);
        return new WordSequence(wordsList);
      }
    }
    return null;
  }

  /**
   * Creates a new {@link WordSequence} from the given words.
   * 
   * @param words the sequence of words
   */
  public WordSequence(String... words) {
    if (words.length < 1) {
      throw new IllegalArgumentException("Must pass in at least one word");
    }
    this.words = Collections.unmodifiableList(Arrays.asList(words));
  }

  private WordSequence(List<String> words) {
    this.words = Collections.unmodifiableList(words);
  }

  /**
   * @return the sequence of words as an unmodifiable list
   */
  public List<String> getWords() {
    return words;
  }

  /**
   * @return the last words in the sequence of words
   */
  public String getLastWord() {
    return words.get(words.size() - 1);
  }

  public int compareTo(WordSequence wordSequence) {
    List<String> otherWords = wordSequence.words;
    int minSize = (words.size() < otherWords.size()) ? words.size()
        : otherWords.size();
    for (int i = 0; i < minSize; ++i) {
      int wordCmp = words.get(i).compareTo(otherWords.get(i));
      if (wordCmp != 0) {
        return wordCmp;
      }
    }
    if (words.size() != otherWords.size()) {
      return (words.size() < otherWords.size()) ? -1 : 1;
    }
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof WordSequence) {
      WordSequence wordSequence = (WordSequence) obj;
      return words.equals(wordSequence.getWords());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return words.hashCode();
  }

  @Override
  public String toString() {
    return words.toString();
  }
}