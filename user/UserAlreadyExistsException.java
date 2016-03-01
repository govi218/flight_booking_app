package cs.bo7.p3.user;

/**
 * This Exception is thrown when the User already exists in the memory.
 */

/**
 * Constructs a new UserAlreadyExistsException with null as its detail message.
 */
public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException() {
  }


  /**
   * Constructs a new UserAlreadyExistsException with a specified detail message.
   *
   * @param message the detail message
   */
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
