package org.ss12.wordprediction.newcore;

/**
 * Accepts a sequence of tokens from sentences and adds all found unigrams,
 * bigrams, and trigrams to a backing {@link ScoringWordPredictor}.
 * 
 * @author Michael Parker
 */
public class SentenceReader {
  private final ScoringWordPredictor<?> wp;

  private WordReader wordReader;
  boolean endSentence;

  /**
   * Creates a new word reader, and adds unigrams, bigrams, and trigrams to the
   * given prediction model.
   * 
   * @param wp prediction model to populate
   */
  public SentenceReader(ScoringWordPredictor<? extends AnnotatedWord> wp) {
    this.wp = wp;
    resetWordReader();
  }

  private void resetWordReader() {
    wordReader = wp.getWordReader();
    endSentence = false;
  }

  /**
   * Extracts unigrams, bigrams, and trigrams from the given sequence of tokens
   * of sentences and updates the word predictor.
   * 
   * @param sentenceTokens the sentence tokens
   */
  public void nextTokens(String[] sentenceTokens) {
    for (String word : sentenceTokens) {
      word = word.toLowerCase().trim();

      if (!isValidWord(word)) {
        resetWordReader();
        continue;
      }
      word = stripPunctuation(word);
      if (word.length() == 0) {
        resetWordReader();
        continue;
      }

      wordReader.nextWord(word);
      if (endSentence) {
        resetWordReader();
      }
    }
  }

  private boolean isValidCharacter(char c) {
    return (Character.isLetter(c) || (c == '\'') || (c == '-'));
  }

  private boolean isValidWord(String word) {
    for (int i = 0; i < word.length() - 1; i++) {
      char c = word.charAt(i);
      if (!isValidCharacter(c)) {
        return false;
      }
    }
    return true;
  }

  private String stripPunctuation(String word) {
    if (word.length() > 0) {
      char c = word.charAt(word.length() - 1);
      if (!isValidCharacter(c)) {
        endSentence = true;
        return word.substring(0, word.length() - 1);
      }
    }
    return word;
  }
}
