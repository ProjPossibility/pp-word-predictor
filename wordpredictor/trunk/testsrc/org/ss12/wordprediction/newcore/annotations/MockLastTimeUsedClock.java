package org.ss12.wordprediction.newcore.annotations;

/**
 * A mock implementation of {@link LastTimeUsedClock} that allows controlling
 * the time returned. The internal time value is automatically incremented
 * after every call to {@link #getTimeInMillis()}.
 * 
 * @author Michael Parker
 */
public class MockLastTimeUsedClock implements LastTimeUsedClock {
  private long nextTime;

  /**
   * Creates a new clock that returns {@code 0L} upon its first call to
   * {@link #getTimeInMillis()}.
   */
  public MockLastTimeUsedClock() {
    this(0L);
  }

  /**
   * Creates a new clock that returns the given time upon its first call to
   * {@link #getTimeInMillis()}.
   * 
   * @param startTime the first time to return
   */
  public MockLastTimeUsedClock(long startTime) {
    nextTime = startTime;
  }

  /**
   * Sets the time to return upon the next call to {@link #getTimeInMillis()}.
   * 
   * @param newTime the new internal time
   */
  public void reset(long newTime) {
    nextTime = newTime;
  }

  /**
   * @return the time that will be returned by the next call to
   *         {@link #getTimeInMillis()}
   */
  public long peek() {
    return nextTime;
  }

  public long getTimeInMillis() {
    return nextTime++;
  }
}
