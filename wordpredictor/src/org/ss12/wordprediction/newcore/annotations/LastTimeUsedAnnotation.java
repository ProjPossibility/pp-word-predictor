package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import org.ss12.wordprediction.newcore.AnnotatedWord;
import org.ss12.wordprediction.newcore.WordPredictorUtil;

/**
 * Annotates a word with its last time used.
 * 
 * @author Michael Parker
 */
public class LastTimeUsedAnnotation extends AnnotatedWord {
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

  LastTimeUsedAnnotation(String word, long lastTimeUsed) {
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof LastTimeUsedAnnotation) {
      LastTimeUsedAnnotation annotation = (LastTimeUsedAnnotation) obj;
      return (getWord().equals(annotation.getWord()) &&
          (lastTimeUsed == annotation.lastTimeUsed));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return WordPredictorUtil.hashCode(getWord(), lastTimeUsed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("word=").append(getWord()).append(", ");
    sb.append("time=").append(lastTimeUsed);
    return sb.toString();
  }
}
