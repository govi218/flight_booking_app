package cs.bo7.p3.itinerary;

import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.flight.FullFlightException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Performs all operations on Itineraries.
 *
 * @author sakshaatchoyikandi
 * @author mohammedfaizan
 */
public class ItineraryManager {


  //Instance Variables

  //A hash map to store a list of all flights listed
  private HashMap<String, ArrayList<Flight>> allFlights = new HashMap<String, ArrayList<Flight>>();
  //An array list to store all possible itineraries
  private ArrayList<Itinerary> allPossibleItineraries = new ArrayList<>();


  /** Constructs an itinerary. */
  public ItineraryManager() {

    // List of all flights.
    ArrayList<Flight> flights = FlightManager.getAllFlights();

    // Initializes the HashMap: allFlights with their origin as the key and
    // all the flights originating from the city
    // as the values inside an array list.
    for (Flight flight : flights) {
      String key = flight.getOrigin();
      // if the key already exists, add the flight in the map, else make a new entry in the map
      // with the flightOrigin as the key and add the Flight in the arrayList.
      if (this.allFlights.containsKey(key)) {
        this.allFlights.get(key).add(flight);
      } else {
        ArrayList<Flight> listToAdd = new ArrayList<Flight>();
        listToAdd.add(flight);
        this.allFlights.put(key, listToAdd);
      }
    }

  }

  /**
   * Returns all the itineraries found from the search.
   *
   * @return The search resulting from the inputed parameters.
   */
  public ArrayList<Itinerary> getAllPossibleItineraries() {
    return allPossibleItineraries;
  }

  /**
   * Searches for itineraries based on the departure date, origin and destination, and adds the
   * result to the static list of all possible itineraries..
   *
   * @param date - Date to search by.
   * @param origin - Point of origin.
   * @param destination - Point of destination.
   */
  public void search(String date, String origin, String destination) {
    // From the map of allFlights pick all the flights with origin as origin.
    ArrayList<Flight> originFlights = allFlights.get(origin);
    if (originFlights == null) {
      originFlights = new ArrayList<Flight>();
    }
    // for flights in the picked flights
    if (originFlights.size() > 0) {
      for (Flight flight : originFlights) {
        // if the destination is the destination provided the arrival date is
        // the date provided
        DateFormat dateConvert = new SimpleDateFormat(Constants.DATE);
        String departureDate = dateConvert.format(flight.getDeparture());

        // Create a list of saved flights to added to an itinerary.
        ArrayList<Flight> savedFlights = new ArrayList<>();
        savedFlights.add(flight);
        if (departureDate.equals(date)) {
          if (flight.getDestination().equals(destination) && departureDate.equals(date)) {
            // Create a new itinerary with that flight and related info and add
            // it to the list
            Itinerary savedItinerary = new Itinerary(savedFlights, origin, destination);
            allPossibleItineraries.add(savedItinerary);

          } else {
            // else recurse into an helper function complexItinerary
            Itinerary newItinerary = new Itinerary(savedFlights, origin, flight.getDestination());
            Itinerary recursedItinerary = complexItinerary(newItinerary, destination);

            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE);
            String itineraryDestination = recursedItinerary.getDestination();
            String itineraryDeparture = format.format(recursedItinerary.getFlights().get(0)
                .getDeparture());
            // if the result of the recursion is the required destination and date
            if (itineraryDestination.equals(destination) && itineraryDeparture.equals(
                 departureDate)) {
              // add to the list of itineraries
              allPossibleItineraries.add(recursedItinerary);
            }
          }
        }
      }
    }
  }

  /**
   * Handles the cases where the itinerary does not contain a direct flight.
   *
   * @param current the itinerary to be checked and modified.
   * @param destination the desired destintation of the trip.
   * @return the itinerary which cannot be operated on anymore.
   */
  private Itinerary complexItinerary(Itinerary current, String destination) {
    // if current destination is not the intended destination
    if (!(current.getDestination().equals(destination))) {
      // loop through all flights with the itinerary destination as origin.
      ArrayList<Flight> destinationFlights = allFlights.get(current.getDestination());

      if (!(destinationFlights == null)) {
        for (Flight someFlight : destinationFlights) {
          if (current.getFlights().contains(someFlight)) {
            return current;
          }
        }
        if (destinationFlights.size() > 0) {
          for (Flight flight : destinationFlights) {
            // Only proceed if the lay over time is less that the realistic wait time.
            if (layoverTime(current, flight) <= Constants.WAITTIME) {
              // index of the last flight in the itinerary.
              int lastFlightIndex = current.getFlights().size() - 1;

              // add the new flight into the itinerary if and only
              // if the the last flight in the itinerary departs before
              // the new flight about to be added,
              if (current.getFlights().get(lastFlightIndex).getArrival().compareTo(flight
                  .getDeparture()) < 0) {
                // add the new flight into the itinerary.
                itineraryUpdater(current, flight);
                // recurse into the function with the new itinerary.
                return complexItinerary(current, destination);
              }
            }

          }
        }
      }
    }
    // else return the current itinerary
    return current;
  }

  /**
   * Updates a given itinerary with a given flight.
   *
   * @param current the Itinerary which needs to be updated.
   * @param flight the Flight which is used to update.
   */
  private void itineraryUpdater(Itinerary current, Flight flight) {
    long layover = layoverTime(current, flight);
    current.getFlights().add(flight);
    current.setDestination(flight.getDestination());
    current.setTotalTime(current.getTotalTime() + flight.getFlighttime() + layover);
    current.setTotalCost(current.getTotalCost() + flight.getPrice());
  }

  /**
   * Returns the wait time between connecting flights.
   *
   * @param current the itinerary which contains the flight to be connected from
   * @param flight the connecting flight
   * @return the layover time
   */
  private long layoverTime(Itinerary current, Flight flight) {
    // create an ArrayList to save all flights in the current Itinerary
    ArrayList<Flight> flightList = current.getFlights();
    // retrieve the last flight in the itinerary
    Flight connectFromFlight = flightList.get(flightList.size() - 1);
    // retrieve the arrival and departure times for the flight
    long flightArrival = connectFromFlight.getArrival().getTime();
    long flightDeparture = flight.getDeparture().getTime();
    // calculate the lay-over time
    return flightDeparture - flightArrival;

  }

  /**
   * Checks if any of the flights in the trip are full.
   */
  public static void checkStatus(Itinerary trip) throws FullFlightException {
    for (Flight flight: trip.getFlights()) {
      if (flight.getNumSeats() == 0) {
        throw new FullFlightException();
      }
    }
  }

  /**
   * Sorts all possible itineraries in the manager by cost.
   */
  public void sortByPrice() {
    Collections.sort(this.allPossibleItineraries, cs.bo7.p3.itinerary.Itinerary
        .ItineraryPriceComparator);
  }

  /**
   * Sorts all possible itineraries in the manager by time.
   */
  public void sortByTime() {
    Collections.sort(this.allPossibleItineraries, cs.bo7.p3.itinerary.Itinerary
        .ItineraryTimeComparator);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String returnValue = "";
    for (Itinerary itinerary : allPossibleItineraries) {
      returnValue += itinerary.toString() + "\n";
    }

    return returnValue;

  }
}
