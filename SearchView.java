package cs.bo7.p3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import cs.bo7.p3.user.Client;

import java.util.ArrayList;

public class SearchView extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
  }

  /**
   * Gets the parameters for the search.
   * @param view A View object.
   */
  public void getSearchParams(View view) {

    // Gets the username from the 1st EditText field.
    EditText userOrigin = (EditText) findViewById(R.id.origin_location);
    String origin = userOrigin.getText().toString();

    // Gets the password from the 2nd EditText field.
    EditText userDestination = (EditText) findViewById(R.id.destination_location);
    String destination = userDestination.getText().toString();

    // Gets the departureDate.
    EditText userDeparture = (EditText) findViewById(R.id.departure_date);
    String departureDate = userDeparture.getText().toString();

    // Store the information and pass the intent to the ItineraryListView Activity.
    ArrayList<String> params = new ArrayList<>();
    params.add(departureDate);
    params.add(origin);
    params.add(destination);

    Intent searchViewIntent = getIntent();
    Client client = (Client) searchViewIntent.getSerializableExtra(ClientView.CLIENT);

    Intent itinerariesViewIntent = new Intent(this, ItineraryListView.class);
    itinerariesViewIntent.putStringArrayListExtra("params", params);
    itinerariesViewIntent.putExtra(ClientView.CLIENT, client);
    startActivityForResult(itinerariesViewIntent, 1);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      Intent newIntent = new Intent();
      Client client = (Client) data.getSerializableExtra(ClientView.CLIENT);
      newIntent.putExtra(ClientView.CLIENT, client);
      System.out.println(client.numBookings() + "hello");
      setResult(RESULT_OK, newIntent);
      finish();
    }
  }
}