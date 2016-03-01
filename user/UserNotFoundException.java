package cs.bo7.p3.user;

/**
 * A class to represent the exception of a User not being found.
 *
 * @author sakshaatchoyikandi
 */
public class UserNotFoundException extends Exception {

  // Serialization
  private static final long serialVersionUID = 5871889931777335987L;

  /**
   * Constructs a new UserNotFoundException with null as its detail message.
   */
  public UserNotFoundException() {
  }

  /**
   * Constructs a new UserNotFoundException with a specified detail message.
   *
   * @param message the detail message
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new UserNotFoundException with the specified cause and a detail message of (cause
   * == null ? null : cause.toString()).
   *
   * @param cause the cause of the UserNotFoundException
   */
  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new UserNotFoundException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the UserNotFoundException
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
