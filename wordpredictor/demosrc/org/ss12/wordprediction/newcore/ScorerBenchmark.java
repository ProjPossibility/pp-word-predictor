package org.ss12.wordprediction.newcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ss12.wordprediction.PredictionRequest;

/**
 * A benchmark for {@link Scorer} implementations.
 * 
 * @author Michael Parker
 */
public class ScorerBenchmark {
  /**
   * The benchmark results for a single {@link Scorer}.
   */
  public static final class BenchmarkResult {
    private final String name;
    int totalCharactersSaved;
    long totalTimeElapsed;

    BenchmarkResult(String name) {
      this.name = name;
      totalCharactersSaved = 0;
      totalTimeElapsed = 0;
    }

    void increaseBy(int charactersSaved, long timeElapsed) {
      totalCharactersSaved += charactersSaved;
      totalTimeElapsed += timeElapsed;
    }

    /**
     * @return the descriptive name of this result
     */
    public String getName() {
      return name;
    }

    /**
     * @return the total number of characters saved
     */
    public long getTotalCharactersSaved() {
      return totalCharactersSaved;
    }

    /**
     * @return the total time elapsed
     */
    public long getTotalTimeElapsed() {
      return totalTimeElapsed;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(name).append(": ");
      sb.append("charactersSaved=").append(totalCharactersSaved).append(", ");
      sb.append("timeElapsed=").append(totalTimeElapsed);
      return sb.toString();
    }
  }

  /**
   * Compares two {@link BenchmarkResult} instances by their total characters
   * saved.
   */
  public static final Comparator<BenchmarkResult> BENCHMARK_COMPARATOR =
      new Comparator<BenchmarkResult>() {
    public int compare(BenchmarkResult lhs, BenchmarkResult rhs) {
      if (lhs.totalCharactersSaved == rhs.totalCharactersSaved) {
        return 0;
      }
      return (lhs.totalCharactersSaved < rhs.totalCharactersSaved) ?
          -1 : 1;
    }
  };

  /**
   * TODO
   * 
   * @param trainingContents
   * @param benchmarkContents
   * @param name
   * @param annotationFactory
   * @return
   * @throws IOException
   */
  public static <T extends AnnotatedWord> BenchmarkResult run(
      String[] trainingContents, List<String[]> benchmarkContents,
      String name, AnnotationFactory<T> annotationFactory) throws IOException {
    ScoringWordPredictor<? extends AnnotatedWord> wordPredictor =
        createWordPredictor(annotationFactory);
    trainWordPredictor(trainingContents, wordPredictor);
    BenchmarkResult result = benchmarkWordPredictor(benchmarkContents,
        name, wordPredictor);
    return result;
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
  private static void trainWordPredictor(String[] trainingFiles,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    for (String trainingFile : trainingFiles) {
      trainWordPredictor(trainingFile, wordPredictor);
    }
  }

  /*
   * Trains the word predictor using the given file.
   */
  private static void trainWordPredictor(String trainingFile,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    BufferedReader reader = new BufferedReader(new StringReader(trainingFile));
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
   * TODO(mgp): make public
   * 
   * Reads the entire training file into memory to avoid disk I/O later.
   */
  private static String readEntireFileAsString(File f) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(f));
    while (true) {
      String nextLine = reader.readLine();
      if (nextLine == null) {
        break;
      }
      sb.append(nextLine);
      sb.append(' ');
    }
    return sb.toString();
  }

  /*
   * TODO(mgp): make public? share parsing logic with SentenceReader?
   */
  private static String[] readEntireFileAsWords(File f) throws IOException {
    String fileContents = readEntireFileAsString(f);
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
  private static BenchmarkResult benchmarkWordPredictor(List<String[]> benchmarkFiles,
      String name, ScoringWordPredictor<? extends AnnotatedWord> wordPredictor) {
    BenchmarkResult benchmarkResult = new BenchmarkResult(name);
    for (String[] benchmarkFile : benchmarkFiles) {
      benchmarkWordPredictor(benchmarkFile, wordPredictor, benchmarkResult);
    }
    return benchmarkResult;
  }

  /*
   * Benchmarks the word predictor using the given file.
   */
  private static void benchmarkWordPredictor(String[] benchmarkFile,
      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor,
      BenchmarkResult benchmarkResult) {
    int charactersSaved = 0;
    String prevPrevWord = null;
    String prevWord = null;

    long startTime = System.currentTimeMillis();
    for (String word : benchmarkFile) {
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

  /**
   * TODO
   * 
   * @param trainingFiles
   * @return
   * @throws IOException
   */
  public static String[] readAllTrainingFiles(File[] trainingFiles)
      throws IOException {
    String[] trainingContents = new String[trainingFiles.length];
    for (int i = 0; i < trainingFiles.length; ++i) {
      trainingContents[i] = readEntireFileAsString(trainingFiles[i]);
    }
    return trainingContents;
  }

  /**
   * TODO
   * 
   * @param benchmarkFiles
   * @return
   * @throws IOException
   */
  public static List<String[]> readAllBenchmarkFiles(File[] benchmarkFiles)
      throws IOException {
    List<String[]> benchmarkContents =
        new ArrayList<String[]>(benchmarkFiles.length);
    for (int i = 0; i < benchmarkFiles.length; ++i) {
      benchmarkContents.add(readEntireFileAsWords(benchmarkFiles[i]));
    }
    return benchmarkContents;
  }
}
