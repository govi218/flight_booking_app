package cs.bo7.p3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.user.Client;

import java.util.ArrayList;

public class ClientFlightAdapter extends BaseAdapter implements ListAdapter {

  private ArrayList<Flight> flights;
  private Context context;
  private Client client;

  public ClientFlightAdapter(Context context, ArrayList<Flight> flights, Client client) {
    this.flights = flights;
    this.context = context;
    this.client = client;
  }

  @Override
  public int getCount() {
    return flights.size();
  }

  @Override
  public Object getItem(int pos) {
    return flights.get(pos);
  }

  @Override
  public long getItemId(int pos) {
    return 0;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
          .LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.custom_client_flight_list, null);
    }

    Flight curFlight = flights.get(position);

    //Handle TextView and display flight number and airline
    TextView flightNumText = (TextView) view.findViewById(R.id.flight_num2); // flight number
    flightNumText.setText(curFlight.getFlightnum());
    TextView airlineText = (TextView) view.findViewById(R.id.airline2); // airline
    airlineText.setText(curFlight.getAirline());
    //Handle buttons and add onClickListeners
    ImageButton viewFlightButton = (ImageButton) view.findViewById(R.id.view_client_flight_button);
    viewFlightButton.setOnClickListener(new View.OnClickListener() {
      /**
       * View the flight.
       * @param vi - The view to pass.
       */
      @Override
      public void onClick(View vi) {
        Flight curFlight = flights.get(position);
        Intent editFlightIntent = new Intent(context, FlightViewUneditable.class);
        editFlightIntent.putExtra("FLIGHT", curFlight);
        editFlightIntent.putExtra("CLIENT", client);
        context.startActivity(editFlightIntent);
      }
    });
    notifyDataSetChanged();

    return view;
  }
}