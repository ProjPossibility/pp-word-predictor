package org.ss12.wordprediction.newcore;

/**
 * A base class for all words annotated with metadata; a {@link Scorer}
 * implementation uses this metadata to determine how suitable the word would be
 * as a suggestion.
 * 
 * @author Michael Parker
 */
public abstract class AnnotatedWord {
  // Not final to allow the subclass to assign it upon deserialization.
  protected String word;

  /**
   * Constructor to be called by subclasses.
   * 
   * @param word the word to annotate with metadata
   */
  protected AnnotatedWord(String word) {
    this.word = word;
  }

  /**
   * @return the annotated word
   */
  public final String getWord() {
    return word;
  }

  /**
   * This method is invoked when the word has just been used, as a consequence
   * of {@link CustomLexicon#addUnigram(String)},
   * {@link CustomLexicon#addBigram(String, String)}, or
   * {@link CustomLexicon#addTrigram(String, String, String)} being invoked.
   */
  protected abstract void update();
}
