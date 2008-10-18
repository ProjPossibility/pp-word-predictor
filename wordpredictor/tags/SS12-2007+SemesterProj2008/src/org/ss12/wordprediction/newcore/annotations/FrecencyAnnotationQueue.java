package org.ss12.wordprediction.newcore.annotations;

/**
 * A circular queue of {@link FrecencyAnnotation} instances.
 * 
 * @author Michael Parker
 */
class FrecencyAnnotationQueue {
  int startIndex;
  int size;

  final int[] bucketSizes;
  final int totalBucketSize;
  final FrecencyAnnotation[] annotations;

  /**
   * Constructs a new queue having the given bucket sizes. Buckets with smaller
   * element indices contain annotations that have been updated more recently.
   * 
   * @param bucketSizes the bucket sizes
   */
  FrecencyAnnotationQueue(int[] bucketSizes) {
    startIndex = 0;
    size = 0;

    this.bucketSizes = bucketSizes;
    this.totalBucketSize = getTotalBucketSize();
    this.annotations = new FrecencyAnnotation[totalBucketSize];
  }

  private int getTotalBucketSize() {
    int sum = 0;
    for (int i = 0; i < bucketSizes.length; ++i) {
      sum += bucketSizes[i];
    }
    return sum;
  }

  void add(FrecencyAnnotation annotation) {
    int partialSize = 0;
    for (int i = 0; i < bucketSizes.length; ++i) {
      partialSize += bucketSizes[i];
      if (partialSize > size) {
        break;
      }

      int currIndex = startIndex - partialSize;
      if (currIndex < 0) {
        currIndex += size;
      }
      annotations[currIndex].moveToBucket(i + 1);
    }

    if (size < totalBucketSize) {
      // Not yet at capacity.
      annotations[size] = annotation;
      ++size;
    } else {
      // At capacity, so we must evict the oldest annotation in the queue.
      annotations[startIndex] = annotation;
      ++startIndex;
      startIndex = (startIndex % totalBucketSize);
    }
  }
}
