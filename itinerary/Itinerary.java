package cs.bo7.p3.itinerary;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;


/**
 * A class that contains an Itinerary for a trip.
 *
 * @author sakshaatchoyikandi
 * @author mohammedfaizan.
 */
public class Itinerary implements Serializable, Observer {

  // Instance Variables

  // Serial Version ID for serialization.
  private static final long serialVersionUID = 2536849243609871897L;
  // An array list of all the flights in the itinerary.
  private ArrayList<Flight> flights = new ArrayList<Flight>();
  // The place of origin for this itinerary.
  private String origin;
  // The final destination for this itinerary.
  private String destination;
  // The total cost of this itinerary.
  private double totalCost = 0L;
  // The total time of this itinerary.
  private long totalTime = 0L;


  /**
   * Creates a comparator instance to compare Itinerary by price
   * in a non-decreasing order.
   */
  public static Comparator<Itinerary> ItineraryPriceComparator = new Comparator<Itinerary>() {

    public int compare(Itinerary o1, Itinerary o2) {
      return (int) ((o1.getTotalCost() - o2.getTotalCost()) * 100);
    }

  };

  /**
   * Creates a comparator instance to compare Itinerary
   * by time in a non-decreasing order.
   */
  public static Comparator<Itinerary> ItineraryTimeComparator = new Comparator<Itinerary>() {

    public int compare(Itinerary o1, Itinerary o2) {
      if (o1.getTotalTime() > o2.getTotalTime()) {
        return 1;
      } else if (o1.getTotalTime() == o2.getTotalTime()) {
        return 0;
      } else {
        return -1;
      }
    }
  };

  /**
   * Constructs an Itinerary Object.
   *
   * @param flights     List of all the flights in the itinerary.
   * @param origin      The origin of the itinerary.
   * @param destination The final destination of the itinerary.
   */
  public Itinerary(ArrayList<Flight> flights, String origin, String destination) {
    this.flights = flights;
    this.origin = origin;
    this.destination = destination;

    for (Flight flight : flights) {
      // For every flight add the total time and the total flighttime.
      totalCost += flight.getPrice();
      totalTime += flight.getFlighttime();
    }

  }


  /**
   * Returns the list of all flights in this itinerary.
   *
   * @return the flights
   */
  public ArrayList<Flight> getFlights() {
    return flights;
  }


  /**
   * Returns the origin of the trip.
   *
   * @return the origin
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Returns the destination of the trip.
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Returns the cost of the trip.
   *
   * @return the cost
   */
  public double getTotalCost() {
    return totalCost;
  }

  /**
   * Returns the total time of the trip.
   *
   * @return the time
   */
  public long getTotalTime() {
    return totalTime;
  }


  /**
   * Sets the destination of the trip.
   *
   * @param destination the destination to set.
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Sets the total cost of the trip.
   *
   * @param totalCost the totalCost to set.
   */
  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }

  /**
   * Sets the total time of the trip.
   *
   * @param totalTime the totalTime to set.
   */
  public void setTotalTime(long totalTime) {
    this.totalTime = totalTime;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String toReturn = "";
    for (Flight flight : flights) {
      toReturn += flight.toString() + "\n";
    }

    /*long timeHour = ((totalTime / (1000 * 60 * 60)));
    long timeMinute = (totalTime / (1000 * 60)) - timeHour * 60;
    String time = String.format("%02d:%02d", timeHour, timeMinute);
    */
    String time = getConvertedTime(totalTime);
    NumberFormat formatter = new DecimalFormat("#0.00");

    toReturn += formatter.format(this.totalCost) + "\n" + time;
    return toReturn;
  }

  public static String getConvertedTime(long convertTime) {
    long timeHour = ((convertTime / (1000 * 60 * 60)));
    long timeMinute = (convertTime / (1000 * 60)) - timeHour * 60;
    String time = String.format("%02d:%02d", timeHour, timeMinute);
    return time;
  }

  public ArrayList<String> getLayoverTimes() {
    ArrayList<String> layovers = new ArrayList<>();
    if (flights.size() != 1) {
      return (recLayovers(layovers, 0));
    }
    return null;
  }

  private ArrayList<String> recLayovers(ArrayList<String> layovers, int index) {
    if (flights.size() - 1 == index) {
      return layovers;
    } else {
      long flight2Departure = flights.get(index + 1).getDeparture().getTime();
      long flight1Arrive = flights.get(index).getArrival().getTime();
      layovers.add(getConvertedTime(flight2Departure - flight1Arrive));
      return recLayovers(layovers, index + 1);
    }
  }


  /* (non-Javadoc)
   * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
   */
  @Override
  public void update(Observable ob, Object arg) {
    Flight changedFlight = ((Flight) ob);
    for (Flight flight : flights) {
      if (flight.equals(changedFlight)) {
        FlightManager.update(flight, changedFlight);
        break;
      }
    }
  }

  public boolean equals(Object other) {
    for (Flight flight: ((Itinerary) other).getFlights()) {
      if (!this.flights.contains(flight)) {
        return false;
      }
    }
    return true;
  }

}
