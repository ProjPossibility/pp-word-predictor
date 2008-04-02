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
    LastTimeUsedAnnotation annotation = new LastTimeUsedAnnotation("a", -1L);
    assertEquals(-1L, annotation.getLastTimeUsed());
    
    long beforeUpdateTime = System.currentTimeMillis();
    annotation.update();
    long afterUpdateTime = System.currentTimeMillis();
    assertTrue(beforeUpdateTime <= annotation.getLastTimeUsed());
    assertTrue(annotation.getLastTimeUsed() <= afterUpdateTime);
  }
  
  public void testComparator() {
    LastTimeUsedAnnotation a1 = new LastTimeUsedAnnotation("a", 100L);
    LastTimeUsedAnnotation a2 = new LastTimeUsedAnnotation("b", 200L);
    LastTimeUsedAnnotation a3 = new LastTimeUsedAnnotation("c", 300L);
    
    Comparator<LastTimeUsedAnnotation> comparator = LastTimeUsedAnnotation.COMPARATOR;
    assertTrue(comparator.compare(a1, a2) < 0);
    assertTrue(comparator.compare(a2, a3) < 0);
  }
}
