package org.ss12.wordprediction.newcore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for generating a text file consisting of {@link WordSignificance}
 * instances, where each line in the file contains a word, followed by its
 * significance.
 * 
 * @see SignificanceTextFileReader
 * @author Michael Parker
 */
public class SignificanceTextFileWriter {
  private final BufferedWriter writer;
  private boolean isClosed;

  /**
   * Creates a new writer of {@link WordSignificance} instances.
   * 
   * @param f the file to write
   * @throws IOException if an underlying writer for the file could not be
   *           created
   */
  public SignificanceTextFileWriter(File f) throws IOException {
    writer = new BufferedWriter(new FileWriter(f));
    isClosed = false;
  }

  /**
   * Writes the given {@link WordSignificance} instance.
   * 
   * @param significance the {@link WordSignificance} instance to write to the
   *          file
   * @throws IOException if data could not be written to the flie
   */
  public void writeNext(WordSignificance significance) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(significance.word);
    sb.append(SignificanceTextFileReader.SIGNIFIANCE_DELIMETER);
    sb.append(significance.significance);
    sb.append('\n');

    writer.write(sb.toString());
  }

  /**
   * Closes this writer; if already invoked, this method has no effect.
   * 
   * @throws IOException if the underlying writer could not be closed
   */
  public void close() throws IOException {
    if (!isClosed) {
      writer.close();
      isClosed = true;
    }
  }
}
