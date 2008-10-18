package org.ss12.wordprediction.newcore.annotations;

/**
 * A {@link LastTimeUsedClock} for use with {@link LastTimeUsedAnnotation}
 * instances.
 * 
 * @author Michael Parker
 */
public interface LastTimeUsedClock {
  /**
   * Returns the time in milliseconds since the epoch, similar to
   * {@link java.lang.System#currentTimeMillis()}.
   * 
   * @return the current time in milliseconds
   */
  public long getTimeInMillis();
}
