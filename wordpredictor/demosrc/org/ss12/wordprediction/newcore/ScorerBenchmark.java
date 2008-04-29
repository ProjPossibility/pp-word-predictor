package org.ss12.wordprediction.newcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ss12.wordprediction.PredictionRequest;
import org.ss12.wordprediction.newcore.annotations.FrecencyAnnotation;
import org.ss12.wordprediction.newcore.annotations.FrecencyAnnotationFactory;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;

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
    private final String name;
    int totalCharactersSaved;
    long totalTimeElapsed;

    BenchmarkResult(String name) {
      this.name = name;
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
      sb.append(name).append(": ");
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
      String name, ScoringWordPredictor<? extends AnnotatedWord> wordPredictor)
      throws IOException {
    BenchmarkResult benchmarkResult = new BenchmarkResult(name);
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

  private static void verifyFilesExist(File[] files) {
    for (File file : files) {
      if (!file.exists()) {
        throw new IllegalArgumentException("File does not exist: " + file);
      }
    }
  }

  private static void addFrecencyAnnotations(
      List<NameAnnotationPair> nameAnnotationPairs, int numBuckets) {
    Iterable<int[]> allBucketSizes = getAllBucketSizes(numBuckets);
    Iterable<int[]> allWeights = getAllWeights(numBuckets);

    for (int[] bucketSizes : allBucketSizes) {
      for (int[] weights : allWeights) {
        String name = makeFrecencyAnnotationName(bucketSizes, weights);
        AnnotationFactory<FrecencyAnnotation> annotationFactory =
            new FrecencyAnnotationFactory(bucketSizes, weights);
        nameAnnotationPairs.add(
            new NameAnnotationPair(name, annotationFactory));
      }
    }
  }

  private static List<int[]> generate(int numBuckets,
      int baseValue, float[] multipliers, float[] ratios) {
    List<int[]> allValues = new LinkedList<int[]>();
    for (float multiplier : multipliers) {
      int adjustedValue = (int) Math.ceil(baseValue * multiplier);

      for (float ratio : ratios) {
        int[] values = new int[numBuckets];
        values[0] = adjustedValue;
        float product = adjustedValue;
        for (int i = 1; i < numBuckets; ++i) {
          product *= ratio;
          values[i] = (int) Math.ceil(product);
        }
        
        allValues.add(values);
      }
    }
    return allValues;
  }

  private static Iterable<int[]> getAllBucketSizes(int numBuckets) {
    float[] multipliers = { 1.5f, 3f, 5f, 7.5f, 10f };
    // Bucket sizes are smaller for newer annotations, larger for older, 
    float[] ratios = { 1/0.9f, 1/0.75f, 1/0.6f, 1/0.45f, 1/0.3f, 1/0.15f };
    return generate(numBuckets, 100, multipliers, ratios);
  }

  private static Iterable<int[]> getAllWeights(int numBuckets) {
    float[] multipliers = { 1.5f, 3f, 5f, 7.5f, 10f };
    // Bucket sizes are larger for newer annotations, smaller for older.
    float[] ratios = { 0.9f, 0.75f, 0.6f, 0.45f, 0.3f, 0.15f };
    return generate(numBuckets, 10, multipliers, ratios);
  }

  private static String makeFrecencyAnnotationName(
      int[] bucketSizes, int[] weights) {
    StringBuilder sb = new StringBuilder();
    sb.append("frecency: ");
    sb.append("b=").append(Arrays.toString(bucketSizes));
    sb.append(", w=").append(Arrays.toString(weights));
    return sb.toString();
  }

  private static final Comparator<BenchmarkResult> BENCHMARK_COMPARATOR =
      new Comparator<BenchmarkResult>() {
    public int compare(BenchmarkResult lhs, BenchmarkResult rhs) {
      if (lhs.totalCharactersSaved == rhs.totalCharactersSaved) {
        return 0;
      }
      return (lhs.totalCharactersSaved < rhs.totalCharactersSaved) ?
          -1 : 1;
    }
  };

  public static void main(String[] args) throws IOException {
    // Add all AnnotationFactory instances to benchmark.
    List<NameAnnotationPair> nameAnnotationPairs =
        new LinkedList<NameAnnotationPair>();
    nameAnnotationPairs.add(new NameAnnotationPair("frequency",
        new FrequencyAnnotationFactory()));
    final int numBuckets = 3;
    addFrecencyAnnotations(nameAnnotationPairs, numBuckets);

    // Specify the files used to train the word predictor.
    File[] trainingFiles = new File[] {
        new File("./resources/sample/bible_acts.txt")
    };
    // Specify the files used to benchmark the word predictor.
    File[] benchmarkFiles = new File[] {
        new File("./resources/sample/bible_luke.txt")
    };
    verifyFilesExist(trainingFiles);
    verifyFilesExist(benchmarkFiles);

    List<BenchmarkResult> benchmarkResults =
        new ArrayList<BenchmarkResult>(nameAnnotationPairs.size());
    for (Iterator<NameAnnotationPair> i = nameAnnotationPairs.iterator();
        i.hasNext(); ) {
      // Remove after dequeueing for potential garbage collection later.
      NameAnnotationPair nameAnnotationPair = i.next();
      i.remove();

      ScoringWordPredictor<? extends AnnotatedWord> wordPredictor =
          createWordPredictor(nameAnnotationPair.annotationFactory);
      trainWordPredictor(trainingFiles, wordPredictor);
      benchmarkResults.add(benchmarkWordPredictor(benchmarkFiles,
          nameAnnotationPair.name, wordPredictor));
    }

    Collections.sort(benchmarkResults, BENCHMARK_COMPARATOR);
    for (BenchmarkResult benchmarkResult : benchmarkResults) {
      System.out.println(benchmarkResult);
    }
  }
}
