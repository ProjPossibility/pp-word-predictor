package org.ss12.wordprediction.newcore.annotations;

import org.ss12.wordprediction.newcore.AnnotationFactory;
import org.ss12.wordprediction.newcore.Scorer;

/**
 * An {@link AnnotationFactory} implementation for
 * {@link LastTimeUsedAnnotation} instances.
 * 
 * @author Michael Parker
 */
public class LastTimeUsedAnnotationFactory implements
    AnnotationFactory<LastTimeUsedAnnotation> {
  public LastTimeUsedAnnotation newAnnotation(String word, int numPrecedingWords) {
    return new LastTimeUsedAnnotation(word);
  }

  public Scorer<LastTimeUsedAnnotation> newScorer() {
    return new LastTimeUsedScorer();
  }

  public LastTimeUsedAnnotation copyAnnotation(LastTimeUsedAnnotation annotation) {
    return new LastTimeUsedAnnotation(annotation);
  }
}
