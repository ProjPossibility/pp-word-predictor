package org.ss12.wordprediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link PredictionRequest} and its corresponding suggestions.
 * 
 * @author Michael Parker
 */
public final class Prediction {
  private final PredictionRequest request;
  private final List<String> suggestions;

  /**
   * Creates a new {@link Prediction} instance that bundles together a
   * {@link Prediction} together with its corresponding suggestions.
   * 
   * @param request the prediction request
   * @param suggestions the suggestions in reply to the prediction request
   */
  public Prediction(PredictionRequest request, List<String> suggestions) {
    this.request = request;
    if (suggestions.isEmpty()) {
      this.suggestions = Collections.emptyList();
    } else {
      this.suggestions = Collections.unmodifiableList(new ArrayList<String>(
          suggestions));
    }
  }

  /**
   * @return the {@link PredictionRequest} that the suggestions are for
   */
  public PredictionRequest getRequest() {
    return request;
  }

  /**
   * @return the immutable list of suggestions for the incomplete word
   */
  public List<String> getSuggestions() {
    return suggestions;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("request={").append(request.toString()).append("}, ");
    sb.append("suggestions=").append(suggestions.toString());
    return sb.toString();
  }
}
