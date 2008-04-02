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
  static final Comparator<LastTimeUsedAnnotation> COMPARATOR =
      new Comparator<LastTimeUsedAnnotation>() {
    public int compare(LastTimeUsedAnnotation lhs, LastTimeUsedAnnotation rhs) {
      if (lhs.lastTimeUsed == rhs.lastTimeUsed) {
        return 0;
      }
      return (lhs.lastTimeUsed < rhs.lastTimeUsed) ? -1 : 1;
    }
  };

  long lastTimeUsed;

  LastTimeUsedAnnotation(String word) {
    this(word, System.currentTimeMillis());
  }

  LastTimeUsedAnnotation(LastTimeUsedAnnotation annotation) {
    this(annotation.getWord(), annotation.getLastTimeUsed());
  }

  public LastTimeUsedAnnotation(String word, long lastTimeUsed) {
    super(word);
    this.lastTimeUsed = lastTimeUsed;
  }

  /**
   * @return the last time this word was used, in milliseconds
   */
  long getLastTimeUsed() {
    return lastTimeUsed;
  }

  protected void update() {
    lastTimeUsed = System.currentTimeMillis();
  }
}
