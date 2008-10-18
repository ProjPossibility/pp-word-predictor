package org.ss12.wordprediction.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ss12.wordprediction.Prediction;
import org.ss12.wordprediction.PredictionRequest;

/**
 * A {@link ServiceOutputWriter} that writes suggestions in JSON.
 */
public class JsonOutputWriter implements ServiceOutputWriter {
  /**
   * The {@link JsonOutputWriter} instance.
   */
  public static final JsonOutputWriter INSTANCE = new JsonOutputWriter();

  // Do not allow instantiation, use the singleton.
  private JsonOutputWriter() {
  }

  public String getContentType() {
    return "application/json";
  }

  public void writeSuggestions(Prediction prediction, Writer out)
      throws IOException, ServletException {
    // Generate the JSON response.
    JSONObject json = new JSONObject();
    try {
      PredictionRequest request = prediction.getRequest();
      json.put(JsonConstants.INCOMPLETE_KEY, request.getIncompleteWord());
      json.put(JsonConstants.CONTEXT_KEY, new JSONArray(request
          .getPrecedingWords()));
      json.put(JsonConstants.SUGGESTIONS_KEY, new JSONArray(prediction
          .getSuggestions()));
    } catch (JSONException e) {
      throw new ServletException(e);
    }

    // Write the response.
    out.write(json.toString());
  }
}
