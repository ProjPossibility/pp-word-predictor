package org.ss12.wordprediction.servlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.newcore.WordPredictor;

/**
 * Driver program for {@link JsonWordPredictor}.
 */
public class JsonWordPredictorMain {
  public static void main(String[] args) throws URISyntaxException {
    WordPredictor predictor = new JsonWordPredictor(new URI(
        "http://localhost:8080/predictservice"));
    List<String> predictions =
        predictor.getPredictions(PredictionRequest.from("th", 5));
    System.out.println("predictions = " + predictions);
  }
}
