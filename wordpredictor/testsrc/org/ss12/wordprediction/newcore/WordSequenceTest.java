package org.ss12.wordprediction.newcore;

import junit.framework.TestCase;

/**
 * Unit tests for {@link WordSequence}.
 * 
 * @author Michael Parker
 */
public class WordSequenceTest extends TestCase {
  public void testCompareTo() {
    WordSequence ws1 = new WordSequence("a");
    WordSequence ws2 = new WordSequence("aaa");
    WordSequence ws3 = new WordSequence("aaa", "aa");
    WordSequence ws4 = new WordSequence("zz");

    assertTrue(ws1.compareTo(ws2) < 0);
    assertTrue(ws2.compareTo(ws3) < 0);
    assertTrue(ws3.compareTo(ws4) < 0);
  }

  public void testUpperBound() {
    assertEquals("d", WordSequence.getUpperBound("c"));
    assertEquals("z", WordSequence.getUpperBound("y"));
    assertEquals("zbd", WordSequence.getUpperBound("zbc"));
    assertEquals("zbz", WordSequence.getUpperBound("zby"));
    assertEquals("zz", WordSequence.getUpperBound("zyz"));
  }

  public void testUpperBoundToNull() {
    assertNull(WordSequence.getUpperBound("z"));
    assertNull(WordSequence.getUpperBound("zzz"));
  }

  public void testNextSequence() {
    assertEquals(new WordSequence("d"), WordSequence
        .getNextSequence(new WordSequence("c")));
    assertEquals(new WordSequence("z"), WordSequence
        .getNextSequence(new WordSequence("y")));
    assertEquals(new WordSequence("zb", "d"), WordSequence
        .getNextSequence(new WordSequence("zb", "c")));
    assertEquals(new WordSequence("zb", "z"), WordSequence
        .getNextSequence(new WordSequence("zb", "y")));
    assertEquals(new WordSequence("zz"), WordSequence
        .getNextSequence(new WordSequence("zy", "z")));
  }

  public void testNextSequenceToNull() {
    assertNull(WordSequence.getNextSequence(new WordSequence("zz")));
    assertNull(WordSequence.getNextSequence(new WordSequence("zz", "zz")));
  }
}
