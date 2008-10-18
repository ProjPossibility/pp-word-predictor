package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotationFactory;
import org.ss12.wordprediction.newcore.Scorer;

/**
 * An {@link AnnotationFactory} implementation for
 * {@link FrecencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
public class FrecencyAnnotationFactory implements
    AnnotationFactory<FrecencyAnnotation> {
  /**
   * The default maximum number of annotations for each bucket.
   */
  public static final int[] DEFAULT_BUCKET_SIZES = { 10, 100, 500, 1000 };

  /**
   * The default weights for each of the buckets.
   */
  public static final int[] DEFAULT_WEIGHTS = { 50, 10, 5, 3 };

  /**
   * A comparator that sorts {@link FrecencyAnnotation} instances by their
   * associated score.
   */
  static final class ScoringComparator implements
      Comparator<FrecencyAnnotation> {
    private final int[] weights;

    ScoringComparator(int[] weights) {
      this.weights = weights;
    }

    public int compare(FrecencyAnnotation lhs, FrecencyAnnotation rhs) {
      int lhsScore = getFrecencyScore(lhs);
      int rhsScore = getFrecencyScore(rhs);

      if (lhsScore == rhsScore) {
        return 0;
      }
      return (lhsScore > rhsScore) ? -1 : 1;
    }

    int getFrecencyScore(FrecencyAnnotation annotation) {
      int sum = annotation.totalFrequency;
      for (int i = 0; i < weights.length; ++i) {
        sum += (weights[i] * annotation.bucketCounts[i]);
      }
      return sum;
    }
  };

  private final int[] bucketSizes;
  private final int[] weights;

  private final FrecencyAnnotationQueue unigramQueue;
  private final FrecencyAnnotationQueue bigramQueue;
  private final FrecencyAnnotationQueue trigramQueue;

  public FrecencyAnnotationFactory() {
    this(DEFAULT_BUCKET_SIZES, DEFAULT_WEIGHTS);
  }

  public FrecencyAnnotationFactory(int[] bucketSizes, int[] weights) {
    this.bucketSizes = bucketSizes;
    this.weights = weights;
    
    this.unigramQueue = new FrecencyAnnotationQueue(bucketSizes);
    this.bigramQueue = new FrecencyAnnotationQueue(bucketSizes);
    this.trigramQueue = new FrecencyAnnotationQueue(bucketSizes);
  }

  public FrecencyAnnotation copyAnnotation(FrecencyAnnotation annotation) {
    // TODO(mgp)
    return annotation;
  }

  public FrecencyAnnotation newAnnotation(String word, int numPrecedingWords) {
    if (numPrecedingWords == 0) {
      return new FrecencyAnnotation(word, unigramQueue, bucketSizes.length);  
    } else if (numPrecedingWords == 1) {
      return new FrecencyAnnotation(word, bigramQueue, bucketSizes.length);
    } else if (numPrecedingWords == 2) {
      return new FrecencyAnnotation(word, trigramQueue, bucketSizes.length);
    }

    throw new IllegalArgumentException("Invalid number of preceding words: " +
        numPrecedingWords);
  }

  public Scorer<FrecencyAnnotation> newScorer() {
    return new FrecencyAnnotationScorer(new ScoringComparator(weights));
  }
}
