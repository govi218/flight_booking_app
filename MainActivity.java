package cs.bo7.p3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Admin;
import cs.bo7.p3.user.AppUser;
import cs.bo7.p3.user.Client;
import cs.bo7.p3.user.EmptySpaceException;
import cs.bo7.p3.user.UserAlreadyExistsException;
import cs.bo7.p3.user.UserNotFoundException;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    loadUserData();
    try {
      AccountManager.registerAdmin("a");
    } catch (EmptySpaceException e) {
      e.printStackTrace();
    } catch (UserAlreadyExistsException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads all user data onto the application.
   */
  private void loadUserData() {

    String filePath = getFilesDir() + "/clients/";
    File file = new File(filePath);
    file.mkdir();

    String filePath2 = getFilesDir() + "/admins/";
    File file2 = new File(filePath2);
    file2.mkdir();

    String filePath3 = getFilesDir() + "/flights/";
    File file3 = new File(filePath3);
    file3.mkdir();

    AccountManager.filePath(getFilesDir().getPath());
    AccountManager.deserializeAllUsers();
    FlightManager.filePath(getFilesDir().getPath());
    FlightManager.deserializeAllFlights();
  }

  /**
   * Logs in an AppUser
   * @param view A View Object.
   */
  public void login(View view) {
    findViewById(R.id.failure).setVisibility(View.INVISIBLE);
    // Intent adminViewIntent = new Intent(this, AdminView.class);
    // Gets the username from the 1st EditText field.
    EditText usernameText = (EditText) findViewById(R.id.username);
    String username = usernameText.getText().toString();
    // Gets the password from the 1st EditText field.
    EditText passwordText = (EditText) findViewById(R.id.password);
    String password = passwordText.getText().toString();
    AppUser newUser;
    try {
      newUser = AccountManager.login(username, password);

      if (newUser instanceof Client) {
        Intent clientViewIntent = new Intent(this, ClientView.class);
        clientViewIntent.putExtra(ClientView.CLIENT, newUser);
        startActivity(clientViewIntent);
      } else if (newUser instanceof Admin) {
        Intent adminViewIntent = new Intent(this, AdminView.class);
        adminViewIntent.putExtra(AdminView.ADMIN, newUser);
        startActivity(adminViewIntent);
      }
    } catch (UserNotFoundException e) {
      TextView result = (TextView) findViewById(R.id.failure);
      result.setVisibility(View.VISIBLE);
      Random rnd = new Random();
      int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
      result.setTextColor(color);
      Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
      vib.vibrate(100);
    }

  }

  /**
   * Registers a new User.
   * @param view A View object.
   */
  public void register(View view) {
    Intent registerIntent = new Intent(this, RegisterActivity.class);
    startActivity(registerIntent);
  }


}
