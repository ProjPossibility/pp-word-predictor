package org.ss12.wordprediction.newcore;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Unit test for {@link CustomLexicon} tests.
 */
public abstract class CustomLexiconTest extends TestCase {
  protected final MockAnnotationFactory annotationFactory =
      new MockAnnotationFactory();
  protected CustomLexicon<MockAnnotation> customLexicon;

  @Override
  public void setUp() {
    customLexicon = getCustomLexicon();
  }

  @Override
  protected void tearDown() {
    customLexicon.close();
  }

  /**
   * A subclass implements this method, returning the implementation of
   * {@link CustomLexicon} to test.
   * 
   * @return the lexicon to test
   */
  protected abstract CustomLexicon<MockAnnotation> getCustomLexicon();

  public void testAddUnigrams() {
    MockAnnotation annotation1 = new MockAnnotation("a");
    MockAnnotation annotation2 = new MockAnnotation("aaa");
    MockAnnotation annotation3 = new MockAnnotation("b");

    // Assert that custom lexicon is empty at start.
    assertEquals(Collections.emptyList(), toList(customLexicon.getUnigrams("")));
    assertEquals(Collections.emptyList(),
        toList(customLexicon.getUnigrams("a")));

    // Assert that can find first annotated word given "" or "a".
    customLexicon.addUnigram(annotation1.getWord());
    assertEquals(Arrays.asList(annotation1), toList(customLexicon
        .getUnigrams("")));
    assertEquals(Arrays.asList(annotation1), toList(customLexicon
        .getUnigrams("a")));

    // Assert that can find first two annotated words given "" or "a".
    customLexicon.addUnigram(annotation2.getWord());
    assertEquals(Arrays.asList(annotation1, annotation2), toList(customLexicon
        .getUnigrams("")));
    assertEquals(Arrays.asList(annotation1, annotation2), toList(customLexicon
        .getUnigrams("a")));

    // Assert that can find all annotated words given "".
    customLexicon.addUnigram(annotation3.getWord());
    assertEquals(Arrays.asList(annotation1, annotation2, annotation3),
        toList(customLexicon.getUnigrams("")));
    // Assert that only find first two annotated words given "a".
    assertEquals(Arrays.asList(annotation1, annotation2), toList(customLexicon
        .getUnigrams("a")));
  }

  public void testUpdateUnigrams() {
    String word1 = "a";
    String word2 = "b";
    customLexicon.addUnigram(word1);
    customLexicon.addUnigram(word2);

    // Assert that both words have not been updated by default.
    List<MockAnnotation> annotations = toList(customLexicon.getUnigrams(""));
    assertEquals(2, annotations.size());
    assertAnnotation(annotations.get(0), word1, false);
    assertAnnotation(annotations.get(1), word2, false);

    // Add the first word again, and assert that its annotation was updated.
    customLexicon.addUnigram(word1);
    annotations = toList(customLexicon.getUnigrams(""));
    assertEquals(2, annotations.size());
    assertAnnotation(annotations.get(0), word1, true);
    assertAnnotation(annotations.get(1), word2, false);

    // Add the second word again, and assert that its annotation was updated.
    customLexicon.addUnigram(word2);
    annotations = toList(customLexicon.getUnigrams(""));
    assertEquals(2, annotations.size());
    assertAnnotation(annotations.get(0), word1, false);
    assertAnnotation(annotations.get(1), word2, true);
  }

  // Converts the given iterable of annotations to a list.
  private <T extends AnnotatedWord> List<T> toList(Iterable<T> annotations) {
    List<T> list = new LinkedList<T>();
    for (T annotation : annotations) {
      list.add(annotation);
    }
    return list;
  }

  // Assert that the annotation has the given word, and whether it was updated
  // if expected.
  private void assertAnnotation(MockAnnotation annotation, String word,
      boolean updated) {
    assertEquals(word, annotation.getWord());
    assertEquals(updated, annotation.resetUpdate());
  }
}
