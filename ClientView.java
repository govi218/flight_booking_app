package cs.bo7.p3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Client;

public class ClientView extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener {

  public static final String CLIENT = "CLIENT";
  private Client client;
  boolean pressOnce = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_view);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
        .navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    screenUpdater();
    navigationView.setNavigationItemSelectedListener(this);
  }

  /**
   * Updates the screen with the updated information about the client.
   */
  private void screenUpdater() {
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

    Intent intent;

    intent = getIntent();
    client = (Client) intent.getSerializableExtra(CLIENT);

    TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
    String newText = String.format("%s %s", client.getFirstName(), client.getLastName());
    name.setText(newText);

    TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
    email.setText(client.getEmail());

    TextView newName = (TextView) findViewById(R.id.firstlastname);
    newName.setText(String.format("Hi, %s %s.", client.getFirstName(), client.getLastName()));

    TextView itineraryNum = (TextView) findViewById(R.id.allItineraries);
    itineraryNum.setText(String.format("You have %s booked itineraries.", client.numBookings()));
  }


  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (pressOnce) {
        finish();
        AccountManager.saveAllChanges();
        Toast.makeText(getApplicationContext(), "You have logged out.", Toast.LENGTH_LONG).show();
      }

      this.pressOnce = true;
      Toast.makeText(getApplicationContext(), "Double click back button to log out", Toast
          .LENGTH_SHORT).show();

      new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
          pressOnce = false;
        }
      }, 500);
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    // Get the client file logged in.
    Client client = (Client) getIntent().getSerializableExtra(CLIENT);


    if (id == R.id.nav_search) {
      search();
    } else if (id == R.id.nav_flights) {
      viewAllFlights();
    } else if (id == R.id.nav_bookings) {
      getBookings();
    } else if (id == R.id.nav_manage) {
      // This part of the code gets run if the client wants to edit their information.
      Intent intent = new Intent(this, ClientEditActivity.class);
      intent.putExtra(CLIENT, client);
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

  public void search() {
    // Search for itineraries.
    Intent searchActivityIntent = new Intent(this, SearchView.class);
    searchActivityIntent.putExtra(CLIENT, client);
    startActivityForResult(searchActivityIntent, 1);
  }

  public void search(View view) {
    // Search for itineraries.
    search();
  }

  public void getBookings(View view) {
    getBookings();
  }

  private void getBookings() {
    Intent itineraryListIntent = new Intent(this, ItineraryListView.class);
    itineraryListIntent.putExtra(CLIENT, client);
    startActivity(itineraryListIntent);
  }


  /**
   * The method responsible for getting result from the activity.
   * @param request The request number.
   * @param result Tells the method if the result is right or not.
   * @param intent The updated intent passed back to the caller.
   */
  protected void onActivityResult(int request, int result, Intent intent) {
    super.onActivityResult(request, result, intent);
    if (result == RESULT_OK) {
      Client client = (Client) intent.getSerializableExtra(CLIENT);
      Client oldClient = (Client) getIntent().getSerializableExtra(CLIENT);
      getIntent().removeExtra(CLIENT);
      getIntent().putExtra(CLIENT, client);
      oldClient.updateClient(client);
      screenUpdater();
    }
  }

  /**
   * Lets the client view all flights.
   */
  public void viewAllFlights() {
    Intent allFlightsIntent = new Intent(this, ClientFlightViews.class);
    allFlightsIntent.putExtra("CLIENT", client);
    startActivity(allFlightsIntent);
  }

  /**
   * Lets the client view all flights.
   * @param view A View object.
   */
  public void viewAllFlights(View view) {
    viewAllFlights();
  }

}
