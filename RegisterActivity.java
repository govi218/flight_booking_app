package cs.bo7.p3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.EmptySpaceException;
import cs.bo7.p3.user.IncorrectRegistrationException;
import cs.bo7.p3.user.UserAlreadyExistsException;

public class RegisterActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public void onBackPressed() {
    finish();
  }

  /**
   * Registers a new client into the application.
   * @param view the view
   */
  public void registerClient(View view) {

    // Gets the username from the 1st EditText field.
    EditText registerUsernameText = (EditText) findViewById(R.id.username_register);
    String registerUsername = registerUsernameText.getText().toString();

    // Gets the first name from the 3rd EditText field.
    EditText registerFirstNameText = (EditText) findViewById(R.id.first_name_register);
    String registerFirstName = registerFirstNameText.getText().toString();

    // Gets the last name from the 4th EditText field.
    EditText registerLastNameText = (EditText) findViewById(R.id.last_name_register);
    String registerLastName = registerLastNameText.getText().toString();

    String allClientInfo = "";
    allClientInfo += registerFirstName + ",";
    allClientInfo += registerLastName + ",";
    allClientInfo += registerUsername + ",";

    // Gets the last name from the 5th EditText field.
    EditText registerAddressText = (EditText) findViewById(R.id.address_register);
    String registerAddress = registerAddressText.getText().toString();
    allClientInfo += registerAddress + ",";

    // Gets the credit card number  from the 6th EditText field.
    EditText registerCreditCardText = (EditText) findViewById(R.id.credit_card_number_register);
    String registerCreditCard = registerCreditCardText.getText().toString();
    allClientInfo += registerCreditCard + ",";

    // Gets the credit card expiry date from the 7th EditText field.
    EditText registerExpiryDateText = (EditText) findViewById(R.id
            .credit_card_expiry_date_register);
    String registerExpiryDate = registerExpiryDateText.getText().toString();
    allClientInfo += registerExpiryDate + ",";

    // Gets the password from the 2nd EditText field.
    EditText registerPasswordText = (EditText) findViewById(R.id.password_register);
    String registerPassword = registerPasswordText.getText().toString();
    allClientInfo += registerPassword;

    try {
      // Try to register the client.
      AccountManager.registerClient(allClientInfo);
      finish();
    } catch (UserAlreadyExistsException e) {
      // If the user already exists, inform the user and tell them to pick a new email.
      Toast.makeText(getApplicationContext(), "The user already exists, please pick another "
          + "username.", Toast.LENGTH_SHORT).show();
    } catch (IncorrectRegistrationException e) {
      // If the user formats the information incorrectly, inform them,
      Toast.makeText(getApplicationContext(), "The information you entered is not formatted "
          + "correctly.", Toast.LENGTH_SHORT).show();
    } catch (EmptySpaceException e2) {
      // If an empty field is entered, inform the user.
      Toast.makeText(getApplicationContext(), "Please do not leave any empty fields.", Toast
          .LENGTH_SHORT).show();

    }


  }

}
