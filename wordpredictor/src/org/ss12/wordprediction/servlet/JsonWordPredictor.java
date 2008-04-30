package org.ss12.wordprediction.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.newcore.WordPredictor;
import org.ss12.wordprediction.servlet.WordPredictorHttpUtil.HttpGetParams;

/**
 * A {@link WordPredictor} implementation that gets predictions from some word
 * predictor service returning JSON.
 * 
 * @author Michael Parker
 */
public class JsonWordPredictor implements WordPredictor {
  private final URI predictorUri;

  /**
   * Constructs a new predictor.
   * 
   * @param predictorUri the URL of the predictor service
   */
  public JsonWordPredictor(URI predictorUri) {
    this.predictorUri = predictorUri;
  }

  private URL makeRequestUrl(PredictionRequest request)
      throws MalformedURLException {
    StringBuilder sb = new StringBuilder(predictorUri.toString());
    sb.append("?of=json");
    appendKeyValuePair(sb, HttpGetParams.INCOMPLETE_WORD, request
        .getIncompleteWord());
    if (request.getPrevWord() != null) {
      appendKeyValuePair(sb, HttpGetParams.FIRST_PREV_WORD, request
          .getPrevWord());
    }
    if (request.getPrevPrevWord() != null) {
      appendKeyValuePair(sb, HttpGetParams.SECOND_PREV_WORD, request
          .getPrevPrevWord());
    }
    return new URL(sb.toString());
  }

  private void appendKeyValuePair(StringBuilder sb, String key, String value) {
    sb.append('&').append(key).append('=').append(value);
  }

  public List<String> getPredictions(PredictionRequest request) {
    // TODO(mgp): add a PredictorException type?
    try {
      URL predictorUrl = makeRequestUrl(request);
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          predictorUrl.openStream()));

      // Find the last line, which will contain the JSON response
      String line = null;
      while (true) {
        String nextLine = reader.readLine();
        if (nextLine == null) {
          break;
        }
        line = nextLine;
      }
      reader.close();

      // Extract the JSONArray holding the suggestions.
      JSONObject json = new JSONObject(line);
      JSONArray jsonSuggestions = json
          .getJSONArray(JsonConstants.SUGGESTIONS_KEY);
      // Add all the suggestions to a list.
      final int numSuggestions = jsonSuggestions.length();
      List<String> suggestions = new ArrayList<String>(numSuggestions);
      for (int i = 0; i < numSuggestions; ++i) {
        suggestions.add(jsonSuggestions.getString(i));
      }
      return suggestions;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
}