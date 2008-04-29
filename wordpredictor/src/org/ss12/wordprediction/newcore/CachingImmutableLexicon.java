package org.ss12.wordprediction.newcore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.ss12.wordprediction.TopElements;

/**
 * Decorates another {@link ImmutableLexicon} implementation so that the most
 * frequent words are cached.
 * 
 * @author Michael Parker
 */
public class CachingImmutableLexicon {
  // Letter distribution from
  // http://www.simonsingh.com/The_Black_Chamber/frequencyanalysis.html
  private static final float[] FREQ_DISTRIBUTION = {
    .082f, .015f, .028f, .043f, .127f, .022f,       // A through F
    .02f, .061f, .07f, .002f, .008f, .04f, .024f,   // G through M
    .067f, .075f, .019f, .001f, .06f, .063f,        // N through S
    .091f, .028f, .01f, .024f, .002f, .02f, .001f   // T through Z
  };

  public static ImmutableLexicon createCache(
      ImmutableLexicon lexicon, int approxSize) {
    List<WordFrequencyPair> allFreqs = new LinkedList<WordFrequencyPair>();
    for (char i = 'a'; i <= 'z'; ++i) {
      // Get all WordFrequencyPairs from the lexicon.
      String lowBound = String.valueOf(i);
      String highBound = WordSequence.getUpperBound(lowBound);
      Iterable<WordFrequencyPair> letterFreqs = lexicon.getSignificance(lowBound, highBound);

      // Sort only the WordFrequencyPairs we will cache.
      int letterSize = getSize(0, approxSize);
      List<WordFrequencyPair> letterFreqsList = new LinkedList<WordFrequencyPair>();
      for (WordFrequencyPair letterFreq : letterFreqs) {
        letterFreqsList.add(letterFreq);
      }
      WordFrequencyPair[] letterFreqsArray = letterFreqsList.toArray(
          new WordFrequencyPair[0]);
      TopElements.selectSmallest(letterFreqsArray, letterSize,
          Collections.reverseOrder(WordFrequencyPair.COMPARATOR));

      int copySize = (letterFreqsArray.length < letterSize) ?
          letterFreqsArray.length : letterSize;
      // Append the sorted WordFrequencyPairs to the alphabetized list.
      for (int j = 0; j < copySize; ++j) {
        allFreqs.add(letterFreqsArray[j]);
      }
    }
    return SortedArrayImmutableLexicon.fromSortedArray(
        allFreqs.toArray(new WordFrequencyPair[0]));
  }

  private static int getSize(int letterIndex, int approxSize) {
    return (int) Math.ceil(FREQ_DISTRIBUTION[letterIndex] * approxSize);
  }
}
