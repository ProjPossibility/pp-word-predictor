package org.ss12.wordprediction.newcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;
import org.ss12.wordprediction.newcore.annotations.LastTimeUsedAnnotationFactory;

/**
 * A benchmark for {@link Scorer} implementations.
 * 
 * @author Michael Parker
 */
public class ScorerBenchmark {
  /**
   * Assigns a descriptive name to an {@link AnnotationFactory} instance.
   */
  private static final class NameAnnotationPair {
    final String name;
    final AnnotationFactory<? extends AnnotatedWord> annotationFactory;

    NameAnnotationPair(String name,
        AnnotationFactory<? extends AnnotatedWord> annotationFactory) {
      this.name = name;
      this.annotationFactory = annotationFactory;
    }
  }

  /**
   * The benchmark results for a single {@link Scorer}.
   */
  private static final class BenchmarkResult {
    int totalCharactersSaved;
    long totalTimeElapsed;

    BenchmarkResult() {
      totalCharactersSaved = 0;
      totalTimeElapsed = 0;
    }

    public void increaseBy(int charactersSaved, long timeElapsed) {
      totalCharactersSaved += charactersSaved;
      totalTimeElapsed += timeElapsed;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("charactersSaved=").append(totalCharactersSaved).append(", ");
      sb.append("timeElapsed=").append(totalTimeElapsed);
      return sb.toString();
    }
  }

  /*
   * Creates a new WordPredictor that uses the given AnnotationFactory.
   */
  private static <T extends AnnotatedWord> ScoringWordPredictor<T> createWordPredictor(
      AnnotationFactory<T> annotationFactory) {
    // Create the backing data structures.
    Map<String, Integer> emptyMap = Collections.emptyMap();
    ImmutableLexicon defaultLexicon = TreeMapImmutableLexicon.fromMap(emptyMap);
    CustomLexicon<T> customLexicon = new TreeMapCustomLexicon<T>(
        annotationFactory);
    // Pass these backing data structures into the predictor.
    return new ScoringWordPredictor<T>(defaultLexicon, customLexicon,
        annotationFactory);
  }

  /*
   * Trains the word predictor using each file.
   */
  private static void trainWordPredictor(File[] trainingFiles,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    for (File trainingFile : trainingFiles) {
      trainWordPredictor(trainingFile, wordPredictor);
    }
  }

  /*
   * Trains the word predictor using the given file.
   */
  private static void trainWordPredictor(File trainingFile,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
    SentenceReader sentenceReader = new SentenceReader(wordPredictor);
    while (true) {
      String nextLine = reader.readLine();
      if (nextLine == null) {
        break;
      }

      sentenceReader.addText(nextLine);
      sentenceReader.flush();
    }
    reader.close();
  }

  /*
   * Reads the entire benchmark file into memory to avoid disk I/O later.
   */
  public static String[] readEntireFile(File benchmarkFile) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(benchmarkFile));
    while (true) {
      String nextLine = reader.readLine();
      if (nextLine == null) {
        break;
      }
      sb.append(nextLine);
      sb.append(' ');
    }

    String fileContents = sb.toString();
    List<String> fileWords = new LinkedList<String>();

    BreakIterator wordIterator = BreakIterator.getWordInstance();
    wordIterator.setText(fileContents);
    int start = wordIterator.first();
    int end = wordIterator.next();

    while (end != BreakIterator.DONE) {
      String word = fileContents.substring(start, end);
      if (Character.isLetter(word.charAt(0))) {
        fileWords.add(word);
      }
      start = end;
      end = wordIterator.next();
    }

    return fileWords.toArray(new String[0]);
  }

  /*
   * Benchmarks the word predictor using each file.
   */
  private static BenchmarkResult benchmarkWordPredictor(File[] benchmarkFiles,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    BenchmarkResult benchmarkResult = new BenchmarkResult();
    for (File benchmarkFile : benchmarkFiles) {
      benchmarkWordPredictor(benchmarkFile, wordPredictor, benchmarkResult);
    }
    return benchmarkResult;
  }

  /*
   * Benchmarks the word predictor using the given file.
   */
  private static void benchmarkWordPredictor(File benchmarkFile,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor,
      BenchmarkResult benchmarkResult) throws IOException {
    String[] words = readEntireFile(benchmarkFile);
    int charactersSaved = 0;
    String prevPrevWord = null;
    String prevWord = null;

    long startTime = System.currentTimeMillis();
    for (String word : words) {
      charactersSaved += tryPredict(prevPrevWord, prevWord, word, wordPredictor);
      prevPrevWord = prevWord;
      prevWord = word;
    }
    long timeElapsed = System.currentTimeMillis() - startTime;

    benchmarkResult.increaseBy(charactersSaved, timeElapsed);
  }

  /*
   * Tries to predict the given word, returning the number of characters saved.
   */
  private static int tryPredict(String prevPrevWord, String prevWord,
      String word, ScoringWordPredictor<? extends AnnotatedWord> wordPredictor) {
    final int wordLength = word.length();

    for (int charsTyped = 1; charsTyped < wordLength; ++charsTyped) {
      String incompleteWord = word.substring(0, charsTyped);
      PredictionRequest request = PredictionRequest.from(incompleteWord,
          prevWord, prevPrevWord, 5);
      List<String> suggestions = wordPredictor.getPredictions(request);
      if (suggestions.contains(word)) {
        // Word was predicted successfully.
        return (wordLength - charsTyped);
      }
    }
    return 0;
  }

  public static void main(String[] args) throws IOException {
    // Name all AnnotationFactory instances to benchmark.
    NameAnnotationPair[] nameAnnotationPairs = new NameAnnotationPair[] {
        new NameAnnotationPair("frequency", new FrequencyAnnotationFactory()),
        new NameAnnotationPair("timeLastUsed",
            new LastTimeUsedAnnotationFactory())
    };

    // Specify the files used to train the word predictor.
    File[] trainingFiles = new File[] {

    };
    // Specify the files used to benchmark the word predictor.
    File[] benchmarkFiles = new File[] {

    };

    for (NameAnnotationPair nameAnnotationPair : nameAnnotationPairs) {
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor =
          createWordPredictor(nameAnnotationPair.annotationFactory);
      System.out.println(nameAnnotationPair.name + ":");
      System.out.println("  training...");
      trainWordPredictor(trainingFiles, wordPredictor);
      System.out.println("  benchmarking...");
      BenchmarkResult benchmarkResult = benchmarkWordPredictor(benchmarkFiles,
          wordPredictor);
      System.out.println("  " + benchmarkResult);
    }
  }
}
