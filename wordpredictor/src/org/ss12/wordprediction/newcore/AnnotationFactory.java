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
   * Returns a new annotation for the given word. If the number of preceding
   * words is {@code 0}, then the annotation is used for a unigram; if
   * {@code 1}, then the annotation is used for a bigram; if {@code 2}, then
   * the annotation is used for a trigram; and so forth.
   * 
   * @param word the word to annotate
   * @param numPrecedingWords the number of preceding words
   * @return a new annotation
   */
  public T newAnnotation(String word, int numPrecedingWords);

  /**
   * Performs a deep copy of the given annotation; this can be used for
   * defensive copying to ensure thread safety.
   * 
   * @param annotation the annotation to copy
   * @return a copy of the annotation
   */
  public T copyAnnotation(T annotation);
}
