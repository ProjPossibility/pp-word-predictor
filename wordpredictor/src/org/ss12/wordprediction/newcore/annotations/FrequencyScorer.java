package org.ss12.wordprediction.newcore.annotations;

import org.ss12.wordprediction.newcore.Scorer;

/**
 * A {@link Scorer} implementation for {@link FrequencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
class FrequencyScorer extends AbstractScorer<FrequencyAnnotation> {
  public FrequencyScorer() {
    super(FrequencyAnnotation.COMPARATOR);
  }
}
