package org.ss12.wordprediction.newcore.annotations;

import org.ss12.wordprediction.newcore.AnnotatedWord;

/**
 * Annotates a word with its frequency and the last time it was used.
 * 
 * @author Michael Parker
 */
public class FrecencyAnnotation extends AnnotatedWord {
  int totalFrequency;
  final FrecencyAnnotationQueue queue;
  final int bucketCounts[];

  protected FrecencyAnnotation(String word, FrecencyAnnotationQueue queue,
      int numBuckets) {
    super(word);

    this.totalFrequency = 0;
    this.queue = queue;
    this.bucketCounts = new int[numBuckets];

    update();
  }

  protected void update() {
    ++totalFrequency;
    ++bucketCounts[0];

    queue.add(this);
  }

  void moveToBucket(int toBucket) {
    --bucketCounts[toBucket - 1];
    if (toBucket < bucketCounts.length) {
      ++bucketCounts[toBucket];
    }
  }
}
