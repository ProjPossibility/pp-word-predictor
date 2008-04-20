package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Unit test for {@link SignificanceTextFileReader} and
 * {@link SignificanceTextFileWriter}.
 * 
 * @author Michael Parker
 */
public class SignificanceTextFileTest extends TestCase {
  private File f;

  @Override
  protected void setUp() throws IOException {
    f = File.createTempFile("significance", null);
  }

  @Override
  protected void tearDown() {
    f.delete();
    f = null;
  }

  public void testWriteAndRead() throws IOException {
    WordFrequencyPair ws1 = new WordFrequencyPair("word1", 50);
    WordFrequencyPair ws2 = new WordFrequencyPair("word2", 20);
    WordFrequencyPair ws3 = new WordFrequencyPair("word3", 100);

    // Write the WordSignificance instances to the text file.
    SignificanceTextFileWriter writer = new SignificanceTextFileWriter(f);
    writer.writeNext(ws1);
    writer.writeNext(ws2);
    writer.writeNext(ws3);
    writer.close();

    // Read the WordSignificance instances from the text file.
    SignificanceTextFileReader reader = new SignificanceTextFileReader(f);
    WordFrequencyPair wsCopy1 = reader.readNext();
    WordFrequencyPair wsCopy2 = reader.readNext();
    WordFrequencyPair wsCopy3 = reader.readNext();
    assertNull(reader.readNext());
    reader.close();

    // Compare the written and read instances.
    assertEquals(ws1, wsCopy1);
    assertEquals(ws2, wsCopy2);
    assertEquals(ws3, wsCopy3);
  }

  public void testWriteAndReadEmptyFile() throws IOException {
    SignificanceTextFileWriter writer = new SignificanceTextFileWriter(f);
    writer.close();

    SignificanceTextFileReader reader = new SignificanceTextFileReader(f);
    assertNull(reader.readNext());
    reader.close();
  }
}
