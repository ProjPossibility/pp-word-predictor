package org.ss12.wordprediction.newcore;

/**
 * Unit test for {@link TreeMapCustomLexicon}.
 * 
 * @author Michael Parker
 */
public class TreeMapCustomLexiconTest extends CustomLexiconTest {
  protected CustomLexicon<MockAnnotation> getCustomLexicon() {
    return new TreeMapCustomLexicon<MockAnnotation>(annotationFactory);
  }
}
