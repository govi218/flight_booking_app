package cs.bo7.p3.user;

/**
 * Thrown when a Client attempts to book an Itinerary that already exists.
 * Created by Faizan on 2015-12-04.
 */
public class BookingAlreadyExistsException extends Exception {

  /**
   * Constructs a new BookingAlreadyExistsException with null as its detail message.
   */
  public BookingAlreadyExistsException() {}


  /**
   * Constructs a new BookingAlreadyExistsException with a specified detail message.
   * @param message the detail message
   */
  public BookingAlreadyExistsException(String message) {
    super(message);
  }
}
