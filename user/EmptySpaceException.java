package cs.bo7.p3.user;

/**
 * This exception is thrown when a user inputs an empty String
 * in an attempt to change a User's settings.
 */
public class EmptySpaceException extends Exception {
  /**
   * Constructs a new IncorrectRegistrationException with null as its detail message.
   */
  public EmptySpaceException() {
  }

  /**
   * Constructs a new IncorrectRegistrationException with a specified detail message.
   * @param message the detail message
   */
  public EmptySpaceException(String message) {
    super(message);
  }
}
