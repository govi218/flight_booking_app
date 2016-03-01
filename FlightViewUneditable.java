package cs.bo7.p3;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Client;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class FlightViewUneditable extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    Client client = (Client) intent.getSerializableExtra("CLIENT");;
    Flight flight = (Flight) intent.getSerializableExtra("FLIGHT");
    setContentView(R.layout.activity_flight_view_uneditable);
    SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_AND_TIME);
    DecimalFormat df = new DecimalFormat("#.00");

    //Finding all the TextView views where the editable information will be displayed.
    TextView flightNumberView = (TextView) findViewById(R.id.display_flight_number2);
    TextView departureView = (TextView) findViewById(R.id.display_departure2);
    TextView arrivalView = (TextView) findViewById(R.id.display_arrival2);
    flightNumberView.setText("Flight Number: " + flight.getFlightnum());
    String arrival = "Arrival Date: " + format.format(flight.getArrival());
    arrivalView.setText(arrival);
    String departure = "Departure Date: " + format.format(flight.getArrival());
    departureView.setText(departure);

    TextView airlineView = (TextView) findViewById(R.id.display_airline2);
    airlineView.setText("Airline: " + flight.getAirline());

    TextView originView = (TextView) findViewById(R.id.display_origin2);
    originView.setText("Origin: " + flight.getOrigin());

    TextView destinationView = (TextView) findViewById(R.id.display_destination2);
    destinationView.setText("Destination: " + flight.getDestination());

    TextView priceView = (TextView) findViewById(R.id.display_price2);

    priceView.setText(String.format( "Price: $ %.2f", flight.getPrice() ));

    TextView numSeatsView = (TextView) findViewById(R.id.display_numSeats2);
    numSeatsView.setText(String.valueOf("Available Seats: " + flight.getNumSeats()));
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    // Get the client file logged in.
    Client client = (Client) getIntent().getSerializableExtra(ClientView.CLIENT);

    Intent searchActivityIntent = new Intent(this, SearchView.class);
    searchActivityIntent.putExtra(ClientView.CLIENT, client);

    if (id == R.id.nav_search) {
      // Search for itineraries.
      startActivity(searchActivityIntent);
    } else if (id == R.id.nav_bookings) {
      // View bookings.
      startActivityForResult(searchActivityIntent, 1);
    } else if (id == R.id.nav_slideshow) {
      startActivity(searchActivityIntent);
      item.setCheckable(false);
    } else if (id == R.id.nav_manage) {
      // This part of the code gets run if the client wants to edit their information.
      Intent intent = new Intent(this, ClientEditActivity.class);
      intent.putExtra(ClientView.CLIENT, client);
      startActivityForResult(intent, 1);
    } else if (id == R.id.nav_logout) {
      // Logs out the user.
      finish();
      AccountManager.saveAllChanges();
      Toast.makeText(getApplicationContext(), "You have logged out.", Toast.LENGTH_LONG).show();
    }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

  }

  // Goes back to the previous screen.
  public void flightUneditableBack(View view) {
      finish();
      Intent allFlightsIntentUneditable = new Intent(this, FlightsView.class);
      allFlightsIntentUneditable.putExtra("CLIENT", getIntent().getSerializableExtra("CLIENT"));
      startActivity(allFlightsIntentUneditable);
  }

    @Override
    public void onBackPressed() {
        finish();
        Intent allFlightsIntentUneditable = new Intent(this, FlightsView.class);
        allFlightsIntentUneditable.putExtra("CLIENT", getIntent().getSerializableExtra("CLIENT"));
        startActivity(allFlightsIntentUneditable);
    }


}
