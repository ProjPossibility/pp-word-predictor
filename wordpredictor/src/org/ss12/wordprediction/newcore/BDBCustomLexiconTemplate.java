package org.ss12.wordprediction.newcore;

import java.io.Serializable;

import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotation;

import com.sleepycat.bind.serial.SerialBinding;

/**
 * @author Brad Fol
 */
public class BDBCustomLexiconTemplate<T extends AnnotatedWord & Serializable>
    implements CustomLexicon<T> {
  private final Class<T> clazz;

  public BDBCustomLexiconTemplate(Class<T> clazz) {
    this.clazz = clazz;
  }

  public static void main(String[] args) {
    // Create a lexicon using FrequencyAnnotation.
    BDBCustomLexiconTemplate<FrequencyAnnotation> lexicon =
        new BDBCustomLexiconTemplate<FrequencyAnnotation>(FrequencyAnnotation.class);
  }

  public void open() {
    SerialBinding keyBinding = new SerialBinding(catalog, clazz);
  }

  public void addBigram(String firstWord, String secondWord)
      throws IllegalStateException {
    // TODO Auto-generated method stub

  }

  public void addTrigram(String firstWord, String secondWord, String thirdWord)
      throws IllegalStateException {
    // TODO Auto-generated method stub

  }

  public void addUnigram(String word) throws IllegalStateException {
    // TODO Auto-generated method stub

  }

  public void close() {
    // TODO Auto-generated method stub

  }

  public Iterable<T> getBigrams(String incompleteWord, String prevWord)
      throws IllegalStateException {
    // TODO Auto-generated method stub
    return null;
  }

  public Iterable<T> getTrigrams(String incompleteWord, String prevWord,
      String prevPrevWord) throws IllegalStateException {
    // TODO Auto-generated method stub
    return null;
  }

  public Iterable<T> getUnigrams(String incompleteWord)
      throws IllegalStateException {
    // TODO Auto-generated method stub
    return null;
  }

}
