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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Admin;

import java.io.Serializable;
import java.util.ArrayList;


public class FlightsView extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener, Serializable {

  private Admin admin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent;
    intent = getIntent();
    admin = (Admin) intent.getSerializableExtra("ADMIN");
    setContentView(R.layout.activity_flights_view);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ArrayList<Flight> flightList = FlightManager.getAllFlights();
    FlightAdapter flightAdapter = new FlightAdapter(this, flightList, admin);
    ListView flightView = (ListView) findViewById(R.id.flight_list);
    flightView.setAdapter(flightAdapter);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
        .navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);


  }


  @Override
  public void onStop() {
    super.onStop();
    this.finish();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.flights_view, menu);
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
      Toast.makeText(getApplicationContext(), "Please go back to the main menu to logout",
          Toast.LENGTH_LONG).show();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

  }

}
