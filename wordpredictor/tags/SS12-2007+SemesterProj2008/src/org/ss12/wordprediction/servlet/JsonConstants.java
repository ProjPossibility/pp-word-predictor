package org.ss12.wordprediction.servlet;

/**
 * Constants used for converting predictions to and from JSON.
 * 
 * @author Michael Parker
 */
public final class JsonConstants {
  // Do not allow instantiation.
  private JsonConstants() {
  }

  /**
   * The JSON key for the incomplete word.
   */
  public static final String INCOMPLETE_KEY = "incompleteWord";

  /**
   * The JSON key for the array of preceding words.
   */
  public static final String CONTEXT_KEY = "context";

  /**
   * The JSON key for the array of suggestions.
   */
  public static final String SUGGESTIONS_KEY = "suggestions";
}
