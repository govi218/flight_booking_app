package cs.bo7.p3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.user.Client;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientFlightViews extends AppCompatActivity implements Serializable {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent;
    Client client;
    intent = getIntent();
    client = (Client) intent.getSerializableExtra("CLIENT");
    setContentView(R.layout.activity_client_flights_view);
    ArrayList<Flight> flightList = FlightManager.getAllFlights();
    ClientFlightAdapter flightAdapter = new ClientFlightAdapter(this, flightList, client);
    ListView flightView = (ListView) findViewById(R.id.flight_list);
    flightView.setAdapter(flightAdapter);
  }

  @Override
  public void onStop() {
    super.onStop();
    this.finish();
  }

  @Override
  public void onBackPressed() {
    finish();
  }

}
