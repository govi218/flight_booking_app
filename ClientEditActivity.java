package cs.bo7.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.Client;
import cs.bo7.p3.user.EmptySpaceException;
import cs.bo7.p3.user.IncorrectRegistrationException;


public class ClientEditActivity extends AppCompatActivity {

  Intent intent;
  Client client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    intent = getIntent();
    client = (Client) intent.getSerializableExtra("CLIENT");
    setContentView(R.layout.activity_client_edit);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Get the client passed on to this class by the intent.
    Intent intent = getIntent();
    Client client = (Client) intent.getSerializableExtra(ClientView.CLIENT);

    // Retrieve information from users.
    EditText updateFirstName = (EditText) findViewById(R.id.edit_first);
    updateFirstName.setText(client.getFirstName());

    EditText updateLastName = (EditText) findViewById(R.id.edit_last);
    updateLastName.setText(client.getLastName());

    EditText updatePassword = (EditText) findViewById(R.id.edit_pass);
    updatePassword.setText(client.getPassword());

    EditText updateAddress = (EditText) findViewById(R.id.edit_add);
    updateAddress.setText(client.getAddress());

    EditText updateCcn = (EditText) findViewById(R.id.edit_ccn);
    updateCcn.setText(client.getCreditCardNumber());

    EditText updateExp = (EditText) findViewById(R.id.edit_exp);
    updateExp.setText(client.getExpiryDate());

  }

  public void search() {
    // Search for itineraries.
    Intent searchActivityIntent = new Intent(this, SearchView.class);
    searchActivityIntent.putExtra("CLIENT", client);
    startActivityForResult(searchActivityIntent, 1);
  }

  public void search(View view) {
    // Search for itineraries.
    search();
  }

  /**
   * Saves the edits made by the client.
   *
   * @param view A View object.
   */
  public void saveChanges(View view) {
    // Make an alert dialog to ask them if they are sure.
    AlertDialog.Builder areYouSure = new AlertDialog.Builder(this).setTitle("Save Changes");

    // Set a message for the dialog.
    areYouSure.setMessage("Are you sure you want to save these changes?");

    areYouSure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        try {
          // Try to update.
          Client client = update();
          Toast.makeText(getApplicationContext(), "All changes have been saved.", Toast
              .LENGTH_LONG).show();
          Intent newIntent = new Intent();
          newIntent.putExtra(ClientView.CLIENT, client);
          setResult(RESULT_OK, newIntent);
          finish();
        } catch (EmptySpaceException e) {
          // If one of the fields is empty, tell the user to fix it.
          Toast.makeText(getApplicationContext(), "Please complete all fields.", Toast
              .LENGTH_LONG).show();
        } catch (IncorrectRegistrationException e) {
          // If one of the fields is formatted incorrectly, tell the user to fix it.
          Toast.makeText(getApplicationContext(), "Incorrectly formatted field entered.", Toast
              .LENGTH_LONG).show();
        }
      }
    });

    // Create a negative response for the Alert Dialog.
    areYouSure.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // closes the activity without saving anything
        setResult(RESULT_CANCELED, null);
        finish();
      }
    });

    // Neutral button lets you go back and change more stuff.
    areYouSure.setNeutralButton("Cancel", null);

    // Show the alert.
    areYouSure.show();


  }

  /**
   * Updates the Client with new information.
   * @return the client with new information.
   * @throws EmptySpaceException If there is an empty space
   * @throws IncorrectRegistrationException If the registration is incorrect
   */
  private Client update() throws EmptySpaceException, IncorrectRegistrationException {



    // Retrieve the information the client puts in.
    EditText updateFirstName = (EditText) findViewById(R.id.edit_first);
    String firstName = updateFirstName.getText().toString();
    // Check if the field is empty, if yes, then throw an error.
    if (firstName.equals("")) {
      throw new EmptySpaceException();
    }

    EditText updateLastName = (EditText) findViewById(R.id.edit_last);
    String lastName = updateLastName.getText().toString().trim();
    if (lastName.equals("")) {
      throw new EmptySpaceException();
    }

    EditText updatePassword = (EditText) findViewById(R.id.edit_pass);
    String password = updatePassword.getText().toString().trim();
    if (password.equals("")) {
      throw new EmptySpaceException();
    }

    EditText updateAddress = (EditText) findViewById(R.id.edit_add);
    String address = updateAddress.getText().toString().trim();
    if (address.equals("")) {
      throw new EmptySpaceException();
    }

    EditText updateCcn = (EditText) findViewById(R.id.edit_ccn);
    String ccn = updateCcn.getText().toString().trim();
    if (ccn.equals("")) {
      throw new EmptySpaceException();
    }

    EditText updateExp = (EditText) findViewById(R.id.edit_exp);
    String exp = updateExp.getText().toString().trim();
    if (exp.equals("")) {
      throw new EmptySpaceException();
    }
    // Retrieve the client from the Intent.
    Intent intent = getIntent();

    Client client = (Client) intent.getSerializableExtra(ClientView.CLIENT);
    // Edit client information.
    client.editInfo(Constants.EXPIRY, exp);
    client.editInfo(Constants.CCN, ccn);
    client.editInfo(Constants.LAST_NAME, lastName);
    client.editInfo(Constants.FIRST_NAME, firstName);
    client.editInfo(Constants.PASSWORD, password);
    client.editInfo(Constants.ADDRESS, address);
    AccountManager.serializeClient(client);

    return client;


  }


}
