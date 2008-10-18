package org.ss12.wordprediction.newcore;

import java.util.List;

import org.ss12.wordprediction.PredictionRequest;

/**
 * An interface for any source of word predictions.
 * 
 * @author Michael Parker
 */
public interface WordPredictor {
  /**
   * Returns the predictions for the given request, which contains the
   * incomplete word and any optional context.
   * 
   * @param request the prediction request
   * @return a list of predictions for the incomplete word
   */
  public List<String> getPredictions(PredictionRequest request);
}
