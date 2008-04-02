package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotatedWord;
import org.ss12.wordprediction.newcore.WordPredictorUtil;

/**
 * Annotates a word with its frequency.
 * 
 * @author Michael Parker
 */
public class FrequencyAnnotation extends AnnotatedWord {
  /**
   * A comparator that sorts {@link FrequencyAnnotation} instances by their
   * associated frequency.
   */
  static final Comparator<FrequencyAnnotation> COMPARATOR =
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
    this(word, 1);
  }

  FrequencyAnnotation(FrequencyAnnotation annotation) {
    this(annotation.getWord(), annotation.getFrequency());
  }

  FrequencyAnnotation(String word, int frequency) {
    super(word);
    this.frequency = frequency;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof FrequencyAnnotation) {
      FrequencyAnnotation annotation = (FrequencyAnnotation) obj;
      return (getWord().equals(annotation.getWord()) &&
          (frequency == annotation.frequency));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return WordPredictorUtil.hashCode(getWord(), frequency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("word=").append(getWord()).append(", ");
    sb.append("freq=").append(frequency);
    return sb.toString();
  }
}
