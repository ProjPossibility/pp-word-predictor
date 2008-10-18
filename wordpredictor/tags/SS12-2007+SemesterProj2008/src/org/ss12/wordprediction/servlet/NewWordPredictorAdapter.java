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
    if ((context == null) || (context.length == 0)) {
      return new String[0];
    }
    return wp.getPredictions(PredictionRequest.from(context[context.length - 1],
        numOfSuggestions)).toArray(new String[0]);
  }

  public int learn(String buffer) {
    // silently do nothing
    return 0;
  }
  
  public String wordify(String word) {
    word.trim();
    for (int i = 0; i < word.length() - 1; i++) {
      char c = word.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '\'')
          || (c == '-')) {
        continue;
      } else
        return "";
    }
    if (word.length() > 0) {
      char c = word.charAt(word.length() - 1);
      if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
          || (c == '\'') || (c == '-'))) {
        word = word.substring(0, word.length() - 1);
      }
    }
    return word;
  }

  public String[] processString(String input) {
    input=input.toLowerCase();
    int startPoint=Math.max(input.lastIndexOf('.'), Math.max(input.lastIndexOf('?'),input.lastIndexOf('!')))+1;
    if(startPoint>=input.length())
      return new String[]{""};
    else if(startPoint>0)
      input = input.substring(startPoint);
    String temp[] = new String[3];
    int[] ind = new int[3];
    ind[0] = input.lastIndexOf(' ');
    temp[0] = input.substring(ind[0]+1);
    int i;
    for(i=1; ind[i-1]>0 && i<3; i++){
      ind[i] = input.lastIndexOf(' ', ind[i-1]-1);
      temp[i] = input.substring(ind[i]+1, ind[i-1]);
    }
    String word[] = new String[i];
    for(int j=0;j<i;j++){
      temp[i-j-1] = wordify(temp[i-j-1]);
      word[j] = temp[i-j-1].trim();
    }
    return word;
  }
}
