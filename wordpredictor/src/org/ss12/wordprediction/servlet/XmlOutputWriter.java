package org.ss12.wordprediction.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.ss12.wordprediction.Prediction;
import org.ss12.wordprediction.PredictionRequest;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * A {@link ServiceOutputWriter} that writes suggestions in XML.
 */
public class XmlOutputWriter implements ServiceOutputWriter {
  public static final XmlOutputWriter INSTANCE = new XmlOutputWriter();

  // Do not allow instantiation, use the singleton.
  private XmlOutputWriter() {
  }

  public String getGetParamValue() {
    return "xml";
  }

  private void writeIncompleteWord(ContentHandler contentHandler,
      AttributesImpl attributes, String incompleteWord) throws SAXException {
    contentHandler.startElement("", "", "incompleteWord", attributes);
    contentHandler.characters(incompleteWord.toCharArray(), 0, incompleteWord
        .length());
    contentHandler.endElement("", "", "incompleteWord");
  }

  private void writeContext(ContentHandler contentHandler,
      AttributesImpl attributes, List<String> prevWords) throws SAXException {
    int index = 0;
    contentHandler.startElement("", "", "context", attributes);

    for (String prevWord : prevWords) {
      attributes.addAttribute("", "", "index", "CDATA", String.valueOf(index));
      contentHandler.startElement("", "", "prevWord", attributes);
      contentHandler.characters(prevWord.toCharArray(), 0, prevWord.length());
      contentHandler.endElement("", "", "prevWord");
      attributes.clear();
      ++index;
    }

    contentHandler.endElement("", "", "context");
  }

  private void writeSuggestions(ContentHandler contentHandler,
      AttributesImpl attributes, List<String> suggestions) throws SAXException {
    int index = 1;
    contentHandler.startElement("", "", "suggestions", attributes);

    for (String suggestion : suggestions) {
      attributes.addAttribute("", "", "rank", "CDATA", String.valueOf(index));
      contentHandler.startElement("", "", "suggestion", attributes);
      contentHandler.characters(suggestion.toCharArray(), 0, suggestion
          .length());
      contentHandler.endElement("", "", "suggestion");
      attributes.clear();
      ++index;
    }

    contentHandler.endElement("", "", "suggestions");
  }

  public void writeSuggestions(Prediction prediction, HttpServletResponse resp)
      throws IOException, ServletException {
    resp.setContentType("text/xml");

    OutputFormat outputFormat = new OutputFormat("XML", "ISO-8859-1", true);
    outputFormat.setIndenting(true);

    XMLSerializer serializer = new XMLSerializer(resp.getWriter(), outputFormat);
    ContentHandler contentHandler = serializer.asContentHandler();
    try {
      AttributesImpl attributes = new AttributesImpl();
      contentHandler.startDocument();
      contentHandler.startElement("", "", "prediction", attributes);
      PredictionRequest predictionRequest = prediction.getRequest();
      writeIncompleteWord(contentHandler, attributes, predictionRequest
          .getIncompleteWord());
      writeContext(contentHandler, attributes, predictionRequest
          .getPrecedingWords());
      writeSuggestions(contentHandler, attributes, prediction.getSuggestions());
      contentHandler.endElement("", "", "prediction");
      contentHandler.endDocument();
    } catch (SAXException e) {
      throw new ServletException(e);
    }
  }
}
