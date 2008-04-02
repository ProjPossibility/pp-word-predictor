package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotatedWord;

/**
 * Annotates a word with its last time used.
 * 
 * @author Michael Parker
 */
class LastTimeUsedAnnotation extends AnnotatedWord {
  /**
   * A comparator that sorts {@link LastTimeUsedAnnotation} instances by their
   * associated last time used.
   */
  public static final Comparator<LastTimeUsedAnnotation> COMPARATOR =
      new Comparator<LastTimeUsedAnnotation>() {
    public int compare(LastTimeUsedAnnotation lhs, LastTimeUsedAnnotation rhs) {
      if (lhs.lastTimeUsed == rhs.lastTimeUsed) {
        return 0;
      }
      return (lhs.lastTimeUsed < rhs.lastTimeUsed) ? -1 : 1;
    }
  };

  long lastTimeUsed;

  public LastTimeUsedAnnotation(String word) {
    super(word);
    lastTimeUsed = System.currentTimeMillis();
  }

  public LastTimeUsedAnnotation(LastTimeUsedAnnotation annotation) {
    super(annotation.getWord());
    lastTimeUsed = annotation.lastTimeUsed;
  }

  /**
   * @return the last time used, in milliseconds
   */
  long getLastTimeUsed() {
    return lastTimeUsed;
  }

  protected void update() {
    lastTimeUsed = System.currentTimeMillis();
  }
}
