package cs.bo7.p3.user;

import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.itinerary.Itinerary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contructs a Client Object. <p> Note: this class has a natural ordering that is inconsistent with
 * equals..</p>
 *
 * @author Daerian.
 * @author sakshaatchoyikandi.
 */
public class Client extends AppUser implements Serializable, Comparable<Client> {

  // ID for serialization.
  private static final long serialVersionUID = -4767119955450762235L;


  //Instance Variables

  //The last name of the client
  private String lastName;
  //The first name of the client
  private String firstName;
  //The email address of the client
  private String email;
  //The password
  private String password;
  //The address of the client
  private String address;
  //The credit card number of the client
  private String creditCardNumber;
  //Tthe expiration date of the credit card
  private String expiryDate;
  //An arraylist to store all bookings
  private ArrayList<Itinerary> bookings;

  /**
   * Default constructor for a client, takes in user name and password.
   *
   * @param info - The client's information as read from the csv.
   */
  public Client(String info) {
    // give this a new empty list of booked itineraries then turn the string into fields
    this.bookings = new ArrayList<Itinerary>();
    enterInfo(info);
  }


  /**
   * Reads the information from a csv line and assigns it.
   *
   * @param information - the information to be entered
   */
  public void enterInfo(String information) {
    // Split the given string and use each token to assign the corresponding instances
    String[] info = information.split("\\s*,\\s*");
    this.lastName = info[1];
    this.firstName = info[0];
    this.email = info[2];
    this.address = info[3];
    this.creditCardNumber = info[4];
    this.expiryDate = info[5];
    this.password = Constants.INIT_PASSWORD;

    if (info.length > 6) {
      this.password = info[6];
    }
  }

  /**
   * Acts as a setter for any given field of information from the info block or instance variables.
   *
   * @param field - the field to change (CASE SENSITIVE)
   * @param change - the change to be made
   */
  public void editInfo(String field, String change) throws EmptySpaceException,
      IncorrectRegistrationException {

    if (change.equals("")) {
      throw new EmptySpaceException();
    }

    // Check which field is being modified, then modify it.
    if (field.equals(Constants.LAST_NAME)) {
      this.lastName = change;
    } else if (field.equals(Constants.FIRST_NAME)) {
      this.firstName = change;
    } else if (field.equals(Constants.ADDRESS)) {
      this.address = change;
    } else if (field.equals(Constants.CCN)) {
      if (change.length() == 16) {
        this.creditCardNumber = change;
      } else {
        throw new IncorrectRegistrationException();
      }
    } else if (field.equals(Constants.PASSWORD)) {
      this.password = change;
    } else {
      if ((change.length() == 10) && (change.charAt(4) == '-') && (change.charAt(7) == '-')) {
        this.expiryDate = change;
      } else {
        throw new IncorrectRegistrationException();
      }
    }
  }


  /**
   * Adds the given itinerary to the list of booked itineraries.
   *
   * @param itinerary - The itinerary to be booked.
   */
  public void bookItinerary(Itinerary itinerary) {
    this.bookings.add(itinerary); // just add to the lsit of booked itineraries
  }

  /**
   * @return - Returns an ArrayList containing this client's bookings.
   */
  public ArrayList<Itinerary> getBookings() {
    return this.bookings;
  }


  @Override
  public String toString() {
    String myString = "";
    myString += this.lastName + "," + this.firstName + "," + this.email + "," + this.address + ","
        + "" + this.creditCardNumber + "," + this.expiryDate;
    return myString;
  }


  /**
   * Returns the password of the user.
   *
   * @return lastName
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the last name of the user.
   *
   * @return lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the first name of the user.
   *
   * @return firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the email address of the user.
   *
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns the address of the user.
   *
   * @return address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Returns the credit card number of the user.
   *
   * @return creditCardNumber
   */
  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  /**
   * Returns the expiration date of the credit card.
   *
   * @return the expiryDate
   */
  public String getExpiryDate() {
    return expiryDate;
  }

  @Override
  /**
   * Compares two Clients.
   * @param Another client object to compare.
   */
  public int compareTo(Client obj) {
    // check if the emails are the same
    if (this.getEmail().equals(obj.getEmail())) {
      return 0;
    } else {
      return 1;
    }
  }

  /**
   * Returns the number of bookings this account has made.
   *
   * @return the total number of booked itineraries.
   */
  public int numBookings() {
    return this.getBookings().size();
  }

  /**
   * Sorts all possible itineraries in the manager by cost.
   */
  public void sortByPrice() {
    Collections.sort(this.bookings, cs.bo7.p3.itinerary.Itinerary
            .ItineraryPriceComparator);
  }

  /**
   * Sorts all possible itineraries in the manager by time.
   */
  public void sortByTime() {
    Collections.sort(this.bookings, cs.bo7.p3.itinerary.Itinerary
            .ItineraryTimeComparator);
  }


  /**
   *
   */
  public void updateClient(Client other) {
    AccountManager.getClients().remove(this);
    AccountManager.getClients().add(other);
  }

  @Override
  public boolean equals(Object client) {
    return this.email.equals(((Client) client).getEmail());

  }

}
