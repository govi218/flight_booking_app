package cs.bo7.p3.flight;


import android.util.Log;

import cs.bo7.p3.driver.Constants;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;


/**
 * A class whose object represents an instance of a Flight.
 *
 * @author Govind Mohan
 * @author sakshaatchoyikandi
 */
public class Flight extends Observable implements Comparable<Flight>, Serializable {


  //Serial Version ID for serialization.
  private static final long serialVersionUID = -8847250741487918060L;

  // Instance variables.
  private String flightnum;
  private String airline;
  private String origin;
  private String destination;
  private Date departure;
  private Date arrival;
  private int numSeats;
  private double price;
  private long flighttime;

  /**
   * A constructor class that initializes all variables of a Flight.
   *
   * @param flightnum A String that stores the flight number
   * @param airline A String that stores the airline
   * @param origin A String that stores the origin of the flight
   * @param destination A String that stores the intended destination
   * @param departure A Date that stores the departure date
   * @param arrival A Date that stores the arrival date
   * @param deptime A long that stores the departure time
   * @param arrtime A long that stores the arrival time
   * @param price A double that stores the price of the flight
   * @throws ParseException This constructor can throw a ParseException
   */
  public Flight(String flightnum, String airline, String origin, String destination, String
      departure, String arrival, String deptime, String arrtime, String price, String numSeats)
      throws ParseException {
    this.flightnum = flightnum;
    this.airline = airline;
    this.origin = origin;
    this.destination = destination;
    this.numSeats = Integer.parseInt(numSeats);

    // format to be used for all conversions
    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_AND_TIME);
    this.price = Double.parseDouble(price);
    // All of the departure information is in one Date variable.

    try {
      this.departure = format.parse(departure + " " + deptime);
    } catch (ParseException parseE) {
      parseE.printStackTrace();
    }


    // All of the arrival information is in one Date variable.
    try {
      this.arrival = format.parse(arrival + " " + arrtime);
    } catch (ParseException parseE) {
      parseE.printStackTrace();
    }
    Log.d("hi", arrival);

    this.calculateFlightTime();
  }

  /**
   * Returns the departure date.
   *
   * @return the departure date and time (in the format yyyy-MM-dd HH:mm)
   */
  public Date getDeparture() {
    return departure;
  }

  /**
   * Edits the departure date and departure time flight information, then calculates the new flight
   * time and updates observers.
   *
   * @param departure the new departure date and time (in the format yyyy-MM-dd HH:mm)
   */
  public void setDeparture(Date departure) {
    this.departure = departure;
    this.calculateFlightTime();

    // Notifies all observers of the change.
    this.hasChanged();
    this.notifyObservers();
  }

  /**
   * Returns the arrival date for this Flight.
   *
   * @return the arrival date for this Flight (in the format yyyy-MM-dd HH:mm)
   */
  public Date getArrival() {
    return arrival;
  }

  /**
   * Edits the arrival and arrival time flight information, then calculates the new flight time and
   * updates observers.
   *
   * @param arrival the new arrival date and time (in the format yyyy-MM-dd HH:mm)
   */
  public void setArrival(Date arrival) {
    this.arrival = arrival;

    // Notifies all observers of the change.
    this.calculateFlightTime();
    this.hasChanged();
    this.notifyObservers();

  }

  /**
   * Returns the flight number for this Flight.
   *
   * @return the flight number for this Flight
   */
  public String getFlightnum() {
    return flightnum;
  }

  /**
   * Returns the airline of this Flight.
   *
   * @return the airline of this Flight
   */
  public String getAirline() {
    return airline;
  }

  /**
   * Returns the place of origin of this Flight.
   *
   * @return the place fo origin of this Flight.
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Returns the destination of this Flight.
   *
   * @return the destination for this Flight.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Returns the price of a seat in this Flight.
   *
   * @return the price of a seat in this Flight
   */
  public double getPrice() {
    return price;
  }

  /**
   * Sets the price of this Flight and updates.
   *
   * @param price is the (double) price to set
   */
  public void setPrice(double price) {
    this.price = price;
    this.hasChanged();
    this.notifyObservers();
  }

  /**
   * Sets the price of this Flight and updates.
   *
   * @param price is the (String) price to set
   */
  public void setPrice(String price) {
    this.price = Double.parseDouble(price);
    this.hasChanged();
    this.notifyObservers();
  }

  /**
   * Sets the number of seats available of this Flight and updates.
   *
   * @param numSeats is the (int) price to set
   */
  public void setNumSeats(int numSeats) {
    this.numSeats = numSeats;
    this.hasChanged();
    this.notifyObservers();
  }

  /**
   * Sets the number of seats available of this Flight and updates.
   *
   * @param numSeats is the (String) price to set
   */
  public void setNumSeats(String numSeats) {
    this.numSeats = Integer.parseInt(numSeats);
    this.hasChanged();
    this.notifyObservers();
  }

  /**
   * Returns the number of seats available for this flight.
   *
   * @return the number of seats available for this flight
   */
  public int getNumSeats() {
    return numSeats;
  }

  /**
   * Decreases the number of seats in the flight.
   */
  public void booked() {
    this.numSeats = numSeats - 1;
    this.hasChanged();
    this.notifyObservers();
  }


  /**
   * Returns the time it takes for this Flight to reach its destination.
   *
   * @return the time it takes for this Flight to reach its destination
   */
  public long getFlighttime() {
    return flighttime;
  }

  /**
   * Private method that calculates the time it takes for this Flight to reach its destination.
   */


  private void calculateFlightTime() {

    this.flighttime = arrival.getTime() - departure.getTime();

  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Flight other) {
    if ((this.departure.getTime() - other.getDeparture().getTime()) > 0) {
      return 1;
    } else if ((this.departure.getTime() - other.getDeparture().getTime()) < 0) {
      return -1;
    } else {
      return 0;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String toReturn = "";

    // Use the following formats to format currect outputs.
    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE);
    SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME);

    toReturn += (this.getFlightnum() + "," + format.format(this.departure) + " " + timeFormat
        .format(this.departure) + "," + format.format(this.arrival) + " " + timeFormat.format(
        this.arrival) + "," + this.getAirline() + "," + this.getOrigin() + "," + this
        .getDestination() + "," + this.getPrice());

    return toReturn;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((airline == null) ? 0 : airline.hashCode());
    result = prime * result + ((arrival == null) ? 0 : arrival.hashCode());
    result = prime * result + ((departure == null) ? 0 : departure.hashCode());
    result = prime * result + ((destination == null) ? 0 : destination.hashCode());
    result = prime * result + ((flightnum == null) ? 0 : flightnum.hashCode());
    result = prime * result + (int) (flighttime ^ (flighttime >>> 32));
    result = prime * result + ((origin == null) ? 0 : origin.hashCode());
    long temp;
    temp = Double.doubleToLongBits(price);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object other) {
    return this.flightnum.equals(((Flight) other).getFlightnum());
  }


}