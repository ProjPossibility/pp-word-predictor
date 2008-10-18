package org.ss12.wordprediction.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.ss12.wordprediction.Prediction;

/**
 * Writes the suggestions for a word to some {@link HttpServletResponse}.
 */
public interface ServiceOutputWriter {
  /**
   * @return the {@code content-type} of the HTTP GET response
   */
  public String getContentType();

  /**
   * Writes the given suggestions to the given writer.
   * 
   * @param prediction
   *          the prediction request and corresponding suggestions
   * @param out
   *          the output stream to write the suggestions to
   * @throws IOException
   *           if an error occurs while writing the response
   * @throws ServletException
   *           if any other runtime error occurs
   */
  public void writeSuggestions(Prediction prediction, Writer out)
      throws IOException, ServletException;
}
