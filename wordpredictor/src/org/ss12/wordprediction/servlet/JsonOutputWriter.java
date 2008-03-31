package org.ss12.wordprediction.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

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

  private JsonOutputWriter() {
  }

  public String getGetParamValue() {
    return "json";
  }

  public void writeSuggestions(Prediction prediction, HttpServletResponse resp)
      throws IOException, ServletException {
    System.err.println("prediction: " + prediction);
    
    // Generate the JSON response.
    JSONObject json = new JSONObject();
    try {
      PredictionRequest request = prediction.getRequest();
      json.put("incomplete", request.getIncompleteWord());
      json.put("context", new JSONArray(request.getPrecedingWords()));
      json.put("suggestions", new JSONArray(prediction.getSuggestions()));
    } catch (JSONException e) {
      throw new ServletException(e);
    }

    // Write the response.
    resp.setContentType("applicaton/json");
    PrintWriter writer = resp.getWriter();
    writer.write(json.toString());
  }
}
