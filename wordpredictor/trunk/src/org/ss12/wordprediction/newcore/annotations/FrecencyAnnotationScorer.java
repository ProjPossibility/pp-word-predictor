package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.Scorer;

/**
 * A {@link Scorer} implementation for {@link FrecencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
class FrecencyAnnotationScorer extends AbstractScorer<FrecencyAnnotation> {
  FrecencyAnnotationScorer(Comparator<FrecencyAnnotation> comparator) {
    super(comparator);
  }
}
