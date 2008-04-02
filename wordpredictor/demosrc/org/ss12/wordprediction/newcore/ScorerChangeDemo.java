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
  public static void main(String[] args) throws Exception {
    Map<String, Integer> emptyMap = Collections.emptyMap();
    ImmutableLexicon defaultLexicon = TreeMapImmutableLexicon.fromMap(emptyMap);

    AnnotationFactory<FrequencyAnnotation> annotationFactory =
        new FrequencyAnnotationFactory();
    CustomLexicon<FrequencyAnnotation> customLexicon =
        new TreeMapCustomLexicon<FrequencyAnnotation>(annotationFactory);
    WordPredictor wordPredictor = new ScoringWordPredictor<FrequencyAnnotation>(
        defaultLexicon, customLexicon, annotationFactory);

    customLexicon.addUnigram("bar");
    Thread.sleep(10);
    customLexicon.addUnigram("bar");
    Thread.sleep(10);
    customLexicon.addUnigram("baz");

    showPredictions(wordPredictor);
  }

  private static void showPredictions(WordPredictor wordPredictor) {
    PredictionRequest request = PredictionRequest.from("b", 2);
    System.out.println("incomplete word = " + request.getIncompleteWord());
    List<String> predictions = wordPredictor.getPredictions(request);
    System.out.println("predictions = " + predictions);
  }
}
