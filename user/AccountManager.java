package cs.bo7.p3.user;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FullFlightException;
import cs.bo7.p3.itinerary.Itinerary;
import cs.bo7.p3.itinerary.ItineraryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages all operations on Flights used in the application.
 *
 * @author Daerian
 * @author Simon
 * @author sakshaatchoyikandi
 */
public class AccountManager {

  // Instance variables

  // An array list that contains all the clients
  // An array list that contains all the administrators
  private static ArrayList<Client> clients = new ArrayList<>();
  private static ArrayList<Admin> admins = new ArrayList<>();

  // A string that stores the location of serialized Admins on the disk
  private static String serializedAdminFilePath; // users/Admin
  // string that stores the location of serialized Clients on the disk
  private static String serializedClientFilePath;

  public static void filePath(String filePath) {
    serializedAdminFilePath = filePath + "/admins/";
    serializedClientFilePath = filePath + "/clients/";
  }

  /**
   * Default constructor for an Account Manager.
   */
  public AccountManager() {
    try {
      deserializeAllUsers();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Default constructor for an Account Manager, given two preset list of accounts.
   *
   * @param clientList the list of all clients that will be managed by this manager
   * @param adminList the list of all admins
   */
  public AccountManager(ArrayList<Client> clientList, ArrayList<Admin> adminList) {
    AccountManager.setClients(new ArrayList<Client>());
    AccountManager.setAdmins(new ArrayList<Admin>());
  }

  /**
   * Logs in a certain user.
   *
   * @return A welcome message if the login is successful.
   * @throws UserNotFoundException - if no user exists with given info
   */
  public static AppUser login(String email, String pass) throws UserNotFoundException {

    Admin admin = findAdmin(email);
    if (admin != null && admin.getPassword().equals(pass)) {
      return admin;
    }


    Client client = findClient(email);
    if (client != null && client.getPassword().equals(pass)) {
      return client;
    }


    throw new UserNotFoundException("No such user, either " + "email or password is incorrect"
      + ". Please retry.");
  }

  /**
   * Returns the Client object associated with the email inputed.
   *
   * @param email An email address.
   * @return the Client object with email as their email parameter.
   */

  public static Client findClient(String email) throws UserNotFoundException {
    try {
      for (Client client : getClients()) {
        if (client.getEmail().equals(email)) {
          return client;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    throw new UserNotFoundException();
  }

  /**
   * Finds the Admin object associated with the email.
   *
   * @param email The email of the admin.
   * @return returns An admin with the given email.
   * @throws UserNotFoundException Thrown if the admin is not found .
   */
  public static Admin findAdmin(String email) {
    try {
      for (Admin admin : AccountManager.getAdmins()) {
        if (admin.getInfo().equals(email)) {
          return admin;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Register a new Client account, given some information.
   *
   * @param info Information that this account contains
   */
  public static void registerClient(String info) throws EmptySpaceException,
      UserAlreadyExistsException, IncorrectRegistrationException {

    String[] infor = info.split("\\s*,\\s*");

    if (isInfoEmpty(infor)) {
      throw new EmptySpaceException();
    }

    for (Client client : AccountManager.getClients()) {
      if (client.getEmail().equals(infor[2])) {
        throw new UserAlreadyExistsException();
      }
    }

    if (!((infor[4].length() == 16 && infor[5].length() == 10) && (infor[5].charAt(4) == '-')
        && (infor[5].charAt(7) == '-'))) {
      throw new IncorrectRegistrationException();
    }

    Client newClient = new Client(info);
    AccountManager.clients.add(newClient);

  }

  /**
   * Checks if the info is empty.
   *
   * @param clientList List of clients
   * @return boolean
   */
  public static boolean isInfoEmpty(String[] clientList) {
    for (String info : clientList) {
      if (info.equals("")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Register a new Admin, given the user-name and password.
   *
   * @param info - some information about Admin
   */
  public static void registerAdmin(String info) throws EmptySpaceException,
      UserAlreadyExistsException {
    //make sure it's not a client.

    if (info.isEmpty()) {
      throw new EmptySpaceException();
    }

    for (Client client : AccountManager.getClients()) {
      if (client.getEmail().equals(info)) {
        throw new UserAlreadyExistsException();
      }
    }

    for (Admin admin : AccountManager.getAdmins()) {
      if (admin.getInfo().equals(info)) {
        throw new UserAlreadyExistsException();
      }
    }
    AccountManager.getAdmins().add(new Admin(info, "password"));
  }

  /**
   * Uploads a CSV file consisting of clients into the application.
   *
   * @param filePath The path of the file.
   * @throws FileNotFoundException Thrown when the File is not there.
   * @throws ParseException Thrown when it cannot be parsed.
   */
  public static void uploadClientCsv(String filePath) throws FileNotFoundException, ParseException {
    //Scan the users int he path given.
    Scanner scan = new Scanner(new File(filePath));
    // As long as scanner finds another line.
    while (scan.hasNextLine()) {
      //Assigns line to the value of the line.
      String line = scan.nextLine();
      try {
        registerClient(line);
      } catch (UserAlreadyExistsException | IncorrectRegistrationException
          | EmptySpaceException e1) {
        e1.printStackTrace();
      }
    }
    scan.close();
  }

  /**
   * Loads information from a file and creates Client objects based on the information, given the
   * filePath.
   *
   * @param filePath - The file to read the information out of.
   * @throws FileNotFoundException - Throws if fileName given does not exist.
   * @throws ParseException - Throws if it can not convert from string to a date.
   */
  public static void uploadClientInfo(String filePath) throws FileNotFoundException,
      ParseException {

    uploadClientCsv(filePath);

    // Deserialize all the clients.
    File clientFile = new File(serializedClientFilePath);
    String[] clientNames = clientFile.list();
    // If the path contains files.
    if (!(clientFile.list() == null)) {
      for (String client : clientNames) {
        try {
          String temp = serializedClientFilePath;
          temp += client;
          deserializeClient(temp);
          // Handles ClassNotFoundException.
        } catch (ClassNotFoundException | IOException e) {
          e.printStackTrace();
          //Handles IOException.
        }
      }
    }
  }

  /**
   * Loads information from a file and creates Admin objects based on the information, given the
   * filePath.
   *
   * @param filePath - The file to read the information out of.
   * @throws FileNotFoundException - Throws if fileName given does not exist.
   * @throws ParseException - Throws if it can not convert from string to a date.
   */
  public static void uploadAdminInfo(String filePath) throws FileNotFoundException, ParseException {

    // Scan the users in the path given.
    Scanner scan = new Scanner(new File(filePath));
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      try {
        registerAdmin(line);
      } catch (EmptySpaceException | UserAlreadyExistsException e) {
        e.printStackTrace();
      }

    }
    scan.close();

    // De-serialize all the Admins.
    File adminFile = new File(serializedAdminFilePath);
    String[] adminNames = adminFile.list();
    // If the path contains files.
    if (!(adminFile.list() == null)) {
      // For every element in adminNames
      for (String admin : adminNames) {
        try {
          String temp = serializedAdminFilePath;
          temp += admin;
          deserializeAdmin(temp);
          // Handles ClassNotFoundException
        } catch (ClassNotFoundException | IOException e) {
          e.printStackTrace();
          // Handles IOException
        }
      }
    }
  }

  /**
   * Deseralises clients.
   *
   * @return Client
   * @throws IOException Throws this when IO fails
   * @throws ClassNotFoundException Throws this if the class is not found
   */
  public static Client deserializeClient(String filePath) throws IOException,
      ClassNotFoundException {
    Client client = null;
    try {
      // Creates a file input stream and object input stream
      FileInputStream fileIn = new FileInputStream(filePath);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      client = (Client) in.readObject();
      getClients().add(client);
      in.close();
      fileIn.close();
      // Handles IOException
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return client;
  }

  /**
   * Deseralies admins.
   *
   * @return Client
   * @throws IOException Throws this when IO fails
   * @throws ClassNotFoundException Throws this if the class is not found
   */
  public static Admin deserializeAdmin(String path) throws IOException, ClassNotFoundException {

    Admin admin = null;
    try {
      //Creates a file input stream and object input stream
      FileInputStream fileIn = new FileInputStream(path);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      admin = (Admin) in.readObject();
      getAdmins().add(admin);
      in.close();
      fileIn.close();
      // Handles IOException
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
      // Handles ClassNotFoundException
    } catch (ClassNotFoundException classNotFound) {
      classNotFound.printStackTrace();
      classNotFound.printStackTrace();
    }

    return admin;
  }

  /**
   * Saves the admin object into a serialized file, takes admin object.
   *
   * @param client - The object which we are serializing and storing into a file.
   */
  public static void serializeClient(Client client) {
    try {
      // Creates a temporaryFile named by client email
      String temporaryFile = serializedClientFilePath;
      temporaryFile += client.getEmail();
      temporaryFile += ".ser";
      File relativeFile = new File(temporaryFile);
      // Creates a file output stream and objet output stream
      FileOutputStream outputFile = new FileOutputStream(relativeFile.getPath());
      ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
      // Creates a serialized file with object client inside
      outputStream.writeObject(client);
      outputStream.close();
      outputFile.close();
      // Handles IOExceptions
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the admin object into a serialized file, takes admin object.
   *
   * @param client - The object which we are serializing and storing into a file.
   */
  public static void serializeClient(Client client, String filePath) {
    try {
      // Creates a temporaryFile named by client email
      String temporaryFile = filePath;
      temporaryFile += client.getEmail();
      temporaryFile += ".ser";
      File checkFile = new File(temporaryFile);
      if (checkFile.exists()) {
        checkFile.delete();
      }
      File relativeFile = new File(temporaryFile);
      // Creates a file output stream and object output stream
      FileOutputStream outputFile = new FileOutputStream(relativeFile.getPath());
      ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
      // Creates a serialized file with object client inside
      outputStream.writeObject(client);
      outputStream.close();
      outputFile.close();
      // Handles IOExceptions
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Saves the admin object into a serialized file, takes admin object.
   *
   * @param admin - The object which we are serializing and storing into a file.
   */
  public static void serializeAdmin(Admin admin, String filePath) {
    try {
      String temporaryFile = filePath;
      temporaryFile += admin.getInfo();
      temporaryFile += ".ser";
      // Creates a relativeFile named by the admin info (email address)
      File relativeFile = new File(temporaryFile);
      // Creates a file output stream and object output stream
      FileOutputStream outputFile = new FileOutputStream(relativeFile.getPath());
      ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
      outputStream.writeObject(admin);
      outputStream.close();
      outputFile.close();
      // Handles IOException
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the admin object into a serialized file, takes admin object.
   *
   * @param admin - The object which we are serializing and storing into a file.
   */
  public static void serializeAdmin(Admin admin) {
    try {
      // Creates a relativeFile named by the admin info (email address)
      File relativeFile = new File(serializedAdminFilePath + admin.getInfo() + ".ser");
      // Creates a file output stream and object output stream
      FileOutputStream outputFile = new FileOutputStream(relativeFile.getPath());
      ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
      outputStream.writeObject(admin);
      outputStream.close();
      outputFile.close();
      // Handles IOException
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Serializes all the users managed here.
   */
  private static void serializeAllUsers() {
    // For every client object in the AccountManager static list clients.
    for (Client client : getClients()) {
      serializeClient(client);
    }
    // For every admin object in the AccountManager static list for admins.
    for (Admin admin : getAdmins()) {
      serializeAdmin(admin);
    }
  }

  /**
   * Saves all changes made to users. Serialize all the users handled by this class.
   */
  public static void saveAllChanges() {
    serializeAllUsers();
  }

  /**
   * Serializes all the users managed here.
   *
   * @throws IOException - In the event this isn't valid
   * @throws ClassNotFoundException - In the event the given class object is not found in the file
   */
  private void deserializeAllUsers(String filePath) throws ClassNotFoundException, IOException {
    File clientFile = new File(serializedClientFilePath);
    File adminFile = new File(serializedAdminFilePath);
    String[] clientNames = clientFile.list();
    String[] adminNames = adminFile.list();
    // If there are files in the path
    if (!(clientFile.list() == null)) {
      // For every file name in the String list clientNames
      for (String client : clientNames) {
        try {
          String temp = serializedClientFilePath;
          temp += client;
          deserializeClient(temp);
          // Handles ClassNotFoundException
        } catch (ClassNotFoundException noClass) {
          noClass.printStackTrace();
          // Handles IOException
        } catch (IOException ioEx) {
          ioEx.printStackTrace();
        }
      }
    }
    // If there are no files in the path
    if (!(adminFile.list() == null)) {
      // For every file name in the String list adminNames
      for (String admin : adminNames) {
        try {
          String temporaryPath = serializedAdminFilePath;
          temporaryPath += admin;
          deserializeAdmin(temporaryPath);
          // Handles ClassNotFoundException
        } catch (ClassNotFoundException noClass) {
          noClass.printStackTrace();
          // Handles IOException
        } catch (IOException ioEx) {
          ioEx.printStackTrace();
        }
      }
    }
  }

  /**
   * Serializes all the users managed here.
   */
  public static void deserializeAllUsers() {
    File clientFile = new File(serializedClientFilePath);
    File adminFile = new File(serializedAdminFilePath);
    String[] clientNames = clientFile.list();
    String[] adminNames = adminFile.list();
    // If there are files in the path
    if (!(clientFile.list() == null)) {
      // For every file name in the String list clientNames
      for (String client : clientNames) {
        try {
          String temp = serializedClientFilePath;
          temp += client;
          deserializeClient(temp);
          // Handles ClassNotFoundException
        } catch (ClassNotFoundException | IOException e) {
          e.printStackTrace();
          // Handles IOException
        }
      }
    }
    // If there are no files in the path
    if (!(adminFile.list() == null)) {
      // For every file name in the String list adminNames
      for (String admin : adminNames) {
        try {
          String temporaryPath = serializedAdminFilePath;
          temporaryPath += admin;
          deserializeAdmin(temporaryPath);
          // Handles ClassNotFoundException
        } catch (ClassNotFoundException | IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Creates a new booking for the provided Client.
   *
   * @param trip - the itinerary to book
   * @param user - the client to book for
   */
  public void createBooking(Itinerary trip, Client user) throws BookingAlreadyExistsException,
        FullFlightException {

    ItineraryManager.checkStatus(trip);

    if (user.getBookings().contains(trip)) {
      throw new BookingAlreadyExistsException("Booking Already Exists");
    } else {
      user.bookItinerary(trip);
      for (Flight flight : trip.getFlights()) {
        flight.addObserver(trip);
        flight.booked();
      }
    }
  }

  public static ArrayList<Client> getClients() {
    return clients;
  }

  public static void setClients(ArrayList<Client> clients) {
    AccountManager.clients = clients;
  }

  public static ArrayList<Admin> getAdmins() {
    return admins;
  }

  public static void setAdmins(ArrayList<Admin> admins) {
    AccountManager.admins = admins;
  }

}