package cs.bo7.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Admin;

public class AdminView extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener {

  public static final String ADMIN = "ADMIN";
  boolean pressOnce = false;

  Intent intent;
  Admin admin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    intent = getIntent();
    admin = (Admin) intent.getSerializableExtra(ADMIN);

    setContentView(R.layout.activity_admin_view);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
        .navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (pressOnce) {
        AccountManager.serializeAdmin(admin);
        finish();
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
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
      Toast.makeText(getApplicationContext(), "You have logged out.", Toast.LENGTH_LONG).show();
      AccountManager.serializeAdmin(admin);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

  }

  /**
   * Edits clients information.
   * @param view A view object.
   */
  public void editClientInfo(View view) {
    //Specifies event to switch to: AdminEditClientView
    Intent adminEditIntent = new Intent(this, AdminEditClientView.class);
    adminEditIntent.putExtra(ADMIN, getIntent().getSerializableExtra(ADMIN));
    //Switches to specified event
    startActivity(adminEditIntent);
  }

  /**
   * Lets the administrator view all Flights.
   * @param view A view object.
   */
  public void viewAllFlights(View view) {
    Intent allFlightsIntent = new Intent(this, FlightsView.class);
    allFlightsIntent.putExtra(ADMIN, getIntent().getSerializableExtra(ADMIN));
    startActivity(allFlightsIntent);
  }

  public void uploadFlightCsv(View view) {
    Intent uploadFlightIntent = new Intent(this, UploadFlightActivity.class);
    startActivity(uploadFlightIntent);
  }

  public void uploadClientCsv(View view) {
    Intent uploadClientIntent = new Intent(this, UploadClientActivity.class);
    startActivity(uploadClientIntent);
  }

  public void uploadAdminCsv(View view) {
    Intent uploadAdminIntent = new Intent(this, UploadAdminActivity.class);
    startActivity(uploadAdminIntent);
  }
}
