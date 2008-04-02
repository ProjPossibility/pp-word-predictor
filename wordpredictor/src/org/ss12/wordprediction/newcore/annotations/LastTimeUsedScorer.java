package org.ss12.wordprediction.newcore.annotations;

import java.util.Collections;

import org.ss12.wordprediction.newcore.Scorer;

/**
 * A {@link Scorer} implementation for {@link LastTimeUsedAnnotation} instances.
 * 
 * @author Michael Parker
 */
class LastTimeUsedScorer extends AbstractScorer<LastTimeUsedAnnotation> {
  public LastTimeUsedScorer() {
    super(Collections.reverseOrder(LastTimeUsedAnnotation.COMPARATOR));
  }
}
