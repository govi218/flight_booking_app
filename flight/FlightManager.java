package cs.bo7.p3.flight;

import cs.bo7.p3.driver.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;




/**
 * Manages all operations on flights.
 *
 * @author Daerian Dilkumar
 * @author sakshaatchoyikandi
 */
public class FlightManager {

  // Instance Variables

  // An arraylist of all flights that can be taken.
  private static ArrayList<Flight> allFlights = new ArrayList<>();
  // A final string containing the location of Flights on the disk
  private static String serializedFlightFilePath;

  /**
   * Loads all the flights from memory at construction of a FlightManager.
   */
  public FlightManager() {

    // Load all the saved flights from the memory.
    this.deserializeAllFlights();
  }

  public static void filePath(String filePath) {
    serializedFlightFilePath = filePath + "/flights/";
  }

  /**
   * Edits information inside a flight.
   *
   * @param flight The flight currently being edited.
   * @param field The field currently being edited. Allowed fields are "Arrival", "Departure" and
   *     "Price".
   * @param change The new information to be added.
   */
  public static void setFlightInfo(Flight flight, String field, String change) {

    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_AND_TIME);

    // Field contains which field is to be changed.

    switch (field) {
      case "Arrival":
        try {
          Date arrival = format.parse(change);
          flight.setArrival(arrival);
        } catch (ParseException e1) {
          e1.printStackTrace();
        }
        break;
      case "Departure":
        try {
          Date departure = format.parse(change);
          flight.setArrival(departure);
        } catch (ParseException e1) {
          e1.printStackTrace();
        }
        break;
      case "Price":
        flight.setPrice(change);
        break;
      default:
        break;
    }


  }

  /**
   * Loads information from a file and creates Client objects based on the information, given the
   * filePath.
   *
   * @param filePath - The file to read the information out of.
   * @throws FileNotFoundException - Throws if fileName given does not exist.
   * @throws ParseException - Throws if it can not convert from string to a double.
   * @throws java.text.ParseException - Throws if it can not convert from string to date format
   */
  @SuppressWarnings("resource")
  public static void uploadFlightInfo(String filePath) throws FileNotFoundException,
          ParseException {

    // Initialize variables.
    String flightNum;
    String airline;
    String origin;
    String destination;
    String departure;
    String arrival;
    String deptime;
    String arrtime;
    String price;
    String numSeats;

    // Open a scanner file.
    Scanner scan = new Scanner(new File(filePath));


    while (scan.hasNextLine()) {
      // let line be the String file to be parsed to create a Flight file.
      String line = scan.nextLine();
      if (!line.isEmpty()) {
        // parse the file
        String[] items = line.split("\\s*,\\s*");

        flightNum = items[0];

        // Date and time are split by space, so split the arrival to get these values.
        String[] splitDep = items[1].split(" ");
        departure = splitDep[0];
        deptime = splitDep[1];

        // Similar process as above to get departure time and date.
        String[] splitArr = items[2].split(" ");
        arrival = splitArr[0];
        arrtime = splitArr[1];

        airline = items[3];
        origin = items[4];
        destination = items[5];
        price = items[6];
        numSeats = items[7];

        // Add the flight into the function.
        addFlight(flightNum, airline, origin, destination, departure, arrival, deptime, arrtime,
            price, numSeats);
      }
    }
  }

  /**
   * Adds a new flight to the allFlights list.
   *
   * @param flightNum - The flight number.
   * @param airline - The name of the airline.
   * @param origin - The origin of the flight.
   * @param destination - The destination of the flight.
   * @param departure - The departure date.
   * @param arrival - The arrival date.
   * @param deptime - The departure time.
   * @param arrtime - The arrival time.
   * @param price - The price of the airline.
   * @throws ParseException - Throws an exception is given times cannot be parsed properly
   */
  public static void addFlight(String flightNum, String airline, String origin, String
      destination, String departure, String arrival, String deptime, String arrtime, String
      price, String numSeats) throws ParseException {
    // Create a new instance of Flight.
    Flight newFlight = new Flight(flightNum, airline, origin, destination, departure, arrival,
        deptime, arrtime, price, numSeats);
    // if there are no Flights,
    if (allFlights.size() == 0) {
      allFlights.add(newFlight);
    } else {
      /* // if the flight to be added is already in the list.
      if (allFlights.contains(newFlight)) {
      // if yes, find the instance of the duplicate file, and update it.
      for (Flight flight:allFlights) {
      if (flight.equals(newFlight)) {
      update(flight, newFlight);
      }
      }
      } else {
      // if the flight isn't already in the list, add it.
      allFlights.add(newFlight);
      }*/
      if (!(allFlights.contains(newFlight))) {
        allFlights.add(newFlight);
      }
    }
  }

  /**
   * Updates the self parameter Flight with the information from the other parameter Flight.
   *
   * @param self The Flight which is to be updated.
   * @param other The Flight which is used to update self Flight.
   */

  public static void update(Flight self, Flight other) {
    allFlights.remove(self);
    allFlights.add(other);
    Boolean bo = exists(self);
    String st = bo.toString();
  }

  private static boolean exists(Flight flight) {
    return (allFlights.contains(flight));
  }

  /**
   * Saves the flight object into a serialized file.
   *
   * @param flight - The object which needs to be serialized and stored into a file.
   */
  private static void serializeFlight(Flight flight) {
    try {
      File relativeFile = new File(serializedFlightFilePath + flight.getFlightnum() + ".ser");
      // Find the path to the files stored, relative to the system
      // and write the object into the memory.
      FileOutputStream outputFile = new FileOutputStream(relativeFile.getPath());
      ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
      outputStream.writeObject(flight);
      outputFile.close();
      outputStream.close();
    } catch (IOException IoEx) {
      IoEx.printStackTrace();
    }
  }

  /**
   * Deserializes all of the Flights in the memory.
   *
   * @return Client
   * @throws IOException Throws this when IO fails.
   * @throws ClassNotFoundException Throws this if the class is not found.
   */
  private static Flight deserializeFlight(String path) throws IOException, ClassNotFoundException {
    Flight flight = null;
    try {
      // Initialize the IO streams.
      FileInputStream fileIn = new FileInputStream(path);
      ObjectInputStream objIn = new ObjectInputStream(fileIn);

      // Read the object and add it into the list of all flights.
      flight = (Flight) objIn.readObject();
      allFlights.add(flight);

      // Close the streams.
      objIn.close();
      fileIn.close();

    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    } catch (ClassNotFoundException classNotFound) {
      System.out.println("Flight class not found");
      classNotFound.printStackTrace();
    }

    return flight;
  }

  /**
   * Serializes all the users managed here.
   */
  private static void serializeAllFlights() {
    for (Flight flight : allFlights) {
      serializeFlight(flight);
    }
  }

  /**
   * Serializes all Flights so that a User's changes are saved.
   */
  public static void saveAllChanges() {
    serializeAllFlights();
  }


  /**
   * Serializes all the users managed here.
   *
   * @throws IOException - In the event this isn't valid
   * @throws ClassNotFoundException - In the event the given class object is not found in the file
   */
  public static void deserializeAllFlights() {
    File flightFile = new File(serializedFlightFilePath);
    String[] flightNames = flightFile.list();
    // find all the flights in the flights folder path.
    if (!(flightFile.list() == null)) {
      for (String flight : flightNames) {
        try {
          // load up all the flights in the applications memory.
          deserializeFlight(serializedFlightFilePath + flight);
        } catch (ClassNotFoundException noClass) {
          noClass.printStackTrace();
        } catch (IOException ioEx) {
          ioEx.printStackTrace();
        }
      }
    }
  }

  /**
   * Returns a String of all flights filtered by the inputed parameters.
   *
   * @param date A date in the format yyyy-MM-dd HH:mm
   * @param origin The origin of the flights.
   * @param destination The destination of the flights.
   * @return A string representation of all flights.
   */
  public String getFlights(String date, String origin, String destination) {
    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_AND_TIME);
    String returnValue = "";
    // Loop through all the flights and find all the relevant flights.
    for (Flight flight : allFlights) {
      // checks if there are any flights matching the date, origin, and destination.
      if ((format.format(flight.getDeparture()) == date) && (flight.getOrigin() == origin)
          && (flight.getDestination() == destination)) {
        // if yes, adds it to the return String.
        returnValue += flight.toString() + "," + flight.getPrice() + "\n";
      }
    }
    return returnValue;
  }

  /**
   * Returns all the flights entered into the system.
   * @return An array list of all flights.
   */
  public static ArrayList<Flight> getAllFlights() {
    return allFlights;
  }
}