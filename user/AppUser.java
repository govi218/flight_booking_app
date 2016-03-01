package cs.bo7.p3.user;

import java.io.Serializable;

/**
 * Parent Class for the Client and Admin classes Provides the user-name and password variables along
 * with their getters
 *
 * @author Daerian.
 */

public abstract class AppUser implements Serializable {


  /**
   * Reads the information from a csv line and assigns it.
   *
   * @param information - the information to be entered
   */
  public void enterInfo(String information) {
  }

}
