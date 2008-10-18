package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ss12.wordprediction.newcore.ScorerBenchmark.BenchmarkResult;
import org.ss12.wordprediction.newcore.annotations.FrecencyAnnotationFactory;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;
import org.ss12.wordprediction.newcore.annotations.LastTimeUsedAnnotationFactory;

/**
 * @author Michael Parker
 */
public class ScorerShootout {
  /**
   * The number of threads to concurrently compute {@link BenchmarkResult}
   * instances.
   */
  public static final int NUM_THREADS = 2;
  
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

  private static void addFrecencyAnnotations(
      List<NameAnnotationPair> nameAnnotationPairs, int numBuckets) {
    Iterable<int[]> allBucketSizes = getAllBucketSizes(numBuckets);
    Iterable<int[]> allWeights = getAllWeights(numBuckets);

    for (int[] bucketSizes : allBucketSizes) {
      for (int[] weights : allWeights) {
        String name = makeFrecencyAnnotationName(bucketSizes, weights);
        FrecencyAnnotationFactory annotationFactory =
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

  private static void verifyFilesExist(File[] files) {
    for (File file : files) {
      if (!file.exists()) {
        throw new IllegalArgumentException("File does not exist: " + file);
      }
    }
  }

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException {
    // Add all AnnotationFactory instances to benchmark.
    List<NameAnnotationPair> nameAnnotationPairs =
        new LinkedList<NameAnnotationPair>();
    nameAnnotationPairs.add(new NameAnnotationPair("frequency",
        new FrequencyAnnotationFactory()));
    nameAnnotationPairs.add(new NameAnnotationPair("last time used",
        new LastTimeUsedAnnotationFactory()));
    final int numBuckets = 3;
    addFrecencyAnnotations(nameAnnotationPairs, numBuckets);
    final int nameAnnotationPairsSize = nameAnnotationPairs.size();

    // Specify the files used to train the word predictor.
    File[] trainingFiles = new File[] {
        new File("./resources/books/bnw1.txt"),
        new File("./resources/books/bnw2.txt"),
        new File("./resources/books/bnw3.txt"),
        new File("./resources/books/bnw4.txt"),
        new File("./resources/books/bnw5.txt"),
        new File("./resources/books/bnw6.txt"),
        new File("./resources/books/bnw7.txt"),
        new File("./resources/books/bnw8.txt"),
        new File("./resources/books/bnw9.txt"),
        new File("./resources/books/bnw10.txt"),
        new File("./resources/books/bnw11.txt"),
        new File("./resources/books/bnw12.txt"),
        new File("./resources/books/bnw13.txt"),
        new File("./resources/books/bnw14.txt"),
        new File("./resources/books/bnw15.txt"),
        new File("./resources/books/bnw16.txt"),
        new File("./resources/books/bnw17.txt"),
        new File("./resources/books/bnw18.txt"),
        new File("./resources/books/af1.txt"),
        new File("./resources/books/af2.txt"),
        new File("./resources/books/af3.txt"),
        new File("./resources/books/af4.txt"),
        new File("./resources/books/af5.txt"),
    };
    // Specify the files used to benchmark the word predictor.
    File[] benchmarkFiles = new File[] {
        new File("./resources/books/af6.txt"),
        new File("./resources/books/af7.txt"),
        new File("./resources/books/af8.txt"),
        new File("./resources/books/af9.txt"),
        new File("./resources/books/af10.txt"),
    };
    verifyFilesExist(trainingFiles);
    final String[] trainingContents =
        ScorerBenchmark.readAllTrainingFiles(trainingFiles);
    verifyFilesExist(benchmarkFiles);
    final List<String[]> benchmarkContents =
        ScorerBenchmark.readAllBenchmarkFiles(benchmarkFiles);

    ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
    CompletionService<BenchmarkResult> completionService =
         new ExecutorCompletionService<BenchmarkResult>(executor);
    for (Iterator<NameAnnotationPair> i = nameAnnotationPairs.iterator();
        i.hasNext(); ) {
      // Remove after dequeueing for potential garbage collection later.
      final NameAnnotationPair nameAnnotationPair = i.next();
      i.remove();

      completionService.submit(new Callable<BenchmarkResult>() {
        public BenchmarkResult call() throws Exception {
          return ScorerBenchmark.run(trainingContents, benchmarkContents,
              nameAnnotationPair.name, nameAnnotationPair.annotationFactory);
        }
      });
    }

    List<BenchmarkResult> benchmarkResults =
        new ArrayList<BenchmarkResult>(nameAnnotationPairsSize);
    for (int i = 1; i <= nameAnnotationPairsSize; ++i) {
      benchmarkResults.add(completionService.take().get());
      System.out.println("Benchmarked " + i + " of " + nameAnnotationPairsSize);
    }

    System.out.println("\nResults:");
    Collections.sort(benchmarkResults, ScorerBenchmark.BENCHMARK_COMPARATOR);
    for (BenchmarkResult benchmarkResult : benchmarkResults) {
      System.out.println(benchmarkResult);
    }
    executor.shutdown();
  }
}
