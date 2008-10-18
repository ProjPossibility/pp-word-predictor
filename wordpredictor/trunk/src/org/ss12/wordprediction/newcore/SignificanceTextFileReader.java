package org.ss12.wordprediction.newcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class for generating {@link WordFrequencyPair} instances from a text file,
 * where each line in the file must contain a word, followed by its
 * significance.
 * 
 * @see SignificanceTextFileWriter
 * 
 * @author Michael Parker
 */
public class SignificanceTextFileReader {
  // Package protected so can be used by SignificanceFileWriter.
  static final String SIGNIFIANCE_DELIMETER = " ";

  private final BufferedReader reader;
  private boolean isClosed;

  /**
   * Creates a new reader of {@link WordFrequencyPair} instances.
   * 
   * @param f the file to read
   * @throws IOException if an underlying reader for the file could not be
   *           created
   */
  public SignificanceTextFileReader(File f) throws IOException {
    reader = new BufferedReader(new FileReader(f));
    isClosed = false;
  }

  /**
   * Returns the next {@link WordFrequencyPair} instance. If this reader has
   * reached the end of the file, this method returns {@code null} and
   * {@link #close()} is automatically invoked.
   * 
   * @return the next {@link WordFrequencyPair} instance read from the file, or
   *         {@code null} if at the end of file
   * @throws IOException if data could not be read from the file
   */
  public WordFrequencyPair readNext() throws IOException {
    String line = reader.readLine();
    if (line == null) {
      // Reached end of file, so close
      close();
      return null;
    }

    String[] fields = line.split(SIGNIFIANCE_DELIMETER);
    return new WordFrequencyPair(fields[0], Integer.valueOf(fields[1]));
  }

  /**
   * Closes this reader; if already invoked, either implicitly by
   * {@link #readNext()} or explicitly, this method has no effect.
   * 
   * @throws IOException if the underlying reader could not be closed
   */
  public void close() throws IOException {
    if (!isClosed) {
      reader.close();
      isClosed = true;
    }
  }
}
