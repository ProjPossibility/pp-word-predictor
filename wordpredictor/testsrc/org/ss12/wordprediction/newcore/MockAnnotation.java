package org.ss12.wordprediction.newcore;

import java.util.Comparator;

/**
 * An {@link AnnotatedWord} implementation with no metadata.
 * 
 * @author Michael Parker
 */
class MockAnnotation extends AnnotatedWord {
  static final Comparator<MockAnnotation> COMPARATOR =
      new Comparator<MockAnnotation>() {
    public int compare(MockAnnotation lhs, MockAnnotation rhs) {
      // All NullAnnotation instances have equal scores.
      return 0;
    }
  };

  boolean isUpdated;

  protected MockAnnotation(String word) {
    super(word);
    isUpdated = false;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof MockAnnotation) {
      MockAnnotation annotation = (MockAnnotation) obj;
      return (getWord().equals(annotation.getWord()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getWord().hashCode();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("word=").append(getWord()).append(", ");
    sb.append("updated=").append(isUpdated);
    return sb.toString();
  }

  boolean resetUpdate() {
    boolean wasUpdated = isUpdated;
    isUpdated = false;
    return wasUpdated;
  }

  protected void update() {
    isUpdated = true;
  }
}
