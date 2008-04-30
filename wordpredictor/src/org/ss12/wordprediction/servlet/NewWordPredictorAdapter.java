package org.ss12.wordprediction.servlet;

import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.model.WordPredictor;

/**
 * Adapts interface to {@link org.ss12.wordprediction.newcore.WordPredictor} to
 * interface {@link org.ss12.wordprediction.model.WordPredictor}.
 * 
 * @author Michael Parker
 */
public class NewWordPredictorAdapter implements WordPredictor {
  private final org.ss12.wordprediction.newcore.WordPredictor wp;
  
  public NewWordPredictorAdapter(org.ss12.wordprediction.newcore.WordPredictor wp) {
    this.wp = wp;
  }

  public void addBigram(String s1, String s2) {
    // silently do nothing
  }

  public void addTrigram(String s1, String s2, String s3) {
    // silently do nothing
  }

  public void addUnigram(String s1) {
    // silently do nothing
  }

  public void cleanup() {
    // silently do nothing
  }

  public String[] getSuggestionsFromDic(String begin_seq, int numOfSuggestions) {
    return new String[0];
  }

  public String[] getSuggestionsGramBased(String[] context, int numOfSuggestions) {
    if (context.length == 0) {
      return new String[0];
    }
    return wp.getPredictions(PredictionRequest.from(context[context.length - 1],
        numOfSuggestions)).toArray(new String[0]);
  }

  public int learn(String buffer) {
    // silently do nothing
    return 0;
  }

  public String[] processString(String input) {
    // silently do nothing
    return null;
  }
}
