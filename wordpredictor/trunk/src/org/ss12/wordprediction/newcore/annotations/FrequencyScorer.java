package org.ss12.wordprediction.newcore.annotations;

import java.util.Collections;

import org.ss12.wordprediction.newcore.Scorer;

/**
 * A {@link Scorer} implementation for {@link FrequencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
class FrequencyScorer extends AbstractScorer<FrequencyAnnotation> {
  public FrequencyScorer() {
    super(Collections.reverseOrder(FrequencyAnnotation.COMPARATOR));
  }
}
