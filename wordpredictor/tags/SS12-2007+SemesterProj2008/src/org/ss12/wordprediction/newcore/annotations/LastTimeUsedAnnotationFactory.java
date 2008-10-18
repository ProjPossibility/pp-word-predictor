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
  /**
   * An implementation of {@link LastTimeUsedClock} that delegates to
   * {@link java.lang.System#currentTimeMillis()}.
   */
  public static final LastTimeUsedClock SYSTEM_CLOCK = new LastTimeUsedClock() {
    public long getTimeInMillis() {
      return System.currentTimeMillis();
    }
  };

  private final LastTimeUsedClock clock;

  /**
   * Constructs the factory so that all created {@link LastTimeUsedAnnotation}
   * instances use {@link #SYSTEM_CLOCK}.
   */
  public LastTimeUsedAnnotationFactory() {
    this(SYSTEM_CLOCK);
  }

  /**
   * Constructs the factory so that all created {@link LastTimeUsedAnnotation}
   * instances use the given clock.
   * 
   * @param clock the clock for all annotations.
   */
  public LastTimeUsedAnnotationFactory(LastTimeUsedClock clock) {
    this.clock = clock;
  }

  public LastTimeUsedAnnotation newAnnotation(String word, int numPrecedingWords) {
    return new LastTimeUsedAnnotation(word, clock);
  }

  public Scorer<LastTimeUsedAnnotation> newScorer() {
    return new LastTimeUsedScorer();
  }

  public LastTimeUsedAnnotation copyAnnotation(LastTimeUsedAnnotation annotation) {
    return new LastTimeUsedAnnotation(annotation);
  }
}
