package cs.bo7.p3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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
import cs.bo7.p3.user.Client;
import cs.bo7.p3.user.UserNotFoundException;

public class AdminEditClientView extends AppCompatActivity implements NavigationView
    .OnNavigationItemSelectedListener {

  public static final String ADMIN = "ADMIN";
  boolean pressOnce = false;

  Intent intent;
  Admin admin;
  Client client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    intent = getIntent();
    admin = (Admin) intent.getSerializableExtra(ADMIN);

    setContentView(R.layout.activity_admin_edit_client);

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

  public void searchClient(View view) {

    EditText clientEmailText = (EditText) findViewById(R.id.clientemail);
    String clientEmail = clientEmailText.getText().toString();

    try {
      // Try to find the client.
      client = AccountManager.findClient(clientEmail);
      Intent clientEditViewIntent = new Intent(this, ClientEditActivity.class);
      // Put the client inside the intent and start next activity.
      clientEditViewIntent.putExtra(ClientView.CLIENT, client);
      startActivityForResult(clientEditViewIntent, 1);
    } catch (UserNotFoundException e1) {
      // If client cannot be found, let the user know.
      findViewById(R.id.morefailure).setVisibility(View.VISIBLE);
      Vibrator vib = (Vibrator) getApplicationContext()
          .getSystemService(Context.VIBRATOR_SERVICE);
      vib.vibrate(100);
    }
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
      Client newClient = (Client) intent.getSerializableExtra(ClientView.CLIENT);
      getIntent().removeExtra(ClientView.CLIENT);
      getIntent().putExtra(ClientView.CLIENT, newClient);
      client.updateClient(newClient);
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
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
      Toast.makeText(getApplicationContext(), "Please go back to the main menu to logout",
          Toast.LENGTH_LONG).show();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;

  }

}

