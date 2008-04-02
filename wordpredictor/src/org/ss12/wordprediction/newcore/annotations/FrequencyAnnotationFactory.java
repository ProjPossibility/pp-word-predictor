package org.ss12.wordprediction.newcore.annotations;

import org.ss12.wordprediction.newcore.AnnotationFactory;
import org.ss12.wordprediction.newcore.Scorer;

/**
 * An {@link AnnotationFactory} implementation for {@link FrequencyAnnotation}
 * instances.
 * 
 * @author Michael Parker
 */
public class FrequencyAnnotationFactory implements
    AnnotationFactory<FrequencyAnnotation> {
  public FrequencyAnnotation newAnnotation(String word) {
    return new FrequencyAnnotation(word);
  }

  public Scorer<FrequencyAnnotation> newScorer() {
    return new FrequencyScorer();
  }

  public FrequencyAnnotation copyAnnotation(FrequencyAnnotation annotation) {
    return new FrequencyAnnotation(annotation);
  }
}
