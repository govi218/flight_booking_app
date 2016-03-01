package cs.bo7.p3.user;


import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * Creates an Administrator.
 *
 * @author Simon
 * @author sakshaatchoyikandi
 */
public class Admin extends AppUser implements Serializable {

  //Serial Version ID for serialization
  private static final long serialVersionUID = 5603302669386424269L;
  private String info;
  private String password;

  /**
   * Default constructor for Admin.
   *
   * @param info - The user-name for this admin's account.
   * @param pass - The password for this admin's account.
   */
  public Admin(String info, String pass) {
    this.info = info;
    this.password = pass;
  }

  /**
   * Retrieves the information of the given client.
   *
   * @param client A Client object.
   * @return A string representation of the clients information.
   */
  public String viewClientInfo(Client client) {
    return client.toString();
  }

  /**
   * Edits the given clients information. the field to change and the new entry (change).
   *
   * @param client The Client object which needs to be edited.
   * @param field The field within the client's account which is to be altered.
   * @param change The edit which is to be applied.
   */
  public void editClientInfo(Client client, String field, String change) {
    try {
      client.editInfo(field, change);
    } catch (EmptySpaceException | IncorrectRegistrationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves the information of the given Flight object.
   *
   * @param flight A Flight object.
   * @return A string representation of the flight objects information.
   */
  public String getFlightInfo(Flight flight) {
    NumberFormat format = new DecimalFormat("#0.00");
    return (flight.toString() + "," + format.format(flight.getPrice()));
  }

  /**
   * Edits flight's information.
   *
   * @param flight A Flight object.
   * @param field The field within the client's account which is to be altered.
   * @param change The edit that needs to be applied.
   */
  public void setFlightInfo(Flight flight, String field, String change) {
    FlightManager.setFlightInfo(flight, field, change);
  }

  /**
   * Returns the flight information.
   *
   * @return the info Information about this administrator.
   */
  public String getInfo() {
    return this.info;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    AccountManager.getAdmins().remove(this);
    this.password = password;
    AccountManager.getAdmins().add(this);
  }

  @Override
  public boolean equals(Object client) {
    return (this.info).equals(((Admin) client).getInfo());

  }
}
