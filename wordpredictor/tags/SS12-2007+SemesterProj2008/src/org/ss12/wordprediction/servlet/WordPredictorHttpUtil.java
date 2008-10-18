package org.ss12.wordprediction.servlet;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.ss12.wordprediction.Prediction;
import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.model.WordPredictor;

/**
 * Static utility methods for web-based word predictor applications.
 */
public class WordPredictorHttpUtil {
  // Only static utility methods, so do not allow instantiation.
  private WordPredictorHttpUtil() {
  }

  /**
   * Basic HTTP GET parameters for any word predictor application.
   * 
   * @author Michael Parker
   */
  public static class HttpGetParams {
    /**
     * The incomplete word to provide suggestions for, a required parameter.
     */
    public static final String INCOMPLETE_WORD = "currWord";

    /**
     * The first word preceding the incomplete word, an optional parameter.
     */
    public static final String FIRST_PREV_WORD = "prevWord";

    /**
     * The second word preceding the incomplete word, an optional parameter.
     */
    public static final String SECOND_PREV_WORD = "prevPrevWord";
  }

  public static Prediction makePrediction(HttpServletRequest req,
      WordPredictor wp, int numSuggestions) {
    // Generate the suggestions.
    String incompleteWord = req.getParameter(HttpGetParams.INCOMPLETE_WORD);
    if (incompleteWord == null) {
      // TODO(mgp): throw an IllegalArgumentException instead?
      return null;
    }
    String[] suggestions = wp.getSuggestionsFromDic(incompleteWord, 5);

    // Build the Prediction response.
    String firstPrevWord = req.getParameter(HttpGetParams.FIRST_PREV_WORD);
    String secondPrevWord = req.getParameter(HttpGetParams.SECOND_PREV_WORD);
    PredictionRequest predictionRequest = PredictionRequest.from(
        incompleteWord, firstPrevWord, secondPrevWord, numSuggestions);
    return new Prediction(predictionRequest, Arrays.asList(suggestions));
  }
}
