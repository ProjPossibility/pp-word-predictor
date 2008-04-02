package org.ss12.wordprediction.newcore.annotations;

import java.util.Comparator;

import junit.framework.TestCase;

/**
 * Unit test for {@link FrequencyAnnotation}.
 * 
 * @author Michael Parker
 */
public class FrequencyAnnotationTest extends TestCase {
  public void testUpdate() {
    FrequencyAnnotation annotation = new FrequencyAnnotation("a");
    assertEquals(1, annotation.getFrequency());
    annotation.update();
    assertEquals(2, annotation.getFrequency());
  }

  public void testComparator() {
    FrequencyAnnotation a1 = new FrequencyAnnotation("a", 5);
    FrequencyAnnotation a2 = new FrequencyAnnotation("b", 10);
    FrequencyAnnotation a3 = new FrequencyAnnotation("c", 20);
    
    Comparator<FrequencyAnnotation> comparator = FrequencyAnnotation.COMPARATOR;
    assertTrue(comparator.compare(a1, a2) < 0);
    assertTrue(comparator.compare(a2, a3) < 0);
  }
}
