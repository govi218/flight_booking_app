package cs.bo7.p3.flight;

/**
 * Thrown when the flight being registered is full.
 * Created by sakshaatchoyikandi on 2015-12-04.
 */
public class FullFlightException extends Exception {

  /**
   * Constructs a new FullFlightException with null as its detail message.
   */
  public FullFlightException() {}


  /**
   * Constructs a new FullFlightException with a specified detail message.
   * @param message the detail message
   */
  public FullFlightException(String message) {
    super(message);
  }
}
