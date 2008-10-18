package org.ss12.wordprediction.newcore.annotations;

import junit.framework.TestCase;

/**
 * Unit test for {@link FrecencyAnnotationQueue}.
 * 
 * @author Michael Parker
 */
public class FrecencyAnnotationQueueTest extends TestCase {
  public void testOneAnnotationOneBucket() {
    int[] bucketSizes = { 2 };
    FrecencyAnnotationQueue queue = new FrecencyAnnotationQueue(bucketSizes);

    FrecencyAnnotation a1 = new FrecencyAnnotation("word1", queue,
        bucketSizes.length);
    assertBucketValues(a1, 1);
    // Update so that bucket value reaches maximum.
    a1.update();
    assertBucketValues(a1, 2);
    // Update again and assert that bucket value remains unchanged.
    a1.update();
    assertBucketValues(a1, 2);
  }

  public void testOneAnnotationTwoBuckets() {
    int[] bucketSizes = { 1, 2 };
    FrecencyAnnotationQueue queue = new FrecencyAnnotationQueue(bucketSizes);

    FrecencyAnnotation a1 = new FrecencyAnnotation("word1", queue, bucketSizes.length);
    assertBucketValues(a1, 1, 0);
    // Update so that count moves to second bucket.
    a1.update();
    assertBucketValues(a1, 1, 1);
    // Update so that second bucket value reaches maximum.
    a1.update();
    assertBucketValues(a1, 1, 2);
    // Update again and assert that bucket value remains unchanged.
    a1.update();
    assertBucketValues(a1, 1, 2);
  }

  public void testTwoAnnotationsTwoBuckets() {
    int[] bucketSizes = { 1, 1 };
    FrecencyAnnotationQueue queue = new FrecencyAnnotationQueue(bucketSizes);

    FrecencyAnnotation a1 = new FrecencyAnnotation("word1", queue, bucketSizes.length);
    assertBucketValues(a1, 1, 0);

    // Add second annotation, so count for first annotation moves to second bucket.
    FrecencyAnnotation a2 = new FrecencyAnnotation("word2", queue, bucketSizes.length);
    assertBucketValues(a1, 0, 1);
    assertBucketValues(a2, 1, 0);

    // Update first annotation, so count for second annotation moves to second bucket.
    a1.update();
    assertBucketValues(a1, 1, 0);
    assertBucketValues(a2, 0, 1);

    // Update first annotation again, so second annotation has no values.
    a1.update();
    assertBucketValues(a1, 1, 1);
    assertBucketValues(a2, 0, 0);
  }
  
  public void testQueueWrapping() {
    int[] bucketSizes = { 2, 1 };
    FrecencyAnnotationQueue queue = new FrecencyAnnotationQueue(bucketSizes);

    // Add first annotation, thus incrementing the size.
    FrecencyAnnotation a1 = new FrecencyAnnotation("word1", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(1, queue.size);
    assertEquals(0, queue.startIndex);

    // Add second annotation, thus incrementing the size.
    FrecencyAnnotation a2 = new FrecencyAnnotation("word2", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(2, queue.size);
    assertEquals(0, queue.startIndex);

    // Add third annotation, thus incrementing the size.
    FrecencyAnnotation a3 = new FrecencyAnnotation("word3", queue,
        bucketSizes.length);
    assertEquals(a1, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(a3, queue.annotations[2]);
    assertEquals(3, queue.size);
    assertEquals(0, queue.startIndex);

    // Update second annotation, thus incrementing the start index, but not the size.
    a2.update();
    assertEquals(a2, queue.annotations[0]);
    assertEquals(a2, queue.annotations[1]);
    assertEquals(a3, queue.annotations[2]);
    assertEquals(3, queue.size);
    assertEquals(1, queue.startIndex);
  }

  private void assertBucketValues(FrecencyAnnotation annotation,
      int... bucketValues) {
    assertEquals(annotation.bucketCounts.length, bucketValues.length);
    for (int i = 0; i < bucketValues.length; ++i) {
      assertEquals(bucketValues[i], annotation.bucketCounts[i]);
    }
  }
}
