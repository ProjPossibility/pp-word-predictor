package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An implementation of {@link ImmutableLexicon} backed by a {@code TreeMap}.
 * 
 * @author Michael Parker
 */
public class TreeMapImmutableLexicon implements ImmutableLexicon {
  private final SortedMap<String, WordSignificance> probabilities;

  /**
   * Constructs a new {@link TreeMapImmutableLexicon} from a copy of the given
   * map from words to their significance.
   * 
   * @param map the map from words to their significance
   * @return the new immutable lexicon
   */
  public static TreeMapImmutableLexicon fromMap(Map<String, Integer> map) {
    SortedMap<String, WordSignificance> probabilities = new TreeMap<String, WordSignificance>();
    for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
      String word = mapEntry.getKey();
      probabilities.put(word, new WordSignificance(word, mapEntry.getValue()));
    }
    return new TreeMapImmutableLexicon(probabilities);
  }

  /**
   * Constructs a new {@link TreeMapImmutableLexicon} from the contents of the
   * given file. Each line in the file must follow the format specified by
   * {@link SignificanceFileReader}. The lines in the file do not need to be
   * sorted lexicographically by word.
   * 
   * @param f the file containing words and their significance
   * @return a new immutable lexicon
   * @throws IOException
   */
  public static TreeMapImmutableLexicon fromFile(File f) throws IOException {
    SortedMap<String, WordSignificance> probabilities = new TreeMap<String, WordSignificance>();
    SignificanceFileReader reader = new SignificanceFileReader(f);
    while (true) {
      WordSignificance significance = reader.readNext();
      if (significance == null) {
        break;
      }
      probabilities.put(significance.word, significance);
    }
    return new TreeMapImmutableLexicon(probabilities);
  }

  private TreeMapImmutableLexicon(
      SortedMap<String, WordSignificance> probabilities) {
    this.probabilities = Collections.unmodifiableSortedMap(probabilities);
  }

  public WordSignificance getSignificance(String word) {
    return probabilities.get(word);
  }

  public Iterable<WordSignificance> getSignificance(String lowBound,
      String highBound) {
    if ((lowBound != null) && (highBound != null)) {
      return probabilities.subMap(lowBound, highBound).values();
    } else if (lowBound != null) {
      return probabilities.tailMap(lowBound).values();
    } else if (highBound != null) {
      return probabilities.headMap(highBound).values();
    }
    return probabilities.values();
  }
}
