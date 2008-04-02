package org.ss12.wordprediction.newcore;

/**
 * A factory for {@link Scorer} implementations.
 * 
 * @author Michael Parker
 */
public interface AnnotationFactory<T extends AnnotatedWord> {
  /**
   * @return a new {@link Scorer} instance
   */
  public Scorer<T> newScorer();

  /**
   * Returns a new annotation for the given word.
   * 
   * @param word the word to annotate
   * @return a new annotation
   */
  public T newAnnotation(String word);

  /**
   * Performs a deep copy of the given annotation; this can be used for
   * defensive copying to ensure thread safety.
   * 
   * @param annotation the annotation to copy
   * @return a copy of the annotation
   */
  public T copyAnnotation(T annotation);
}
