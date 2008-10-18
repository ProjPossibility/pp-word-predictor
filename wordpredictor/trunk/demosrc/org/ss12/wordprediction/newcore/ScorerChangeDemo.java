package org.ss12.wordprediction.newcore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotation;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;

/**
 * @author Michael Parker
 */
public class ScorerChangeDemo {
  public static void main(String[] args) throws InterruptedException {
    AnnotationFactory<FrequencyAnnotation> annotationFactory = new FrequencyAnnotationFactory();

    WordPredictor wordPredictor = createPredictor(annotationFactory);
    showPredictions(wordPredictor);
  }

  private static <T extends AnnotatedWord> WordPredictor createPredictor(
      AnnotationFactory<T> annotationFactory) throws InterruptedException {
    // Create the backing data structures.
    Map<String, Integer> emptyMap = Collections.emptyMap();
    ImmutableLexicon defaultLexicon = TreeMapImmutableLexicon.fromMap(emptyMap);
    CustomLexicon<T> customLexicon = new TreeMapCustomLexicon<T>(
        annotationFactory);
    // Pass these backing data structures into the predictor.
    WordPredictor wordPredictor = new ScoringWordPredictor<T>(defaultLexicon,
        customLexicon, annotationFactory);

    // Populate predictor with two unigrams.
    customLexicon.addUnigram("bar");
    customLexicon.addUnigram("bar");
    Thread.sleep(10);
    customLexicon.addUnigram("baz");

    return wordPredictor;
  }

  private static void showPredictions(WordPredictor wordPredictor) {
    PredictionRequest request = PredictionRequest.from("b", 2);
    System.out.println("incomplete word = " + request.getIncompleteWord());
    List<String> predictions = wordPredictor.getPredictions(request);
    System.out.println("predictions = " + predictions);
  }
}
