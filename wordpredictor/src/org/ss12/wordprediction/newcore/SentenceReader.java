package org.ss12.wordprediction.newcore;

import java.text.BreakIterator;

/**
 * Accepts a sequence of tokens from sentences and adds all found unigrams,
 * bigrams, and trigrams to a backing {@link ScoringWordPredictor}.
 * 
 * @author Michael Parker
 */
public class SentenceReader {
  private final ScoringWordPredictor<?> wp;
  private StringBuilder sb;

  /**
   * Creates a new word reader, and adds unigrams, bigrams, and trigrams to the
   * given prediction model.
   * 
   * @param wp prediction model to populate
   */
  public SentenceReader(ScoringWordPredictor<? extends AnnotatedWord> wp) {
    this.wp = wp;
    sb = new StringBuilder();
  }

  /**
   * Extracts unigrams, bigrams, and trigrams from the given text and updates
   * the word predictor. The text may be one or more complete sentences, or even
   * only a sentence fragment.
   * 
   * @param text the sentence
   */
  public void addText(String text) {
    sb.append(text);
    String allText = sb.toString();

    BreakIterator sentenceIterator = BreakIterator.getSentenceInstance();
    sentenceIterator.setText(allText);
    int start = sentenceIterator.first();
    int end = sentenceIterator.next();

    // Process all complete sentences.
    while (end < allText.length()) {
      String sentence = allText.substring(start, end);
      addSentence(sentence);
      start = end;
      end = sentenceIterator.next();
    }

    // Keep buffered any remaining sentence fragment.
    sb.delete(0, start);
  }

  /**
   * Clear the internal state of this reader, meaning any sentence fragment that
   * may be buffered will be considered a complete sentence and the word
   * predictor will be updated accordingly.
   */
  public void flush() {
    addSentence(sb.toString());
    sb = new StringBuilder();
  }

  private void addSentence(String sentence) {
    WordReader wordReader = wp.getWordReader();

    BreakIterator wordIterator = BreakIterator.getWordInstance();
    wordIterator.setText(sentence);
    int start = wordIterator.first();
    int end = wordIterator.next();

    while (end != BreakIterator.DONE) {
      String word = sentence.substring(start, end);
      if (Character.isLetter(word.charAt(0))) {
        wordReader.nextWord(word);
      }
      start = end;
      end = wordIterator.next();
    }
  }
}
