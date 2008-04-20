package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of {@link ImmutableLexicon} backed by a {@code TreeMap}.
 * 
 * @author Michael Parker
 */
public class TreeMapImmutableLexicon implements ImmutableLexicon {
  private static final Logger log = Logger.getLogger(
      TreeMapImmutableLexicon.class.getName());

  private final SortedMap<String, WordFrequencyPair> frequencies;
  private boolean isClosed;

  /**
   * Constructs a new {@link TreeMapImmutableLexicon} from a copy of the given
   * map from words to their significance.
   * 
   * @param map the map from words to their significance
   * @return the new immutable lexicon
   */
  public static TreeMapImmutableLexicon fromMap(Map<String, Integer> map) {
    SortedMap<String, WordFrequencyPair> probabilities = new TreeMap<String, WordFrequencyPair>();
    for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
      String word = mapEntry.getKey();
      probabilities.put(word, new WordFrequencyPair(word, mapEntry.getValue()));
    }
    return new TreeMapImmutableLexicon(probabilities);
  }

  /**
   * Constructs a new {@link TreeMapImmutableLexicon} from a serialized
   * {@link TreeMap} of words mapped to their corresponding frequencies.
   * 
   * @param file the file containing the serialized map
   * @return the new immutable lexicon
   */
  public static TreeMapImmutableLexicon fromSerializedMap(File file) {
    TreeMap<String, Integer> map = new TreeMap<String, Integer>();
    try {
      FileInputStream objReader = new FileInputStream(file);
      ObjectInputStream in = new ObjectInputStream(objReader);
      map = (TreeMap<String, Integer>) in.readObject();
      in.close();
    } catch (IOException e) {
      log.log(Level.WARNING, "Could not deserialize map, creating empty map", e);
    } catch (ClassNotFoundException e) {
      log.log(Level.SEVERE, "Could not find TreeMap class", e);
    }

    return fromMap(map);
  }
  
  /**
   * Constructs a new {@link TreeMapImmutableLexicon} from the contents of the
   * given file. Each line in the file must follow the format specified by
   * {@link SignificanceTextFileReader}. The lines in the file do not need to be
   * sorted lexicographically by word.
   * 
   * @param f the file containing words and their significance
   * @return a new immutable lexicon
   * @throws IOException
   */
  public static TreeMapImmutableLexicon fromFile(File f) throws IOException {
    SortedMap<String, WordFrequencyPair> probabilities = new TreeMap<String, WordFrequencyPair>();
    SignificanceTextFileReader reader = new SignificanceTextFileReader(f);
    while (true) {
      WordFrequencyPair significance = reader.readNext();
      if (significance == null) {
        break;
      }
      probabilities.put(significance.word, significance);
    }
    return new TreeMapImmutableLexicon(probabilities);
  }

  private TreeMapImmutableLexicon(
      SortedMap<String, WordFrequencyPair> probabilities) {
    this.frequencies = Collections.unmodifiableSortedMap(probabilities);
    isClosed = false;
  }

  public Iterable<WordFrequencyPair> getSignificance(String lowBound,
      String highBound) {
    if (isClosed) {
      throw new IllegalStateException("Lexicon is closed, cannot read");
    }

    if ((lowBound != null) && (highBound != null)) {
      return frequencies.subMap(lowBound, highBound).values();
    } else if (lowBound != null) {
      return frequencies.tailMap(lowBound).values();
    } else if (highBound != null) {
      return frequencies.headMap(highBound).values();
    }
    return frequencies.values();
  }

  public void close() {
    isClosed = true;
  }
}
