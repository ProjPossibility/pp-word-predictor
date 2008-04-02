package org.ss12.wordprediction.newcore;

import java.util.Comparator;

/**
 * Annotates a word with its frequency.
 * 
 * @author Michael Parker
 */
class FrequencyAnnotation extends AnnotatedWord {
  /**
   * A comparator by associate frequency.
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

  int frequency = 1;

  FrequencyAnnotation(String word) {
    super(word);
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
