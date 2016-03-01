package cs.bo7.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FlightEditActivity extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener {

  public static final String FLIGHT = "FLIGHT";

  private Flight flight;
  private Admin admin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    flight = (Flight) intent.getSerializableExtra(FLIGHT);
    admin = (Admin) intent.getSerializableExtra("ADMIN");

    setContentView(R.layout.activity_flight_edit);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
        .navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    //Finding all the EditText views where the editable information will be displayed.
    EditText flightNumberView = (EditText) findViewById(R.id.display_flight_number_edit);
    EditText arrivalView = (EditText) findViewById(R.id.display_arrival_edit);
    EditText numSeatsView = (EditText) findViewById(R.id.display_numSeats);

    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_AND_TIME);
    flightNumberView.setText(flight.getFlightnum());
    arrivalView.setText(format.format(flight.getArrival()));
    numSeatsView.setText(String.valueOf(flight.getNumSeats()));

    EditText departureView = (EditText) findViewById(R.id.display_departure_edit);
    departureView.setText(format.format(flight.getDeparture()));

    EditText airlineView = (EditText) findViewById(R.id.display_airline_edit);
    airlineView.setText(flight.getAirline());

    EditText originView = (EditText) findViewById(R.id.display_origin_edit);
    originView.setText(flight.getOrigin());

    EditText destinationView = (EditText) findViewById(R.id.display_destination_edit);
    destinationView.setText(flight.getDestination());

    EditText priceView = (EditText) findViewById(R.id.display_price_edit);
    priceView.setText(String.format( "%.2f", flight.getPrice()));
  }

    @Override
    public void onBackPressed() {
        finish();
        Intent allFlightsIntent = new Intent(this, FlightsView.class);
        allFlightsIntent.putExtra("ADMIN", getIntent().getSerializableExtra("ADMIN"));
        startActivity(allFlightsIntent);
    }

  /**
   * Given some information, updates the old info to reflect the updated information.
   *
   * @param view Some View object.
   */
  public void updateFlightInformation(View view) {
    //Get all the EditText Fields from the edit flights page.
    EditText editFlightNumberText = (EditText) findViewById(R.id.display_flight_number_edit);
    EditText editDepartureText = (EditText) findViewById(R.id.display_departure_edit);
    EditText editArrivalText = (EditText) findViewById(R.id.display_arrival_edit);
    EditText editAirlineText = (EditText) findViewById(R.id.display_airline_edit);
    EditText editOriginText = (EditText) findViewById(R.id.display_origin_edit);
    EditText editDestinationText = (EditText) findViewById(R.id.display_destination_edit);
    EditText editPriceText = (EditText) findViewById(R.id.display_price_edit);
    EditText editNumSeatsText = (EditText) findViewById(R.id.display_numSeats);



    String editFlightNumber = editFlightNumberText.getText().toString();


    String editDeparture = editDepartureText.getText().toString();
    String[] editDepartureList = editDeparture.split(" ");

    String editArrival = editArrivalText.getText().toString();
    String[] editArrivalList = editArrival.split(" ");


    String editAirline = editAirlineText.getText().toString();
    String editOrigin = editOriginText.getText().toString();
    String editDestination = editDestinationText.getText().toString();
    String editPrice = editPriceText.getText().toString();
    String editNumSeats = editNumSeatsText.getText().toString();


    try {
      Flight newFlight = new Flight(editFlightNumber, editAirline, editOrigin, editDestination,
          editDepartureList[0], editArrivalList[0], editDepartureList[1], editArrivalList[1],
          editPrice, editNumSeats);
      FlightManager.update(flight, newFlight);
      this.finish();
      Intent flightView = new Intent(this, FlightsView.class);
      flightView.putExtra("ADMIN", admin);
      startActivity(flightView);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.flight_edit, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_manage) {
      // Creating an AlertDialog.
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Type in your new password");
      final EditText input = new EditText(this);
      builder.setView(input);

      // Creating the Positive Button
      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          String newPass = input.getText().toString();
          if (newPass.equals("")) {
            // Do not let the administrator change their password to an empty string.
            Toast.makeText(getApplicationContext(), "Invalid characters, try again.", Toast
                .LENGTH_LONG).show();
          } else {
            // If the password is valid, set it and display message.
            admin.setPassword(newPass);
            AccountManager.serializeAdmin(admin);
            Toast.makeText(getApplicationContext(), String.format("Password changed to %s",
                newPass), Toast.LENGTH_LONG).show();
          }
        }
      });

      builder.show();

    } else if (id == R.id.nav_logout) {
      this.finish();
      Toast.makeText(getApplicationContext(), "You have logged out.", Toast.LENGTH_LONG).show();
      AccountManager.serializeAdmin(admin);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

  }

}
