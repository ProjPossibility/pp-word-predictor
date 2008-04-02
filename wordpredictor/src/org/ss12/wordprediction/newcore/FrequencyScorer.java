package org.ss12.wordprediction.newcore;

/**
 * An {@link Scorer} implementation for {@link FrequencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
public class FrequencyScorer extends AbstractScorer<FrequencyAnnotation> {
  public FrequencyScorer() {
    super(FrequencyAnnotation.COMPARATOR);
  }
}
