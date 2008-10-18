package org.ss12.wordprediction.newcore;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * An immutable lexicon wrapper around arrays of sorted
 * {@link WordFrequencyPair} instances.
 * 
 * @author Michael Parker
 */
public class SortedArrayImmutableLexicon implements ImmutableLexicon {
  private final List<WordFrequencyPair> freqs;
  private boolean isClosed = false;
  
  private static final Comparator<WordFrequencyPair> WORD_COMPARATOR =
      new Comparator<WordFrequencyPair>() {
    public int compare(WordFrequencyPair lhs, WordFrequencyPair rhs) {
      return lhs.word.compareTo(rhs.word);
    }
  };

  public static SortedArrayImmutableLexicon fromSortedArray(
      WordFrequencyPair[] freqs) {
    return new SortedArrayImmutableLexicon(freqs.clone());
  }

  public static SortedArrayImmutableLexicon fromUnsortedArray(
      WordFrequencyPair[] freqs) {
    WordFrequencyPair[] sortedFreqs = freqs.clone();
    Arrays.sort(sortedFreqs, WORD_COMPARATOR);
    return new SortedArrayImmutableLexicon(sortedFreqs);
  }

  private SortedArrayImmutableLexicon(WordFrequencyPair[] freqs) {
    this.freqs = Arrays.asList(freqs);
    isClosed = false;
  }

  public Iterable<WordFrequencyPair> getSignificance(String lowBound,
      String highBound) throws IllegalStateException {
    if (isClosed) {
      throw new IllegalStateException(/* TODO */);
    }

    int lowIndex = ((lowBound == null) || (lowBound.equals(""))) ?
        0 : getLowerBound(lowBound);
    int highIndex = (highBound == null) ?
        freqs.size() : getUpperBound(highBound);
    return freqs.subList(lowIndex, highIndex);
  }

  private int getLowerBound(String word) {
    int lo = 0, hi = freqs.size();
    while (lo < hi) {
      int mid = (lo + hi) / 2;
      if (freqs.get(mid).word.compareTo(word) < 0) {
        lo = mid + 1;
      } else {
        hi = mid;
      }
    }
    return lo;
  }

  private int getUpperBound(String word) {
    int lo = 0, hi = freqs.size();
    while (lo < hi) {
      int mid = (lo + hi) / 2;
      if (word.compareTo(freqs.get(mid).word) < 0) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    return lo;
  }

  public void close() {
    isClosed = true;
  }
}
