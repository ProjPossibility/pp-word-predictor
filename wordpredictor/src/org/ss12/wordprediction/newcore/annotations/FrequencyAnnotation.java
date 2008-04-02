package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotatedWord;

/**
 * Annotates a word with its frequency.
 * 
 * @author Michael Parker
 */
class FrequencyAnnotation extends AnnotatedWord {
  /**
   * A comparator that sorts {@link FrequencyAnnotation} instances by their
   * associated frequency.
   */
  public static final Comparator<FrequencyAnnotation> COMPARATOR =
      new Comparator<FrequencyAnnotation>() {
    public int compare(FrequencyAnnotation lhs, FrequencyAnnotation rhs) {
      if (lhs.frequency == rhs.frequency) {
        return 0;
      }
      return (lhs.frequency < rhs.frequency) ? -1 : 1;
    }
  };

  int frequency;

  FrequencyAnnotation(String word) {
    super(word);
    frequency = 1;
  }

  FrequencyAnnotation(FrequencyAnnotation annotation) {
    super(annotation.getWord());
    frequency = annotation.frequency;
  }

  /**
   * @return the frequency of this word
   */
  int getFrequency() {
    return frequency;
  }

  protected void update() {
    ++frequency;
  }
}
