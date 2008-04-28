package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotationFactory;
import org.ss12.wordprediction.newcore.Scorer;

/**
 * 
 * 
 * @author Michael Parker
 */
public class FrecencyAnnotationFactory implements
    AnnotationFactory<FrecencyAnnotation> {
  /**
   * The default weights for each of the buckets.
   */
  public static final int[] DEFAULT_WEIGHTS = { 1, 2, 5, 10 };

  /**
   * The default maximum number of annotations for each bucket.
   */
  public static final int[] BUCKET_SIZES = { 1000, 500, 200, 10 };

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
      int lhsScore = getFrecencyScore(lhs, weights);
      int rhsScore = getFrecencyScore(rhs, weights);

      if (lhsScore == rhsScore) {
        return 0;
      }
      return (lhsScore > rhsScore) ? -1 : 1;
    }

    int getFrecencyScore(FrecencyAnnotation annotation, int[] weights) {
      int sum = annotation.totalFrequency;
      for (int i = 0; i < weights.length; ++i) {
        sum += (weights[i] * annotation.bucketCounts[i]);
      }
      return sum;
    }
  };

  private final FrecencyAnnotationQueue unigramQueue;
  private final FrecencyAnnotationQueue bigramQueue;
  private final FrecencyAnnotationQueue trigramQueue;

  public FrecencyAnnotationFactory() {
    unigramQueue = new FrecencyAnnotationQueue(null);
    bigramQueue = new FrecencyAnnotationQueue(null);
    trigramQueue = new FrecencyAnnotationQueue(null);
  }

  public FrecencyAnnotation copyAnnotation(FrecencyAnnotation annotation) {
    // TODO(mgp)
    return annotation;
  }

  public FrecencyAnnotation newAnnotation(String word) {
    // TODO Auto-generated method stub
    return null;
  }

  public Scorer<FrecencyAnnotation> newScorer() {
    return null; // new FrecencyAnnotationScorer();
  }

  static {
    int sum = 0;
    for (int i = 0; i < BUCKET_SIZES.length; ++i) {
      sum += BUCKET_SIZES[i];
    }

    // TOTAL_BUCKET_SIZE = sum;
  }

}
