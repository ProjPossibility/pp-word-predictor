package org.ss12.wordprediction.newcore;

import org.ss12.wordprediction.newcore.annotations.AbstractScorer;

/**
 * An {@link AnnotationFactory} implementation for {@link MockAnnotation}
 * instances.
 * 
 * @author Michael Parker
 */
public class MockAnnotationFactory implements AnnotationFactory<MockAnnotation> {
  private static final NullAnnotationScorer SCORER = new NullAnnotationScorer();

  public MockAnnotation copyAnnotation(MockAnnotation annotation) {
    return annotation;
  }

  public MockAnnotation newAnnotation(String word, int numPrecedingWords) {
    return new MockAnnotation(word);
  }

  public Scorer<MockAnnotation> newScorer() {
    return SCORER;
  }

  private static final class NullAnnotationScorer extends
      AbstractScorer<MockAnnotation> {
    public NullAnnotationScorer() {
      super(MockAnnotation.COMPARATOR);
    }
  }
}
