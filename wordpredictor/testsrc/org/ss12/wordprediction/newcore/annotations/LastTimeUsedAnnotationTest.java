package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import junit.framework.TestCase;

/**
 * Unit test for {@link LastTimeUsedAnnotation}.
 * 
 * @author Michael Parker
 */
public class LastTimeUsedAnnotationTest extends TestCase {
  public void testUpdate() {
    LastTimeUsedClock clock = new MockLastTimeUsedClock();
    LastTimeUsedAnnotation annotation = new LastTimeUsedAnnotation("a", clock);
    assertEquals(0L, annotation.getLastTimeUsed());

    annotation.update();
    assertEquals(1L, annotation.getLastTimeUsed());
  }
  
  public void testComparator() {
    LastTimeUsedClock clock = new MockLastTimeUsedClock();
    LastTimeUsedAnnotation a1 = new LastTimeUsedAnnotation("a", clock);
    LastTimeUsedAnnotation a2 = new LastTimeUsedAnnotation("b", clock);
    LastTimeUsedAnnotation a3 = new LastTimeUsedAnnotation("c", clock);

    Comparator<LastTimeUsedAnnotation> comparator = LastTimeUsedAnnotation.COMPARATOR;
    assertTrue(comparator.compare(a1, a2) < 0);
    assertTrue(comparator.compare(a2, a3) < 0);
  }
}
