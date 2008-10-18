package org.ss12.wordprediction.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ss12.wordprediction.Prediction;
import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.model.WordPredictor;
import org.ss12.wordprediction.servlet.WordPredictorHttpUtil.HttpGetParams;

/**
 * A servlet that returns word prediction suggestions in XML or JSON format.
 */
public class WordPredictorService extends HttpServlet {
  /**
   * The output format that can be specified by the HTTP GET parameter
   * {@code of}.
   */
  public static final class OutputFormat {
    /**
     * If the generated output should be XML.
     */
    public static final String XML = "xml";

    /**
     * If the generated output should be JSON.
     */
    public static final String JSON = "json";
  }

  /**
   * The HTTP GET parameters the service accepts.
   */
  public static final class ServiceHttpGetParams extends HttpGetParams {
    /**
     * The {@link OutputFormat} value to use, a required parameter.
     */
    public static final String OUTPUT_FORMAT = "of";
  }

  private final WordPredictor wp;
  private final Map<String, ServiceOutputWriter> writers;

  /**
   * This creates a new servlet.
   */
  public WordPredictorService() {
    WordLoader wl = new WordLoader(1);
    try {
      wl.loadDictionary(new File("resources/dictionaries/converted/plain.dat"));
      wl
          .loadFrequenciess(new File(
              "resources/dictionaries/converted/freq.dat"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.wp = new TreeMapWordPredictor(wl.getWords());

    this.writers = new HashMap<String, ServiceOutputWriter>();
    writers.put(OutputFormat.JSON, JsonOutputWriter.INSTANCE);
    writers.put(OutputFormat.XML, XmlOutputWriter.INSTANCE);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    ServiceOutputWriter outputWriter = writers.get(
        req.getParameter(ServiceHttpGetParams.OUTPUT_FORMAT));
    if (outputWriter == null) {
      writeUnknownFormatError();
      return;
    }

    Prediction prediction = WordPredictorHttpUtil.makePrediction(req, wp, 5);
    if (prediction == null) {
      writeMissingParamsError();
      return;
    }

    res.setContentType(outputWriter.getContentType());
    outputWriter.writeSuggestions(prediction, res.getWriter());
  }

  private void writeUnknownFormatError() {
    // TODO(mgp)
  }
  
  private void writeMissingParamsError() {
    // TODO(mgp)
  }
}
