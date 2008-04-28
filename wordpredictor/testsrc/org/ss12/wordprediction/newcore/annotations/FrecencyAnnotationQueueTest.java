package org.ss12.wordprediction.newcore.annotations;

import junit.framework.TestCase;

/**
 * Unit test for {@link FrecencyAnnotationQueue}.
 * 
 * @author Michael Parker
 */
public class FrecencyAnnotationQueueTest extends TestCase {
  public void testUpdateBuckets() {

  }

  public void testQueueWrapping() {
    int[] bucketSizes = { 2, 1 };
    FrecencyAnnotationQueue queue = new FrecencyAnnotationQueue(bucketSizes);

    FrecencyAnnotation a1 = new FrecencyAnnotation("word1", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(1, queue.size);
    assertEquals(0, queue.startIndex);

    FrecencyAnnotation a2 = new FrecencyAnnotation("word2", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(2, queue.size);
    assertEquals(0, queue.startIndex);

    FrecencyAnnotation a3 = new FrecencyAnnotation("word3", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(a3, queue.annotations[2]);
    assertEquals(3, queue.size);
    assertEquals(0, queue.startIndex);

    a2.update();
    assertEquals(a2, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(a3, queue.annotations[2]);
    assertEquals(3, queue.size);
    assertEquals(1, queue.startIndex);
  }

  private void assertBucketValues(FrecencyAnnotation annotation,
      int firstValue, int secondValue, int thirdValue) {
    assertEquals(firstValue, annotation.bucketCounts[0]);
    assertEquals(secondValue, annotation.bucketCounts[1]);
    assertEquals(thirdValue, annotation.bucketCounts[2]);
  }
}
