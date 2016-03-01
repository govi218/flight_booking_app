package cs.bo7.p3.user;

/**
 * A class to represent the exception of a User not being found.
 *
 * @author sakshaatchoyikandi
 */
public class IncorrectRegistrationException extends Exception {

  /**
   * Constructs a new IncorrectRegistrationException with null as its detail message.
   */
  public IncorrectRegistrationException() {
  }

  /**
   * Constructs a new IncorrectRegistrationException with a specified detail message.
   *
   * @param message the detail message
   */
  public IncorrectRegistrationException(String message) {

    super(message);
  }

}
